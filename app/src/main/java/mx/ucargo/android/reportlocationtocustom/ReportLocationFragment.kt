package mx.ucargo.android.reportlocationtocustom

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.action_event_button.*
import kotlinx.android.synthetic.main.driver_profile_activity.*
import mx.ucargo.android.R
import mx.ucargo.android.destinaionreport.ActionDialogFragment
import mx.ucargo.android.destinaionreport.ReportDestinationFragment
import mx.ucargo.android.orderdetails.OrderDetailsActivity
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.reportsign.ReportSignActivity
import javax.inject.Inject

private val TAG = OrderDetailsActivity::class.java.simpleName

class ReportLocationFragment : Fragment(), PermissionListener {


    companion object {
        private const val ORDER_ID = "ORDER_ID"
        private const val TYPE_ORDER = "TYPE_ORDER"

        fun newInstance(orderId: String,typeOrder: String): ReportLocationFragment {
            val arguments = Bundle()
            arguments.putString(ORDER_ID, orderId)
            arguments.putString(TYPE_ORDER,typeOrder)

            val fragment = ReportLocationFragment()
            fragment.arguments = arguments
            return fragment
        }
    }

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    /**
     * Constant used in the location settings dialog.
     */
    private val REQUEST_CHECK_SETTINGS = 0x1

    var REQUESTCODE = 0
    val SIGNEDREQUESTCODE = 111

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 15000

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2

    // Keys for storing activity state in the Bundle.
    private val KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates"
    private val KEY_LOCATION = "location"
    private val KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string"

    /**
     * Provides access to the Fused Location Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    /**
     * Provides access to the Location Settings API.
     */
    private var mSettingsClient: SettingsClient? = null

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private var mLocationRequest: LocationRequest? = null

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private var mLocationSettingsRequest: LocationSettingsRequest? = null

    /**
     * Callback for Location events.
     */
    private var mLocationCallback: LocationCallback? = null

    /**
     * Represents a geographical location.
     */
    private var mCurrentLocation: Location? = null

    lateinit var orderId: String

    @Inject
    lateinit var orderDetailsViewModel: OrderDetailsViewModel

    @Inject
    lateinit var  locationViewModel: ReportLocationViewModel

    lateinit var orderType: String


    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderId = arguments?.getString(ORDER_ID)!!
        orderType = arguments?.getString(TYPE_ORDER)!!

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context!!)
        mSettingsClient = LocationServices.getSettingsClient(this.context!!)
        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
        onCheckPermisionToLocationTracking()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.action_event_button, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        locationViewModel.orderStatus.observe(this, orderStatusbserver)

        if (orderType.toInt() == 1){
            actionButton.setText(R.string.order_detail_arrive_delivery_destination_export)
            actionButton.setOnClickListener{
                stopLocationUpdates()
                locationViewModel.orderStatus.postValue(OrderDetailsModel.Status.REPORTCUSTOMEXPORT)
            }
        }else if(orderType.toInt() == 0) {
            actionButton.setText(R.string.order_detail_arrive_delivery_destination)
            actionButton.setText(getString(R.string.begin_route_or_store))
            actionButton.setOnClickListener {
                val confirmDataFragment = ConfirmDialogFragment()
                confirmDataFragment.setTargetFragment(fragmentManager!!.findFragmentById(R.id.actionsFragment),REQUESTCODE)
                confirmDataFragment.show(fragmentManager,"¿Delivery to client?")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUESTCODE) {
            activity?.startActivityForResult(ReportSignActivity.newIntent(context!!,orderId), SIGNEDREQUESTCODE)

        }
    }


    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult!!.lastLocation
                Toast.makeText(activity, mCurrentLocation!!.bearing.toString(), Toast.LENGTH_LONG).show()
                mCurrentLocation?.let {
                    orderDetailsViewModel.currentLocation.postValue(it)
                    locationViewModel.sendLocationEvent(orderId,it)
                }
            }
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest!!.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest!!.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)

        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(this.mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient!!.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener {
                    Log.i(TAG, "All location settings are satisfied.")
                    mFusedLocationClient!!.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper())
                }
                .addOnFailureListener { e ->
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " + "location settings ")
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
                            } catch (sie: IntentSender.SendIntentException) {
                                Log.i(TAG, "PendingIntent unable to execute request.")
                            }

                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                            Log.e(TAG, errorMessage)
                        }
                    }
                }
    }

    fun onCheckPermisionToLocationTracking(){
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(this)
                .check()
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        startLocationUpdates()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
        AlertDialog.Builder(context!!)
                .setTitle(R.string.storage_permission_rationale_title)
                .setMessage(R.string.storage_permition_rationale_message)
                .setNegativeButton(android.R.string.cancel,
                        { dialog, _ ->
                            dialog.dismiss()
                            token?.cancelPermissionRequest()
                        })
                .setPositiveButton(android.R.string.ok,
                        { dialog, _ ->
                            dialog.dismiss()
                            token?.continuePermissionRequest()
                        })
                .setOnDismissListener({ token?.cancelPermissionRequest() })
                .show()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Snackbar.make(mainContainer!!, R.string.storage_permission_denied_message, Snackbar.LENGTH_LONG).show()
    }



    private fun stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener {
                }
    }


    private val orderStatusbserver = Observer<OrderDetailsModel.Status> {
        it?.let { newStatus ->
            orderDetailsViewModel.order.value?.let {
                it.status = newStatus
                orderDetailsViewModel.order.postValue(it)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }




}