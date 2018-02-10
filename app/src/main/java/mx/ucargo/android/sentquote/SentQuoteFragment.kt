package mx.ucargo.android.sentquote

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.send_quote_fragment.*
import mx.ucargo.android.R
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.sendquote.SendQuoteFragment
import javax.inject.Inject

class SentQuoteFragment : Fragment() {
    companion object {
        const val ORDER_ID = "ORDER_ID"

        fun newInstance(orderId: String): SentQuoteFragment {
            val arguments = Bundle()
            arguments.putString(ORDER_ID, orderId)

            val fragment = SentQuoteFragment()
            fragment.arguments = arguments
            return fragment
        }
    }

    lateinit var orderId: String

    @Inject
    lateinit var orderDetailsViewModel: OrderDetailsViewModel

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderId = arguments?.getString(SendQuoteFragment.ORDER_ID)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sent_quote_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        orderDetailsViewModel.order.observe(this, orderObserver)
    }

    private val orderObserver = Observer<OrderDetailsModel> {
        it?.let {
            quoteEditText.setText(it.quote.toString())
        }
    }
}
