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
import com.google.android.gms.maps.model.MarkerOptions
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.order_details_bottom_sheet.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet_detail_item.view.*
import mx.ucargo.android.R
import javax.inject.Inject

class OrderDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        fun newIntent(context: Context) = Intent(context, OrderDetailsActivity::class.java)
    }

    @Inject
    lateinit var viewModel: OrderDetailsViewModel

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.order_details_activity)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.order.observe(this, Observer {
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
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        this.googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
