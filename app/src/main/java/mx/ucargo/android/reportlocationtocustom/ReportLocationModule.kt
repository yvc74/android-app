package mx.ucargo.android.reportlocationtocustom

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.customscheck.CustomsCheckFragment
import mx.ucargo.android.customscheck.CustomsCheckViewModel
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.reportlock.ReportLockFragment
import mx.ucargo.android.usecase.GetOrderUseCase
import mx.ucargo.android.usecase.GetRouteUseCase
import mx.ucargo.android.usecase.SendEventUseCase
import java.util.*

@Module
class ReportLocationModule {
    @Provides
    fun provideOrderDetailsViewModelFactory(getOrderUseCase: GetOrderUseCase, getRouteUseCase: GetRouteUseCase) =
            OrderDetailsViewModel.Factory(getOrderUseCase, getRouteUseCase, { Date() })

    @Provides
    fun provideOrderDetailsViewModel(fragment: ReportLocationFragment, factory: OrderDetailsViewModel.Factory): OrderDetailsViewModel =
            ViewModelProviders.of(fragment.activity!!, factory).get(OrderDetailsViewModel::class.java)


    @Provides
    fun provideReportLocationViewModelFactory(sendEventUseCase: SendEventUseCase) = ReportLocationViewModel.Factory(sendEventUseCase)

    @Provides
    fun provideReportLocationViewModel(fragment: ReportLocationFragment, factory: ReportLocationViewModel.Factory): ReportLocationViewModel =
            ViewModelProviders.of(fragment, factory).get(ReportLocationViewModel::class.java)
}