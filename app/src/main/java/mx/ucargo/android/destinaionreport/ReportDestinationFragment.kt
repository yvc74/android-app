package mx.ucargo.android.destinaionreport

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.action_event_button.*
import mx.ucargo.android.R
import mx.ucargo.android.begin.BeginViewModel
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.reportlocationtocustom.ConfirmDialogFragment
import mx.ucargo.android.reportsign.ReportSignActivity
import javax.inject.Inject




class ReportDestinationFragment : Fragment() {
    companion object {
        private const val ORDER_ID = "ORDER_ID"
        private const val TYPE_ORDER = "TYPE_ORDER"

        fun newInstance(orderId: String,typeOrder: String): ReportDestinationFragment {
            val arguments = Bundle()
            arguments.putString(ORDER_ID, orderId)
            arguments.putString(TYPE_ORDER,typeOrder)

            val fragment = ReportDestinationFragment()
            fragment.arguments = arguments
            return fragment
        }
    }

    var REQUESTCODE = 0

    lateinit var orderId: String

    lateinit var orderType: String


    @Inject
    lateinit var orderDetailsViewModel: OrderDetailsViewModel

    @Inject
    lateinit var viewModel: DestinationViewModel

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderId = arguments?.getString(ORDER_ID)!!
        orderType = arguments?.getString(TYPE_ORDER)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.action_event_button, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.orderStatus.observe(this, orderStatusbserver)
        viewModel.error.observe(this, errorObserver)

        if (orderType.toInt() == 1){
            actionButton.setText(getString(R.string.begin_route_aduana))
            actionButton.setOnClickListener{
                viewModel.begin(orderId)
            }
        }else if(orderType.toInt() == 0) {
            actionButton.setText(getString(R.string.begin_route_or_store))
            actionButton.setOnClickListener {
                val confirmDataFragment = ActionDialogFragment()
                confirmDataFragment.setTargetFragment(fragmentManager!!.findFragmentById(R.id.actionsFragment),REQUESTCODE)
                confirmDataFragment.show(fragmentManager,"¿Delivery to client?")
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if(resultCode == 1){
                viewModel.orderStatus.postValue(OrderDetailsModel.Status.BEGINROUTE)
                //viewModel.begin(orderId)

            } else if (resultCode == 2){
                viewModel.orderStatus.postValue(OrderDetailsModel.Status.STORED)
                //viewModel.store(orderId)
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

    private val errorObserver = Observer<Throwable> {
        Snackbar.make(view!!, it?.message
                ?: getString(R.string.error_generic), Snackbar.LENGTH_LONG).show()
    }
}