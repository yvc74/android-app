package mx.ucargo.android.orderdetails

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.app.NavUtils
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.order_details_bottom_sheet.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet_detail_delivery_item.view.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet_detail_instructions_item.view.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet_detail_item.view.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet_detail_quote_item.view.*
import kotlinx.android.synthetic.main.order_empty.*
import mx.ucargo.android.R
import mx.ucargo.android.reportlock.ReportLockFragment
import mx.ucargo.android.begin.BeginFragment
import mx.ucargo.android.cargocheck.CargoCheckFragment
import mx.ucargo.android.collectcharge.CollectCheckFragment
import mx.ucargo.android.customscheck.CustomsCheckFragment
import mx.ucargo.android.destinaionreport.ReportDestinationFragment
import mx.ucargo.android.entity.Order
import mx.ucargo.android.entity.Route
import mx.ucargo.android.exportsign.ReportSignFragment
import mx.ucargo.android.redcustomsdetail.RedCustomsDetailActivity
import mx.ucargo.android.reportedred.PamaActivity
import mx.ucargo.android.reportlocationtocustom.ReportLocationFragment
import mx.ucargo.android.sendquote.SendQuoteFragment
import mx.ucargo.android.sentquote.SentQuoteFragment
import javax.inject.Inject

private val TAG = OrderDetailsActivity::class.java.simpleName

class OrderDetailsActivity : AppCompatActivity(), OnMapReadyCallback, HasSupportFragmentInjector {


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


    private val CARGOCHECKREQUESTCODE = 109

    private val REDDEATILSREQUESTCODE = 110

    private  var nextstatus:Int = 0

    private val SIGNEDREQUESTCODE = 111

    private val PAMAREQUESTCODE = 112

    private var cameraLatLng: Pair<Double, Double>? = null

    private var googleMap: GoogleMap? = null

    private var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>? = null

    private var polyline: Polyline? = null

    private var stateView: Boolean = false


    @Inject
    lateinit var viewModel: OrderDetailsViewModel

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        viewModel.getOrder("400")

        setContentView(R.layout.order_empty)
        setTitle("Sin Viaje Asignado")



        //supportActionBar?.setDisplayHomeAsUpEnabled(true)


        reloadbutton.setOnClickListener(reloadActionLister)





        viewModel.order.observe(this, orderObserver)
        viewModel.routes.observe(this,routesObserver)
        viewModel.error.observe(this, errorObserver)
        viewModel.currentLocation.observe(this,locationObserver)

    }

    private val reloadActionLister = View.OnClickListener { v ->
        viewModel.getOrder(intent.getStringExtra(ORDER_ID))
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGNEDREQUESTCODE || requestCode == PAMAREQUESTCODE) {
            Log.d("com.ucargo.result", "Should finish" + requestCode.toString())
            finish()
        } else if(requestCode == CARGOCHECKREQUESTCODE ){
            nextstatus = 1
            viewModel.getOrder("13")
        } else if(requestCode == REDDEATILSREQUESTCODE ){
            if (resultCode == Activity.RESULT_OK && data!= null) {
                nextstatus = data.getIntExtra("PAMA",3)
            }else{
                nextstatus = 2
            }
            viewModel.getOrder("13")
        } else {
            Log.d("com.ucargo.result", "Not finish" + requestCode.toString())
        }
    }

    private val routesObserver = Observer<List<Route>> {
        it?.let {
            for (route in it){
                googleMap?.let {
                    if (polyline != null){
                        polyline?.let { it.remove() }
                        polyline = it.addPolyline(PolylineOptions().addAll(route.points).geodesic(true))
                    }
                    else{
                        polyline = it.addPolyline(PolylineOptions().addAll(route.points).geodesic(true))
                    }
                }
            }
        }
    }

    private val locationObserver = Observer<Location> {
        it?.let {
            viewModel.order.value?.let {
                when(it.status){
                    OrderDetailsModel.Status.ONROUTE,OrderDetailsModel.Status.ONTRACKING -> {
                            val destination = LatLng(it.destinationLatLng.first, it.destinationLatLng.second)

                            val currentLocation = LatLng(viewModel.currentLocation.value!!.latitude,viewModel.currentLocation.value!!.longitude)
                        googleMap?.let {
                            it.clear()
                            it.addMarker(MarkerOptions().position(destination).title(getString(R.string.order_details_map_destination_marker)))
                            it.addMarker(MarkerOptions().position(currentLocation)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car))
                                    .title(getString(R.string.order_details_map_current_location_marker)))
                            moveCamera(currentLocation,destination)
                        }

                        viewModel.getRoute(viewModel.currentLocation.value!!,it.destinationLatLng)


                    }
                    else -> {
                    }
                }
            }
        }
    }

    private val orderObserver = Observer<OrderDetailsModel> {
        it?.let {

            if (!stateView){
                setContentView(R.layout.order_details_activity)
                setTitle("Viaje Asignado")
                val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
                mapFragment.getMapAsync(this)
                stateView = true
            }

            when(nextstatus){
                1 -> {it.status = OrderDetailsModel.Status.CUSTOMS
                    nextstatus = 0
                }
                2 -> {it.status = OrderDetailsModel.Status.REPORTEDLOCK
                    nextstatus = 0
                }
                3->{it.status = OrderDetailsModel.Status.REPORTEDPAMA
                    nextstatus = 0
                }
            }

            val orderType: Int
            val orderTypeValue: Int
            if (it.orderType == Order.Type.IMPORT) {
                orderType =  R.string.order_details_type_import
                orderTypeValue = 0
            } else {
                orderType = R.string.order_details_type_export
                orderTypeValue = 1
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


            if((it.status == OrderDetailsModel.Status.REPORTEDGREEN || it.status == OrderDetailsModel.Status.REPORTEDRED) && it.orderType == Order.Type.EXPORT) {
                orderStatusTextView.text = getString(R.string.REPORTSIGN)
            }
            else{
                orderStatusTextView.text = getString(resources.getIdentifier(it.status.toString(),"string",packageName))
            }


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
                OrderDetailsModel.Status.NEW->{
                    instructionsDetailsLayout.visibility = View.VISIBLE
                    val instructionsView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_instructions_item,instructionsDetailsLayout,false)
                    instructionsView.instrutionsTextView.text = getString(R.string.instructions_order_quote)

                    instructionsDetailsLayout.addView(instructionsView)
                }
                OrderDetailsModel.Status.APPROVED -> {

                    instructionsDetailsLayout.visibility = View.VISIBLE
                    val instructionsView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_instructions_item,instructionsDetailsLayout,false)
                    if (it.orderType == Order.Type.IMPORT){
                        instructionsView.instrutionsTextView.text = getString(R.string.order_aproved_import)
                    } else{
                        instructionsView.instrutionsTextView.text = getString(R.string.order_aproved_export)
                    }


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

                    instructionsDetailsLayout.addView(instructionsView)
                }
                OrderDetailsModel.Status.FINISHED -> {
                    quoteDetailsLayout.visibility = View.VISIBLE;
                    val detailQuoteView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_quote_item,quoteDetailsLayout, false)
                    detailQuoteView.quoteTextView.text =  "\$ ${it.quote}.00  ${getString(R.string.quote_info)}"
                    quoteDetailsLayout.addView(detailQuoteView)

                    instructionsDetailsLayout.visibility = View.VISIBLE
                    val instructionsView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_instructions_item,instructionsDetailsLayout,false)

                    instructionsDetailsLayout.addView(instructionsView)
                }
                OrderDetailsModel.Status.SENT_QUOTE -> {
                    instructionsDetailsLayout.visibility = View.VISIBLE
                    val instructionsView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_instructions_item,instructionsDetailsLayout,false)
                    instructionsView.instrutionsTextView.text = getString(R.string.instructions_order_quoted)

                    instructionsDetailsLayout.addView(instructionsView)
                }
            }


            addMarkers(it)


            var fragment = supportFragmentManager.findFragmentById(R.id.actionsFragment)
            /*if (it.status == OrderDetailsModel.Status.NEW && fragment !is SendQuoteFragment) {
                fragment = SendQuoteFragment.newInstance(it.id)
            } else if (it.status == OrderDetailsModel.Status.SENT_QUOTE && fragment !is SentQuoteFragment) {
                fragment = SentQuoteFragment.newInstance(it.id)
            }*/
            if (it.status == OrderDetailsModel.Status.APPROVED && it.orderType == Order.Type.IMPORT && fragment !is BeginFragment) {
                fragment = BeginFragment.newInstance(it.id)
                //Begin Import
            }  else if (it.status == OrderDetailsModel.Status.APPROVED && it.orderType == Order.Type.EXPORT && fragment !is BeginFragment) {
                fragment = CollectCheckFragment.newInstance(it.id)
                //Begin Export
            } else if (it.status == OrderDetailsModel.Status.ONROUTETOCUSTOM  && fragment !is CargoCheckFragment && it.orderType == Order.Type.IMPORT) {
                fragment = CargoCheckFragment.newInstance(it.id)
            } else if (it.status == OrderDetailsModel.Status.CUSTOMS  && fragment !is CustomsCheckFragment && it.orderType == Order.Type.IMPORT) {
                fragment = CustomsCheckFragment.newInstance(it.id)
            } else if (it.status == OrderDetailsModel.Status.ONROUTETOCUSTOM  && fragment !is CustomsCheckFragment && it.orderType == Order.Type.EXPORT) {
                fragment = ReportLocationFragment.newInstance(it.id,orderTypeValue.toString())
            } else if ((it.status == OrderDetailsModel.Status.REPORTEDGREEN && it.orderType == Order.Type.IMPORT)  || it.status == OrderDetailsModel.Status.COLLECTED){
                fragment = ReportLockFragment.newInstance(it.id)
            }else if((it.status == OrderDetailsModel.Status.REPORTEDGREEN || it.status == OrderDetailsModel.Status.REPORTEDRED) && it.orderType == Order.Type.EXPORT){
                fragment = ReportSignFragment.newInstance(it.id)
            } else if(it.status == OrderDetailsModel.Status.REPORTCUSTOMEXPORT) {
                fragment = CustomsCheckFragment.newInstance(it.id)
            } else if(it.status == OrderDetailsModel.Status.REPORTEDLOCK){
                fragment = ReportDestinationFragment.newInstance(it.id,orderTypeValue.toString())
            } else if (it.status == OrderDetailsModel.Status.REPORTEDRED){
                startActivityForResult(RedCustomsDetailActivity.newIntent(this,it.id), REDDEATILSREQUESTCODE)
                //startActivityForResult(RedCustomsDetailActivity.newIntent(this,it.id), PAMAREQUESTCODE)
            } else if (it.status == OrderDetailsModel.Status.REPORTEDPAMA){
                startActivityForResult(PamaActivity.newIntent(this,it.id), PAMAREQUESTCODE)
            } else if (it.status == OrderDetailsModel.Status.STORED){
                fragment = ReportDestinationFragment.newInstance(it.id,orderTypeValue.toString())
            } else if (it.status == OrderDetailsModel.Status.ONROUTE){
                fragment = ReportLocationFragment.newInstance(it.id,orderTypeValue.toString())
            } else if(it.status == OrderDetailsModel.Status.ONTRACKING){
                fragment = ReportLocationFragment.newInstance(it.id,orderTypeValue.toString())
            } else if (it.status == OrderDetailsModel.Status.FINISHED){
                actionsFragment.visibility = View.GONE
            } //report sign
            else if(it.status == OrderDetailsModel.Status.REPORTSIGN){

            }
                //lauch activity to signed
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


    fun moveCamera(currentlocation: LatLng,destination: LatLng) {
        googleMap?.let {
            try {
                if (cameraLatLng != null) {
                    it.moveCamera(CameraUpdateFactory.newLatLng(
                            LatLng(cameraLatLng!!.first,
                                    cameraLatLng!!.second
                            )
                    ))
                } else {
                        googleMap!!.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(LatLngBounds.Builder()
                                        .include(currentlocation)
                                        .build(), 100)
                        )
                }
            } catch (e: Exception) {
                // Ignore
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
    }

}
