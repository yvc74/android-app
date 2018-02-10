package mx.ucargo.android.sentquote

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.usecase.GetOrderUseCase
import java.util.*

@Module
class SentQuoteModule {
    @Provides
    fun provideOrderDetailsViewModelFactory(getOrderUseCase: GetOrderUseCase) =
            OrderDetailsViewModel.Factory(getOrderUseCase, { Date() })

    @Provides
    fun provideOrderDetailsViewModel(fragment: SentQuoteFragment, factory: OrderDetailsViewModel.Factory) =
            ViewModelProviders.of(fragment.activity!!, factory).get(OrderDetailsViewModel::class.java)
}
