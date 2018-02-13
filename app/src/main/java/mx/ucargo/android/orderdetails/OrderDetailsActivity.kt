package mx.ucargo.android.orderdetails

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
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
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.order_details_bottom_sheet.*
import kotlinx.android.synthetic.main.order_details_bottom_sheet_detail_item.view.*
import mx.ucargo.android.R
import mx.ucargo.android.entity.Order
import mx.ucargo.android.sendquote.SendQuoteFragment
import mx.ucargo.android.sentquote.SentQuoteFragment
import javax.inject.Inject


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

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        if (savedInstanceState != null) {
            cameraLatLng = savedInstanceState.getSerializable(CAMERA) as Pair<Double, Double>?
            bottomSheetBehavior?.state = savedInstanceState.getInt(BOTTOM_SHEET)
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

            detailsLayout.removeAllViews()
            for (orderDetailModel in it.details) {
                val detailView = layoutInflater.inflate(R.layout.order_details_bottom_sheet_detail_item, detailsLayout, false)

                Glide.with(this).load(orderDetailModel.icon).into(detailView.iconImageView)
                detailView.detailLabelTextView.text = orderDetailModel.label
                detailView.detailValueTextView.text = orderDetailModel.value

                detailsLayout.addView(detailView)
            }

            addMarkers(it)

            var fragment = supportFragmentManager.findFragmentById(R.id.actionsFragment)
            if (it.status == OrderDetailsModel.Status.NEW && fragment !is SendQuoteFragment) {
                fragment = SendQuoteFragment.newInstance(it.id)
            } else if (it.status == OrderDetailsModel.Status.SENT_QUOTE && fragment !is SentQuoteFragment) {
                fragment = SentQuoteFragment.newInstance(it.id)
            }

            if (fragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.actionsFragment, fragment).commit()
            }
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

        bottomSheetBehavior?.let {
            outState?.putInt(BOTTOM_SHEET, it.state)
        }
    }
}
