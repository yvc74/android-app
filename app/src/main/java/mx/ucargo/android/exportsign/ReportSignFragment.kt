package mx.ucargo.android.exportsign

import android.app.Activity
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
import kotlinx.android.synthetic.main.begin_fragment.*
import mx.ucargo.android.R
import mx.ucargo.android.begin.BeginFragment
import mx.ucargo.android.begin.BeginViewModel
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.reportlocationtocustom.ConfirmDialogFragment
import mx.ucargo.android.reportsign.ReportSignActivity
import javax.inject.Inject


class ReportSignFragment : Fragment() {
    companion object {
        private const val ORDER_ID = "ORDER_ID"

        fun newInstance(orderId: String): ReportSignFragment {
            val arguments = Bundle()
            arguments.putString(ORDER_ID, orderId)

            val fragment = ReportSignFragment()
            fragment.arguments = arguments
            return fragment
        }
    }

    var REQUESTCODE = 0
    val SIGNEDREQUESTCODE = 111

    lateinit var orderId: String


    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderId = arguments?.getString(ORDER_ID)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.action_event_button, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        actionButton.setText(R.string.order_detail_arrive_delivery_destination)
        actionButton.setOnClickListener {
            val confirmDataFragment = ConfirmDialogFragment()
            confirmDataFragment.setTargetFragment(fragmentManager!!.findFragmentById(R.id.actionsFragment),REQUESTCODE)
            confirmDataFragment.show(fragmentManager,"¿Delivery to client?")
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

}
