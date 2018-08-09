package mx.ucargo.android.sendquote

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.usecase.GetOrderUseCase
import mx.ucargo.android.usecase.GetRouteUseCase
import mx.ucargo.android.usecase.SendEventUseCase
import java.util.*

@Module
class SendQuoteModule {
    @Provides
    fun provideOrderDetailsViewModelFactory(getOrderUseCase: GetOrderUseCase,getRouteUseCase: GetRouteUseCase) =
            OrderDetailsViewModel.Factory(getOrderUseCase,getRouteUseCase, { Date() })

    @Provides
    fun provideOrderDetailsViewModel(fragment: SendQuoteFragment, factory: OrderDetailsViewModel.Factory): OrderDetailsViewModel =
            ViewModelProviders.of(fragment.activity!!, factory).get(OrderDetailsViewModel::class.java)

    @Provides
    fun provideSendQuoteViewModelFactory(sendEventUseCase: SendEventUseCase) =
            SendQuoteViewModel.Factory(sendEventUseCase)

    @Provides
    fun provideSendQuoteViewModel(fragment: SendQuoteFragment, factory: SendQuoteViewModel.Factory): SendQuoteViewModel =
            ViewModelProviders.of(fragment, factory).get(SendQuoteViewModel::class.java)
}
