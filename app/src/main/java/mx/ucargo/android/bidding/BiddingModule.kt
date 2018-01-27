package mx.ucargo.android.bidding

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides

@Module
class BiddingModule {
    @Provides
    fun provideBiddingViewModelFactory(): BiddingViewModel.Factory {
        return BiddingViewModel.Factory()
    }

    @Provides
    fun provideBiddingViewModel(activity: BiddingActivity, factory: BiddingViewModel.Factory): BiddingViewModel {
        return ViewModelProviders.of(activity, factory).get(BiddingViewModel::class.java)
    }

    @Provides
    fun provideBiddingAdapter(activity: BiddingActivity): BiddingAdapter = BiddingAdapter(activity)
}
