package mx.ucargo.android.orderlist

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.order_list_activity.*
import mx.ucargo.android.R
import mx.ucargo.android.app.drawerMenuOnBackPressed
import mx.ucargo.android.app.setUpDrawer
import mx.ucargo.android.orderdetails.OrderDetailsActivity
import mx.ucargo.android.orderdetails.OrderDetailsModel
import javax.inject.Inject

class OrderListActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, OrderListActivity::class.java)
    }

    @Inject
    lateinit var orderListViewModel: OrderListViewModel

    lateinit var orderListAdapter: OrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.order_list_activity)

        setUpDrawer(drawerLayout)

        orderListAdapter = OrderListAdapter()
        orderListAdapter.onItemSelected = {
            startActivity(OrderDetailsActivity.newIntent(this, it.id))
        }

        configureRecyclerView(orderListAdapter)

        orderListViewModel.orderList.observe(this, orderListObserver)

        orderListViewModel.loading.observe(this, loadingObserver)

        orderListSwipeRefreshLayout.setOnRefreshListener({
            orderListViewModel.getOrderList()
        })
    }

    private fun configureRecyclerView(adapter: OrderListAdapter) {
        orderlistRecyclerView.setHasFixedSize(true)
        orderlistRecyclerView.isDrawingCacheEnabled = true;
        orderlistRecyclerView.layoutManager = LinearLayoutManager(this)
        orderlistRecyclerView.adapter = adapter
    }

    private val orderListObserver = Observer<List<OrderDetailsModel>> {
        it?.let {
            orderListAdapter.replaceOrderList(it)
        }
    }

    private val loadingObserver = Observer<Boolean> {
        orderListSwipeRefreshLayout.isRefreshing = it ?: false
    }

    override fun onBackPressed() {
        drawerMenuOnBackPressed {
            super.onBackPressed()
        }
    }
}


