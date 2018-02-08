package mx.ucargo.android.orderdetails

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.order_details_bottom_sheet.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet_detail_item.view.*
import mx.ucargo.android.R
import javax.inject.Inject


class OrderDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        val CAMERA = "CAMERA"

        fun newIntent(context: Context) = Intent(context, OrderDetailsActivity::class.java)
    }

    var cameraLatLng: Pair<Double, Double>? = null

    @Inject
    lateinit var viewModel: OrderDetailsViewModel

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.order_details_activity)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (savedInstanceState != null) {
            cameraLatLng = savedInstanceState.getSerializable(CAMERA) as Pair<Double, Double>?
        }

        viewModel.order.observe(this, orderObserver)
    }

    private val orderObserver = Observer<OrderDetailsModel> {
        it?.let {
            originTextView.text = it.originName
            destinationTextView.text = it.destinationName
            orderTypeTextView.text = it.orderType
            remainingTimeTextView.text = it.remainingTime
            pickUpAddressTextView.text = it.pickUpAddress
            deliverAddressTextView.text = it.deliverAddress

            for (orderDetailModel in it.details) {
                val detailView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_item, detailsLayout, false)
                detailView.detailNameTextView.text = orderDetailModel.name
                detailView.detailValueTextView.text = orderDetailModel.value
                detailsLayout.addView(detailView)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        viewModel.order.value?.let {
            val origin = LatLng(it.originLatLng.first, it.originLatLng.second)
            val destination = LatLng(it.destinationLatLng.first, it.destinationLatLng.second)

            googleMap.addMarker(MarkerOptions().position(origin).title(getString(R.string.order_details_map_origin_marker)))
            googleMap.addMarker(MarkerOptions().position(destination).title(getString(R.string.order_details_map_destination_marker)))

            if (cameraLatLng != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                        LatLng(cameraLatLng!!.first,
                                cameraLatLng!!.second
                        )
                ))
            } else {
                googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(LatLngBounds.Builder()
                                .include(origin)
                                .include(destination)
                                .build(), 100)
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        val camera = googleMap.cameraPosition.target
        outState?.putSerializable(CAMERA, Pair(camera.latitude, camera.longitude))
    }
}
