package mx.ucargo.android.orderdetails

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.usecase.GetOrderUseCase
import mx.ucargo.android.usecase.GetRouteUseCase
import java.util.*

@Module
class OrderDetailsModule {
    @Provides
    fun provideOrderDetailsViewModelFactory(getOrderUseCase: GetOrderUseCase,getRouteUseCase: GetRouteUseCase) =
            OrderDetailsViewModel.Factory(getOrderUseCase, getRouteUseCase,{ Date() })

    @Provides
    fun provideOrderDetailsViewModel(activity: OrderDetailsActivity, factory: OrderDetailsViewModel.Factory): OrderDetailsViewModel =
            ViewModelProviders.of(activity, factory).get(OrderDetailsViewModel::class.java)
}
