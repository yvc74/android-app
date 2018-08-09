package mx.ucargo.android.orderdetails

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.NavUtils
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.driver_profile_activity.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet_detail_delivery_item.view.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet_detail_instructions_item.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet_detail_instructions_item.view.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet_detail_item.view.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet_detail_quote_item.view.*
import mx.ucargo.android.BuildConfig.APPLICATION_ID
import mx.ucargo.android.R
import mx.ucargo.android.begin.BeginFragment
import mx.ucargo.android.customscheck.CustomsCheckFragment
import mx.ucargo.android.entity.Order
import mx.ucargo.android.entity.Route
import mx.ucargo.android.sendquote.SendQuoteFragment
import mx.ucargo.android.sentquote.SentQuoteFragment
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

private val TAG = OrderDetailsActivity::class.java.simpleName

class OrderDetailsActivity : AppCompatActivity(), OnMapReadyCallback, HasSupportFragmentInjector, PermissionListener {


    companion object {
        const val ORDER_ID = "ORDER_ID"

        const val CAMERA = "CAMERA"
        const val BOTTOM_SHEET = "BOTTOM_SHEET"

        fun newIntent(context: Context, orderId: String): Intent {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra(ORDER_ID, orderId)
            return intent
        }
    }

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    /**
     * Constant used in the location settings dialog.
     */
    private val REQUEST_CHECK_SETTINGS = 0x1

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

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


    private var cameraLatLng: Pair<Double, Double>? = null

    private var googleMap: GoogleMap? = null

    private var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>? = null



    @Inject
    lateinit var viewModel: OrderDetailsViewModel

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.order_details_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        if (savedInstanceState != null) {
            cameraLatLng = savedInstanceState.getSerializable(CAMERA) as Pair<Double, Double>?
            bottomSheetBehavior?.state = savedInstanceState.getInt(BOTTOM_SHEET)
        }



        viewModel.getOrder(intent.getStringExtra(ORDER_ID))
        viewModel.order.observe(this, orderObserver)
        viewModel.routes.observe(this,routesObserver)
        viewModel.error.observe(this, errorObserver)


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)


        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
        onCheckPermisionToLocationTracking()


    }

    private val routesObserver = Observer<List<Route>> {
        it?.let {
            for (route in it){
                googleMap?.let {
                    it.addPolyline(PolylineOptions().addAll(route.points).geodesic(true))
                }
            }
        }
    }

    private val orderObserver = Observer<OrderDetailsModel> {
        it?.let {
            val orderType = if (it.orderType == Order.Type.IMPORT) {
                R.string.order_details_type_import
            } else {
                R.string.order_details_type_export
            }

            val days = if (it.remainingTime.first != 0) {
                resources.getQuantityString(R.plurals.order_details_remaining_time_days, it.remainingTime.first, it.remainingTime.first)
            } else {
                ""
            }
            val hours = if (it.remainingTime.second != 0) {
                resources.getQuantityString(R.plurals.order_details_remaining_time_hours, it.remainingTime.second, it.remainingTime.second)
            } else {
                ""
            }

            originTextView.text = it.originName
            destinationTextView.text = it.destinationName
            orderTypeTextView.text = getString(orderType)



            detailsLayout.removeAllViews()
            for (orderDetailModel in it.details) {
                val detailView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_item, detailsLayout, false)



                detailView.detailLabelTextView.text = getString(resources.getIdentifier(orderDetailModel.label,"string",packageName))
                detailView.detailValueTextView.text = orderDetailModel.value
                detailView.iconImageView.setImageResource(resources.getIdentifier(orderDetailModel.label, "drawable",packageName));
                detailsLayout.addView(detailView)
            }


            detailsDeliveryLayout.removeAllViews()
            for (orderDetailModel in it.deliveryDetails){
                val detailDeliveryView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_delivery_item, detailsDeliveryLayout, false)

                if (it.orderType == Order.Type.EXPORT && orderDetailModel.label == "custom"){
                    detailDeliveryView.detailLabel.text = getString(resources.getIdentifier("customsExport","string",packageName))
                }else{
                    detailDeliveryView.detailLabel.text = getString(resources.getIdentifier(orderDetailModel.label,"string",packageName))
                }
                detailDeliveryView.detailsOriginTextView.text = orderDetailModel.address
                detailDeliveryView.dateCollectTextView.text = orderDetailModel.date
                detailDeliveryView.hourCollectTextView.text = orderDetailModel.hour
                detailsDeliveryLayout.addView(detailDeliveryView)
            }

            quoteDetailsLayout.removeAllViews()
            instructionsDetailsLayout.removeAllViews()
            when(it.status){
                OrderDetailsModel.Status.APPROVED -> {
                    quoteDetailsLayout.visibility = View.VISIBLE;
                    val detailQuoteView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_quote_item,quoteDetailsLayout, false)
                    detailQuoteView.quoteTextView.text =  "\$ ${it.quote}.00  ${getString(R.string.quote_info)}"
                    quoteDetailsLayout.addView(detailQuoteView)

                    instructionsDetailsLayout.visibility = View.VISIBLE
                    val instructionsView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_instructions_item,instructionsDetailsLayout,false)
                    if (it.orderType == Order.Type.IMPORT){
                        instructionsView.instrutionsTextView.text = getString(R.string.order_aproved_import)
                    } else{
                        instructionsView.instrutionsTextView.text = getString(R.string.order_aproved_export)
                    }

                    instructionsView.instrutionButton.setOnClickListener{ viewModel.cancelOrder(intent.getStringExtra(ORDER_ID)) }
                    instructionsDetailsLayout.addView(instructionsView)
                }
                OrderDetailsModel.Status.CUSTOMS -> {

                    val detailQuoteView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_quote_item,quoteDetailsLayout, false)
                    detailQuoteView.quoteTextView.text =  "\$ ${it.quote}.00  ${getString(R.string.quote_info)}"
                    quoteDetailsLayout.addView(detailQuoteView)

                    instructionsDetailsLayout.visibility = View.VISIBLE
                    val instructionsView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_instructions_item,instructionsDetailsLayout,false)
                    instructionsView.instrutionsTextView.text = getString(R.string.instructions_ligths_custom)
                    //onclick listener call service
                    instructionsView.instrutionButton.setTextColor(Color.GRAY)
                    instructionsView.instrutionButton.setText(getString(R.string.call_service))
                    instructionsDetailsLayout.addView(instructionsView)
                }
                OrderDetailsModel.Status.RED -> {
                    quoteDetailsLayout.visibility = View.VISIBLE;
                    val detailQuoteView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_quote_item,quoteDetailsLayout, false)
                    detailQuoteView.quoteTextView.text =  "\$ ${it.quote}.00  ${getString(R.string.quote_info)}"
                    quoteDetailsLayout.addView(detailQuoteView)

                    instructionsDetailsLayout.visibility = View.VISIBLE
                    val instructionsView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_instructions_item,instructionsDetailsLayout,false)
                    //onclick listener call service
                    instructionsView.instrutionButton.setTextColor(Color.GRAY)
                    instructionsView.instrutionButton.setText(getString(R.string.call_service))
                    instructionsDetailsLayout.addView(instructionsView)
                }
                OrderDetailsModel.Status.FINISHED -> {
                    quoteDetailsLayout.visibility = View.VISIBLE;
                    val detailQuoteView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_quote_item,quoteDetailsLayout, false)
                    detailQuoteView.quoteTextView.text =  "\$ ${it.quote}.00  ${getString(R.string.quote_info)}"
                    quoteDetailsLayout.addView(detailQuoteView)

                    instructionsDetailsLayout.visibility = View.VISIBLE
                    val instructionsView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_instructions_item,instructionsDetailsLayout,false)
                    instructionsView.instrutionsTextView.text = getString(R.string.instructions_order_finished)
                    instructionsView.instrutionButton.visibility = View.GONE
                    instructionsDetailsLayout.addView(instructionsView)
                }
                OrderDetailsModel.Status.SENT_QUOTE -> {
                    instructionsDetailsLayout.visibility = View.VISIBLE
                    val instructionsView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_instructions_item,instructionsDetailsLayout,false)
                    instructionsView.instrutionsTextView.text = getString(R.string.instructions_order_quoted)
                    instructionsView.instrutionButton.visibility = View.GONE
                    instructionsDetailsLayout.addView(instructionsView)
                }
            }


            addMarkers(it)

            var fragment = supportFragmentManager.findFragmentById(R.id.actionsFragment)
            if (it.status == OrderDetailsModel.Status.NEW && fragment !is SendQuoteFragment) {
                fragment = SendQuoteFragment.newInstance(it.id)
            } else if (it.status == OrderDetailsModel.Status.SENT_QUOTE && fragment !is SentQuoteFragment) {
                fragment = SentQuoteFragment.newInstance(it.id)
            } else if (it.status == OrderDetailsModel.Status.APPROVED && fragment !is BeginFragment) {
                fragment = BeginFragment.newInstance(it.id)
            } else if (it.status == OrderDetailsModel.Status.ONROUTETOCUSTOM && fragment !is CustomsCheckFragment) {
                fragment = CustomsCheckFragment.newInstance(it.id)
            } else if (it.status == OrderDetailsModel.Status.REPORTEDGREEN){
                // reported lock implemntation
            }

            if (fragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.actionsFragment, fragment).commit()
            }
        }
    }

    private val errorObserver = Observer<Throwable> {
        Log.e(TAG, "errorObserver", it)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        viewModel.order.value?.let { addMarkers(it) }
    }

    private fun addMarkers(order: OrderDetailsModel) {
        googleMap?.let {
            val origin = LatLng(order.originLatLng.first, order.originLatLng.second)
            val destination = LatLng(order.destinationLatLng.first, order.destinationLatLng.second)
            val pickUpModel = LatLng(order.pickUpLatLng.first,order.pickUpLatLng.second)
            if (order.orderType == Order.Type.EXPORT){
                it.addMarker(MarkerOptions().position(pickUpModel).title("Pick Up"))
            }

            it.addMarker(MarkerOptions()
                    .position(origin)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(getString(R.string.order_details_map_origin_marker)))
            it.addMarker(MarkerOptions().position(destination).title(getString(R.string.order_details_map_destination_marker)))

            try {
                if (cameraLatLng != null) {
                    it.moveCamera(CameraUpdateFactory.newLatLng(
                            LatLng(cameraLatLng!!.first,
                                    cameraLatLng!!.second
                            )
                    ))
                } else {

                    if (order.orderType == Order.Type.EXPORT){
                        googleMap!!.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(LatLngBounds.Builder()
                                        .include(origin)
                                        .include(destination)
                                        .include(pickUpModel)
                                        .build(), 100)
                        )
                    }else {
                        it.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(LatLngBounds.Builder()
                                        .include(origin)
                                        .include(destination)
                                        .build(), 100)
                        )
                    }


                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    fun onCheckPermisionToLocationTracking(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(this)
                .check()
    }


    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        startLocationUpdates()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
        AlertDialog.Builder(this@OrderDetailsActivity)
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


    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult!!.lastLocation
                viewModel.order.value?.let { trackingMap(it) }
            }
        }
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(this.mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
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


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient!!.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this) {
                    Log.i(TAG, "All location settings are satisfied.")
                    mFusedLocationClient!!.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper())
                }
                .addOnFailureListener(this) { e ->
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " + "location settings ")
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(this@OrderDetailsActivity, REQUEST_CHECK_SETTINGS)
                            } catch (sie: IntentSender.SendIntentException) {
                                Log.i(TAG, "PendingIntent unable to execute request.")
                            }

                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                            Log.e(TAG, errorMessage)
                            Toast.makeText(this@OrderDetailsActivity, errorMessage, Toast.LENGTH_LONG).show()

                        }
                    }
                }
    }


    private fun stopLocationUpdates() {

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this) {
                }
    }

    private fun trackingMap(order: OrderDetailsModel){
        when(order.status){
            OrderDetailsModel.Status.ONROUTETOCUSTOM -> {
                googleMap?.let {
                    val destination = LatLng(order.destinationLatLng.first, order.destinationLatLng.second)
                    val currentLocation = LatLng(mCurrentLocation!!.latitude,mCurrentLocation!!.longitude)
                    it.clear()
                    it.addMarker(MarkerOptions().position(destination).title(getString(R.string.order_details_map_destination_marker)))
                    it.addMarker(MarkerOptions().position(currentLocation).title(getString(R.string.order_details_map_current_location_marker)))
                }
            }
            else -> {
                stopLocationUpdates()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        googleMap?.let {
            val camera = it.cameraPosition.target
            outState?.putSerializable(CAMERA, Pair(camera.latitude, camera.longitude))
        }

        bottomSheetBehavior?.let {
            outState?.putInt(BOTTOM_SHEET, it.state)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

}
