package mx.ucargo.android.collectcharge

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.usecase.GetOrderUseCase
import mx.ucargo.android.usecase.GetRouteUseCase
import mx.ucargo.android.usecase.SendEventUseCase
import java.util.*


@Module
class CollectCheckModule {
    @Provides
    fun provideOrderDetailsViewModelFactory(getOrderUseCase: GetOrderUseCase, getRouteUseCase: GetRouteUseCase) =
            OrderDetailsViewModel.Factory(getOrderUseCase, getRouteUseCase,{ Date() })

    @Provides
    fun provideOrderDetailsViewModel(fragment: CollectCheckFragment, factory: OrderDetailsViewModel.Factory): OrderDetailsViewModel =
            ViewModelProviders.of(fragment.activity!!, factory).get(OrderDetailsViewModel::class.java)

    @Provides
    fun provideCollectViewModelFactory(sendEventUseCase: SendEventUseCase) = CollectCheckViewModel.Factory(sendEventUseCase)

    @Provides
    fun provideCollectViewModel(fragment: CollectCheckFragment, factory: CollectCheckViewModel.Factory): CollectCheckViewModel =
            ViewModelProviders.of(fragment, factory).get(CollectCheckViewModel::class.java)
}
