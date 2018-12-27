package mx.ucargo.android.reportedred

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.pama_activity.*
import mx.ucargo.android.R
import mx.ucargo.android.reportsign.ReportSignActivity
import javax.inject.Inject

class PamaActivity : AppCompatActivity() {

    companion object {
        const val ORDER_ID = "ORDER_ID"

        fun newIntent(context: Context, orderId: String): Intent {
            val intent = Intent(context, PamaActivity::class.java)
            intent.putExtra(ORDER_ID, orderId)
            return intent
        }
    }


    @Inject
    lateinit var viewModel: PamaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.pama_activity)



        callSuportButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:0123456789")
            startActivity(intent)
        }

        acceptButton.setOnClickListener{
            finish()
        }

    }

}