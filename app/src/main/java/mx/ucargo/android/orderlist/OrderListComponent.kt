package mx.ucargo.android.orderlist


import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(OrderListModule::class))
interface OrderListComponent {
    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: OrderListActivity): Builder

        fun build(): OrderListComponent

    }

    fun inject(activity: OrderListActivity)
}