package mx.ucargo.android.begin

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.usecase.SendEventUseCase
import mx.ucargo.android.usecase.GetOrderUseCase
import mx.ucargo.android.usecase.GetRouteUseCase
import java.util.*

@Module
class BeginModule {
    @Provides
    fun provideOrderDetailsViewModelFactory(getOrderUseCase: GetOrderUseCase,getRouteUseCase: GetRouteUseCase) =
            OrderDetailsViewModel.Factory(getOrderUseCase, getRouteUseCase,{ Date() })

    @Provides
    fun provideOrderDetailsViewModel(fragment: BeginFragment, factory: OrderDetailsViewModel.Factory): OrderDetailsViewModel =
            ViewModelProviders.of(fragment.activity!!, factory).get(OrderDetailsViewModel::class.java)

    @Provides
    fun provideBeginViewModelFactory(sendEventUseCase: SendEventUseCase) = BeginViewModel.Factory(sendEventUseCase)

    @Provides
    fun provideBeginViewModel(fragment: BeginFragment, factory: BeginViewModel.Factory): BeginViewModel =
            ViewModelProviders.of(fragment, factory).get(BeginViewModel::class.java)
}
