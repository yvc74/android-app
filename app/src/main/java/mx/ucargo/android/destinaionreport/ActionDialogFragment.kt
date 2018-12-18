package mx.ucargo.android.destinaionreport

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import kotlinx.android.synthetic.main.confirm_route_or_store_alert_fragment.view.*
import mx.ucargo.android.R

class ActionDialogFragment: DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        super.onCreateDialog(savedInstanceState)

        val view = activity!!.layoutInflater.inflate(R.layout.confirm_route_or_store_alert_fragment, null)

        view.startRouteButton.setOnClickListener{
            sendResult(1)
            dismiss()
        }

        view.storeButton.setOnClickListener{
            sendResult(2)
            dismiss()
        }

        val aleertDialogBuilder = AlertDialog.Builder(activity)
                .setIcon(android.R.drawable.stat_notify_error)
                .setView(view)
                .setNegativeButton("No",DialogInterface.OnClickListener { dialog, which ->
                    dismiss()
                })
                .create()
        return aleertDialogBuilder
    }


    private fun sendResult(resultCode: Int) {
        if (targetFragment == null) {
            return
        }
        val intent = Intent()
        targetFragment!!.onActivityResult(targetRequestCode, resultCode, intent)
    }
}
