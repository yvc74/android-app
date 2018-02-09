package mx.ucargo.android.orderdetails

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.order_details_bottom_sheet.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet_detail_item.view.*
import mx.ucargo.android.R
import mx.ucargo.android.entity.Order
import javax.inject.Inject


class OrderDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        val CAMERA = "CAMERA"
        val ORDER_ID = "ORDER_ID"

        fun newIntent(context: Context, orderId: String): Intent {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra(ORDER_ID, orderId)
            return intent
        }
    }

    var cameraLatLng: Pair<Double, Double>? = null

    @Inject
    lateinit var viewModel: OrderDetailsViewModel

    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.order_details_activity)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (savedInstanceState != null) {
            cameraLatLng = savedInstanceState.getSerializable(CAMERA) as Pair<Double, Double>?
        }

        viewModel.getOrder(intent.getStringExtra(ORDER_ID))

        viewModel.order.observe(this, orderObserver)
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
            remainingTimeTextView.text = "$days $hours".trim()
            pickUpAddressTextView.text = it.pickUpAddress
            deliverAddressTextView.text = it.deliverAddress

            for (orderDetailModel in it.details) {
                val detailView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_item, detailsLayout, false)

                Glide.with(this).load(orderDetailModel.icon).into(detailView.iconImageView)
                detailView.detailNameTextView.text = getString(orderDetailModel.name)
                detailView.detailValueTextView.text = orderDetailModel.value

                detailsLayout.addView(detailView)
            }

            addMarkers(it)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        viewModel.order.value?.let { addMarkers(it) }
    }

    private fun addMarkers(order: OrderDetailsModel) {
        googleMap?.let {
            val origin = LatLng(order.originLatLng.first, order.originLatLng.second)
            val destination = LatLng(order.destinationLatLng.first, order.destinationLatLng.second)

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
                    it.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(LatLngBounds.Builder()
                                    .include(origin)
                                    .include(destination)
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
    }
}
