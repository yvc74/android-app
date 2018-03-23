package mx.ucargo.android.orderlist

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.usecase.GetOrderListUseCase

@Module
class OrderListModule {
    @Provides
    fun provideOrderListViewModelFactory(getOrderListUseCase: GetOrderListUseCase) =
            OrderListViewModel.Factory(getOrderListUseCase)

    @Provides
    fun provideOrderListViewModel(activity: OrderListActivity, factory: OrderListViewModel.Factory): OrderListViewModel {
        return ViewModelProviders.of(activity, factory).get(OrderListViewModel::class.java)
    }
}
