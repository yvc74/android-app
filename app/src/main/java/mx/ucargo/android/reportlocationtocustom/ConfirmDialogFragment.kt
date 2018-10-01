package mx.ucargo.android.reportlocationtocustom

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment

class ConfirmDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val aleertDialogBuilder = AlertDialog.Builder(activity)
                .setIcon(android.R.drawable.stat_notify_error)
                // set Dialog Title
                // Set Dialog Message
                .setMessage("¿ESTAS SEGURO?")
                // positive button
                .setPositiveButton("Si", DialogInterface.OnClickListener{ dialog, which ->
                    sendResult(Activity.RESULT_OK)
                })
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