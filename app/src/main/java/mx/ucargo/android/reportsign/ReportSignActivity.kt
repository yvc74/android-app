package mx.ucargo.android.reportsign

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import kotlinx.android.synthetic.main.sign_view.*
import mx.ucargo.android.R

class ReportSignActivity : Activity() {

    companion object {
        const val ORDER_ID = "ORDER_ID"

        fun newIntent(context: Context): Intent {
            val intent = Intent(context,ReportSignActivity::class.java)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.sign_view)


        sendButton.setOnClickListener(View.OnClickListener {
            drawPadView1?.let {
                it.clearScreen()
            }
        })
    }

}