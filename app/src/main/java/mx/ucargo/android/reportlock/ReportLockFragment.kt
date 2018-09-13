package mx.ucargo.android.reportlock

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.begin_fragment.*
import kotlinx.android.synthetic.main.driver_profile_activity.*
import mx.ucargo.android.R
import mx.ucargo.android.editprofile.S3Image
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import java.io.File
import javax.inject.Inject


class ReportLockFragment : Fragment(), PermissionListener {
    companion object {
        private const val ORDER_ID = "ORDER_ID"

        fun newInstance(orderId: String): ReportLockFragment {
            val arguments = Bundle()
            arguments.putString(ORDER_ID, orderId)

            val fragment = ReportLockFragment()
            fragment.arguments = arguments
            return fragment
        }
    }

    @Inject
    lateinit var transferUtility: TransferUtility

    lateinit var orderId: String

    @Inject
    lateinit var orderDetailsViewModel: OrderDetailsViewModel

    @Inject
    lateinit var viewModel: ReportLockViewModel

    lateinit private var imageKey: String

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderId = arguments?.getString(ORDER_ID)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.begin_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.orderStatus.observe(this, orderStatusbserver)
        viewModel.error.observe(this, errorObserver)
        viewModel.flagToSendEvent.observe(this,onSendEvent)
        viewModel.s3Image.observe(this, s3ImageObserver)
        beginButton.setOnClickListener(changeImageProfileButtonListener)
    }

    private val changeImageProfileButtonListener: (View) -> Unit = {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(this)
                .check()
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        launchCamera()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
        AlertDialog.Builder(context!!)
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
        viewModel.imageSelected(image.path)
    }

    private val s3ImageObserver = Observer<S3Image> {
        it?.let {
            imageKey = it.key
            val uploadObserver = transferUtility.upload(it.bucket, it.key, File(it.path))
            uploadObserver.setTransferListener(viewModel.transferListener)
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

    private val onSendEvent  = Observer<Boolean> {
        it?.let {
            if (it){
                viewModel.reportLock(orderId,imageKey)
            }
        }
    }

    private val errorObserver = Observer<Throwable> {
        Snackbar.make(view!!, it?.message
                ?: getString(R.string.error_generic), Snackbar.LENGTH_LONG).show()
    }
}
