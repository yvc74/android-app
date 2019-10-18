package mx.ucargo.android.destinaionreport

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.begin.BeginFragment
import mx.ucargo.android.begin.BeginViewModel
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.usecase.GetOrderUseCase
import mx.ucargo.android.usecase.GetRouteUseCase
import mx.ucargo.android.usecase.SendEventUseCase
import java.util.*


@Module
class DestinationModule {
    @Provides
    fun provideOrderDetailsViewModelFactory(getOrderUseCase: GetOrderUseCase, getRouteUseCase: GetRouteUseCase) =
            OrderDetailsViewModel.Factory(getOrderUseCase, getRouteUseCase,{ Date() })

    @Provides
    fun provideOrderDetailsViewModel(fragment: ReportDestinationFragment, factory: OrderDetailsViewModel.Factory): OrderDetailsViewModel =
            ViewModelProviders.of(fragment.activity!!, factory).get(OrderDetailsViewModel::class.java)

    @Provides
    fun provideBeginViewModelFactory(sendEventUseCase: SendEventUseCase) = DestinationViewModel.Factory(sendEventUseCase)

    @Provides
    fun provideBeginViewModel(fragment: ReportDestinationFragment, factory: DestinationViewModel.Factory): DestinationViewModel =
            ViewModelProviders.of(fragment, factory).get(DestinationViewModel::class.java)
}
