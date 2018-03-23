package mx.ucargo.android.orderlist

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.LinearLayout
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.order_list_activity.*
import mx.ucargo.android.R
import mx.ucargo.android.orderdetails.OrderDetailsActivity
import javax.inject.Inject

class OrderListActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, OrderListActivity::class.java)
        }
    }

    @Inject
    lateinit var orderListViewModel: OrderListViewModel

    lateinit var orderListAdapter: OrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.order_list_activity)

        orderListAdapter = OrderListAdapter()
        orderListAdapter.onItemSelected = {
            startActivity(OrderDetailsActivity.newIntent(this, it.id))
        }

        orderlistRecyclerView.setHasFixedSize(true)
        orderlistRecyclerView.isDrawingCacheEnabled = true;
        orderlistRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        orderlistRecyclerView.adapter = orderListAdapter

        orderListViewModel.orderList.observe(this, Observer { orders ->
            orders?.let {
                orderListAdapter.replaceOrderList(it)
            }
        })

        orderListViewModel.loading.observe(this, Observer {
            orderListSwipeRefreshLayout.isRefreshing = it ?: false
        })

        orderListSwipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            orderListViewModel.getOrderList()
        })


    }

}


