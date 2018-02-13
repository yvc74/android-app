package mx.ucargo.android.begin

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.usecase.BeginUseCase
import mx.ucargo.android.usecase.GetOrderUseCase
import java.util.*

@Module
class BeginModule {
    @Provides
    fun provideOrderDetailsViewModelFactory(getOrderUseCase: GetOrderUseCase) =
            OrderDetailsViewModel.Factory(getOrderUseCase, { Date() })

    @Provides
    fun provideOrderDetailsViewModel(fragment: BeginFragment, factory: OrderDetailsViewModel.Factory): OrderDetailsViewModel =
            ViewModelProviders.of(fragment.activity!!, factory).get(OrderDetailsViewModel::class.java)

    @Provides
    fun provideBeginViewModelFactory(beginUseCase: BeginUseCase) = BeginViewModel.Factory(beginUseCase)

    @Provides
    fun provideBeginViewModel(fragment: BeginFragment, factory: BeginViewModel.Factory): BeginViewModel =
            ViewModelProviders.of(fragment, factory).get(BeginViewModel::class.java)
}
