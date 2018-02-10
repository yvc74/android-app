package mx.ucargo.android.sendquote

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.usecase.GetOrderUseCase
import mx.ucargo.android.usecase.SendQuoteUseCase
import java.util.*

@Module
class SendQuoteModule {
    @Provides
    fun provideOrderDetailsViewModelFactory(getOrderUseCase: GetOrderUseCase) =
            OrderDetailsViewModel.Factory(getOrderUseCase, { Date() })

    @Provides
    fun provideOrderDetailsViewModel(fragment: SendQuoteFragment, factory: OrderDetailsViewModel.Factory): OrderDetailsViewModel =
            ViewModelProviders.of(fragment.activity!!, factory).get(OrderDetailsViewModel::class.java)

    @Provides
    fun provideSendQuoteViewModelFactory(sendQuoteUseCase: SendQuoteUseCase) =
            SendQuoteViewModel.Factory(sendQuoteUseCase)

    @Provides
    fun provideSendQuoteViewModel(fragment: SendQuoteFragment, factory: SendQuoteViewModel.Factory): SendQuoteViewModel =
            ViewModelProviders.of(fragment, factory).get(SendQuoteViewModel::class.java)
}
