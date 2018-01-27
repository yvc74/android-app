package mx.ucargo.android.bidding


import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(BiddingModule::class))
interface BiddingComponent {
    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: BiddingActivity): Builder

        fun build(): BiddingComponent

    }

    fun inject(activity: BiddingActivity)
}