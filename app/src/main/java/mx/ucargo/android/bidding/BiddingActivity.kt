package mx.ucargo.android.bidding

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.bidding_activity.*
import mx.ucargo.android.R
import javax.inject.Inject

class BiddingActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, BiddingActivity::class.java)
        }
    }

    @Inject
    lateinit var biddingViewModel: BiddingViewModel

    @Inject
    lateinit var biddingAdapter: BiddingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.bidding_activity)

        biddingRecyclerView.setHasFixedSize(true)
        biddingRecyclerView.isDrawingCacheEnabled = true;
        biddingRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        biddingRecyclerView.adapter = biddingAdapter

        biddingViewModel.biddings.observe(this, Observer { biddings ->
            biddingAdapter.replaceBiddings(biddings!!)
        })

    }
}
