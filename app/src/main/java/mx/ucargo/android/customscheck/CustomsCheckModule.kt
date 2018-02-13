package mx.ucargo.android.customscheck

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.usecase.GetOrderUseCase
import java.util.*

@Module
class CustomsCheckModule {
    @Provides
    fun provideOrderDetailsViewModelFactory(getOrderUseCase: GetOrderUseCase) =
            OrderDetailsViewModel.Factory(getOrderUseCase, { Date() })

    @Provides
    fun provideOrderDetailsViewModel(fragment: CustomsCheckFragment, factory: OrderDetailsViewModel.Factory): OrderDetailsViewModel =
            ViewModelProviders.of(fragment.activity!!, factory).get(OrderDetailsViewModel::class.java)

    @Provides
    fun provideCustomsCheckViewModelFactory() = CustomsCheckViewModel.Factory()

    @Provides
    fun provideCustomsCheckViewModel(fragment: CustomsCheckFragment, factory: CustomsCheckViewModel.Factory): CustomsCheckViewModel =
            ViewModelProviders.of(fragment, factory).get(CustomsCheckViewModel::class.java)
}
