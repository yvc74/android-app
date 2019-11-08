package mx.ucargo.android.cargocheck

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.action_event_button.*
import mx.ucargo.android.R
import mx.ucargo.android.reportlocationtocustom.ConfirmDialogFragment

class CargoCheckFragment : Fragment() {
    companion object {
        private const val ORDER_ID = "ORDER_ID"

        fun newInstance(orderId: String): CargoCheckFragment {
            val arguments = Bundle()
            arguments.putString(ORDER_ID, orderId)

            val fragment = CargoCheckFragment()
            fragment.arguments = arguments
            return fragment
        }
    }

    var REQUESTCODE = 0
    val CARGOCHECKREQUESTCODE = 109

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

        actionButton.setText("Reportar Estado de la mercancia")
        actionButton.setOnClickListener {
            val confirmDataFragment = ConfirmDialogFragment()
            confirmDataFragment.setTargetFragment(fragmentManager!!.findFragmentById(R.id.actionsFragment),REQUESTCODE)
            confirmDataFragment.show(fragmentManager,"Reportar Estado de la mercancia")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUESTCODE) {
            activity?.startActivityForResult(CargoCheckActivity.newIntent(context!!,orderId), CARGOCHECKREQUESTCODE)
        }
    }

}
