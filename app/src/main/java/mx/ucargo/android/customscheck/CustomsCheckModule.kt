package mx.ucargo.android.customscheck

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.usecase.GetOrderUseCase
import mx.ucargo.android.usecase.GetRouteUseCase
import mx.ucargo.android.usecase.SendEventUseCase
import java.util.*

@Module
class CustomsCheckModule {
    @Provides
    fun provideOrderDetailsViewModelFactory(getOrderUseCase: GetOrderUseCase,getRouteUseCase: GetRouteUseCase) =
            OrderDetailsViewModel.Factory(getOrderUseCase,getRouteUseCase, { Date() })

    @Provides
    fun provideOrderDetailsViewModel(fragment: CustomsCheckFragment, factory: OrderDetailsViewModel.Factory): OrderDetailsViewModel =
            ViewModelProviders.of(fragment.activity!!, factory).get(OrderDetailsViewModel::class.java)

    @Provides
    fun provideCustomsCheckViewModelFactory(sendEventUseCase: SendEventUseCase) = CustomsCheckViewModel.Factory(sendEventUseCase)

    @Provides
    fun provideCustomsCheckViewModel(fragment: CustomsCheckFragment, factory: CustomsCheckViewModel.Factory): CustomsCheckViewModel =
            ViewModelProviders.of(fragment, factory).get(CustomsCheckViewModel::class.java)
}
