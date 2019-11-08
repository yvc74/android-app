package mx.ucargo.android.reportlocationtocustom

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import kotlinx.android.synthetic.main.confirm_dialog.*
import kotlinx.android.synthetic.main.confirm_dialog.view.*
import mx.ucargo.android.R

class ConfirmDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)


        val view = activity!!.layoutInflater.inflate(R.layout.confirm_dialog, null)


        if (tag == "Reportar Estado de la mercancia"){
            view.labelTextView.text = "Estas de acuerdo"
        }

        view.acceptButton.setOnClickListener{
            sendResult(Activity.RESULT_OK)
            dismiss()
        }

        view.cancelButton.setOnClickListener{
            dismiss()
        }

        val aleertDialogBuilder = AlertDialog.Builder(activity)
                .setIcon(android.R.drawable.stat_notify_error)
                .setView(view)
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