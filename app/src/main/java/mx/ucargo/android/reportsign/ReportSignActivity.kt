package mx.ucargo.android.reportsign

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.sign_view.*
import mx.ucargo.android.R
import mx.ucargo.android.editprofile.S3Image
import mx.ucargo.android.orderdetails.OrderDetailsActivity
import java.io.File
import java.io.StringReader
import java.util.*
import javax.inject.Inject

class ReportSignActivity : AppCompatActivity() {

    companion object {
        const val ORDER_ID = "ORDER_ID"

        fun newIntent(context: Context, orderId: String): Intent {
            val intent = Intent(context, ReportSignActivity::class.java)
            intent.putExtra(ORDER_ID, orderId)
            return intent
        }
    }

    @Inject
    lateinit var transferUtility: TransferUtility

    @Inject
    lateinit var viewModel: ReportSignViewModel

    private val path = Environment.getExternalStorageDirectory().toString() + File.separator + "ucargo/"

    var imageKey: String = ""

    

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.sign_view)


        viewModel.s3Image.observe(this,s3ImageObserver)
        viewModel.uploadProgress.observe(this,progesionReport)
        viewModel.flagToSendEvent.observe(this,onSendEvent)

        sendButton.setOnClickListener(View.OnClickListener {
            imageKey = "sign_${UUID.randomUUID()}.jpg"
            if(SaveViewUtil.saveScreen(drawPadView1,imageKey)){

                viewModel.imageSelected(path+imageKey,imageKey)
            }else{
                Toast.makeText(this, "Save drawing fail. Please check your SD card", Toast.LENGTH_SHORT).show();
            }
        })
    }

    private val s3ImageObserver = Observer<S3Image> {
        it?.let {
            Log.d("AM3PATH","${it.bucket}/${it.key}")
            val uploadObserver = transferUtility.upload(it.bucket, it.key, File(it.path))
            uploadObserver.setTransferListener(viewModel.transferListener)
        }
    }

    private val progesionReport = Observer<Int> {
        it?.let {
            Toast.makeText(this, "Subiendo Imagen ${it}%", Toast.LENGTH_SHORT).show();
        }
    }

    private val onSendEvent  = Observer<Boolean> {
        it?.let {
            if (it){
                viewModel.s3Image.value?.let {
                    if (SaveViewUtil.deleteSign(it.path)){
                        viewModel.beginSign(intent.getStringExtra(ORDER_ID),it.key)
                    }

                }
            }
        }
    }

}