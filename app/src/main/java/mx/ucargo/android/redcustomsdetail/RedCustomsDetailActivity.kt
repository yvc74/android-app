package mx.ucargo.android.redcustomsdetail

import android.Manifest
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.red_custom_detail_activity.*
import kotlinx.android.synthetic.main.driver_profile_activity.*
import mx.ucargo.android.R
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import javax.inject.Inject
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.app.Activity


class RedCustomsDetailActivity : AppCompatActivity(), PermissionListener {

    companion object {
        const val ORDER_ID = "ORDER_ID"
        fun newIntent(context: Context, orderId: String): Intent {
            val intent = Intent(context, RedCustomsDetailActivity::class.java)
            intent.putExtra(ORDER_ID, orderId)
            return intent

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.red_custom_detail_activity)

        withoutincidentscheckbox.setOnCheckedChangeListener(showLockButtonPicture)
        withincidentscheckbox.setOnCheckedChangeListener(showEditTextInicidents)
        pamaincidentscheckbox.setOnCheckedChangeListener(pamaOncheckBox)
        lockbuttonpicture.setOnClickListener(selectpicture)
        lockbuttonpictureincident.setOnClickListener(selectpictureincident)
        continuebutton.setOnClickListener(senddetails)
    }

    private val selectpicture : (View) -> Unit = {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(this)
                .check()
        lockbuttonpicture.setText(getText(R.string.change_lock_photo))
    }

    private val selectpictureincident : (View) -> Unit = {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(this)
                .check()
        lockbuttonpicture.setText(getText(R.string.change_lock_photo))
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        launchCamera()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
        AlertDialog.Builder(this@RedCustomsDetailActivity)
                .setTitle(R.string.storage_permission_rationale_title)
                .setMessage(R.string.storage_permition_rationale_message)
                .setNegativeButton(android.R.string.cancel,
                        { dialog, _ ->
                            dialog.dismiss()
                            token?.cancelPermissionRequest()
                        })
                .setPositiveButton(android.R.string.ok,
                        { dialog, _ ->
                            dialog.dismiss()
                            token?.continuePermissionRequest()
                        })
                .setOnDismissListener({ token?.cancelPermissionRequest() })
                .show()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Snackbar.make(mainContainer!!, R.string.storage_permission_denied_message, Snackbar.LENGTH_LONG).show()
    }

    private fun launchCamera() {
        ImagePicker.create(this)
                .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                .single() // single mode
                .showCamera(true) // show camera or not (true by default)
                .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        val image = ImagePicker.getFirstImageOrNull(data)


    }


    private val showLockButtonPicture = CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton, stateCheckBox: Boolean ->

        if (compoundButton.isChecked) {
            withincidentscheckbox.isChecked = false;
            pamaincidentscheckbox.isChecked = false;
            lockbuttonpicture.visibility = View.VISIBLE;
            incidentEdittext.visibility = View.GONE;
        }
        else{
            lockbuttonpicture.visibility = View.GONE;
        }
    }


    private val showEditTextInicidents = CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton, stateCheckBox: Boolean ->

        if (compoundButton.isChecked) {
            withoutincidentscheckbox.isChecked = false;
            pamaincidentscheckbox.isChecked = false;
            lockbuttonpicture.visibility = View.GONE;
            incidentEdittext.visibility = View.VISIBLE;
        }
        else{
            incidentEdittext.visibility = View.GONE;
        }
    }



    private val pamaOncheckBox = CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton, stateCheckBox: Boolean ->

        if (compoundButton.isChecked) {
            withoutincidentscheckbox.isChecked = false;
            withincidentscheckbox.isChecked = false;
            lockbuttonpicture.visibility = View.GONE;
            incidentEdittext.visibility = View.GONE;
        }
    }


    private val senddetails : (View) -> Unit = {
       if(withoutincidentscheckbox.isChecked){
            finish()
       }
       else if(withincidentscheckbox.isChecked){
           finish()
       }
       else if (pamaincidentscheckbox.isChecked){
           val output = Intent()
           output.putExtra("PAMA", 3)
           setResult(Activity.RESULT_OK, output)
           finish()
           finish()
       }
    }




}
