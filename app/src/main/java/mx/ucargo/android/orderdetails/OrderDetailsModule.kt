package mx.ucargo.android.orderdetails

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides

@Module
class OrderDetailsModule {
    @Provides
    fun provideOrderDetailsViewModelFactory() =
            OrderDetailsViewModel.Factory()

    @Provides
    fun provideOrderDetailsViewModel(activity: OrderDetailsActivity, factory: OrderDetailsViewModel.Factory): OrderDetailsViewModel =
            ViewModelProviders.of(activity, factory).get(OrderDetailsViewModel::class.java)
}
