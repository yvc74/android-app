package mx.ucargo.android.bidding

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity

class BiddingActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, BiddingActivity::class.java)
        }
    }
}
