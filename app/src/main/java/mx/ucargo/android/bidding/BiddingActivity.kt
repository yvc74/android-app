package mx.ucargo.android.bidding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import mx.ucargo.android.orderdetails.OrderDetailsActivity

class BiddingActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, BiddingActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(OrderDetailsActivity.newIntent(this, "ANY_ORDER_ID"))
    }
}
