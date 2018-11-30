package mx.ucargo.android.orderlist

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.order_list_activity.*
import kotlinx.android.synthetic.main.sign_in_activity.*
import kotlinx.android.synthetic.main.toolbar.*
import mx.ucargo.android.R
import mx.ucargo.android.app.drawerMenuOnBackPressed
import mx.ucargo.android.app.setUpDrawer
import mx.ucargo.android.app.setUpMenuHeader
import mx.ucargo.android.editprofile.Profile
import mx.ucargo.android.orderdetails.OrderDetailsActivity
import mx.ucargo.android.orderdetails.OrderDetailsModel
import javax.inject.Inject



private const val TYPE_OF_ORDER_LIST = "TYPE_OF_ORDER_LIST"

class OrderListActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context,type_of_order_list: Int = 0) = Intent(context, OrderListActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            putExtra(TYPE_OF_ORDER_LIST, type_of_order_list)
        }
    }

    @Inject
    lateinit var orderListViewModel: OrderListViewModel

    lateinit var orderListAdapter: OrderListAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_list_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        orderListViewModel.profile.observe(this,profileObserver)

        when (intent.getIntExtra(TYPE_OF_ORDER_LIST, 0)) {
            0 -> {setTitle("Cotizaciones")
                  setUpDrawer(drawerLayout,0)
                   orderListViewModel.getOrderList(0)}
            1 ->{setTitle("Asignados")
                setUpDrawer(drawerLayout,1)
                orderListViewModel.getOrderList(1)}
            2 -> {setTitle("Historial")
                setUpDrawer(drawerLayout,2)
                orderListViewModel.getOrderList(2)}
            else -> false
        }

        orderListAdapter = OrderListAdapter()
        orderListAdapter.onItemSelected = {
            startActivity(OrderDetailsActivity.newIntent(this, it.id))
        }

        configureRecyclerView(orderListAdapter)

        orderListViewModel.orderList.observe(this, orderListObserver)

        orderListViewModel.loading.observe(this, loadingObserver)



        orderListSwipeRefreshLayout.setOnRefreshListener({
            orderListViewModel.getOrderList(intent.getIntExtra(TYPE_OF_ORDER_LIST, 0))
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

    private val profileObserver = Observer<Profile> {
        it?.let {
            setUpMenuHeader(it)
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


