package mx.ucargo.android.customscheck

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.customs_check_fragment.*
import mx.ucargo.android.R
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import javax.inject.Inject

class CustomsCheckFragment : Fragment() {
    companion object {
        private const val ORDER_ID = "ORDER_ID"

        fun newInstance(orderId: String): CustomsCheckFragment {
            val arguments = Bundle()
            arguments.putString(ORDER_ID, orderId)

            val fragment = CustomsCheckFragment()
            fragment.arguments = arguments
            return fragment
        }
    }

    lateinit var orderId: String

    @Inject
    lateinit var orderDetailsViewModel: OrderDetailsViewModel

    @Inject
    lateinit var viewModel: CustomsCheckViewModel

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderId = arguments?.getString(ORDER_ID)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.customs_check_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.orderStatus.observe(this, orderStatusbserver)
        viewModel.error.observe(this, errorObserver)

        greenButton.setOnClickListener {
            viewModel.greenLight(orderId)
        }
    }

    private val orderStatusbserver = Observer<OrderDetailsModel.Status> {
        it?.let { newStatus ->
            orderDetailsViewModel.order.value?.let {
                it.status = newStatus
                orderDetailsViewModel.order.postValue(it)
            }
        }
    }

    private val errorObserver = Observer<Throwable> { throwable ->
        view?.let { view ->
            Snackbar.make(view, throwable?.message
                    ?: getString(R.string.error_generic), Snackbar.LENGTH_LONG).show()
        }
    }
}
