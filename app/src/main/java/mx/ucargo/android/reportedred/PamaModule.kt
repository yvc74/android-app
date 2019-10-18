package mx.ucargo.android.reportedred

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mx.ucargo.android.usecase.SendEventUseCase


@Module
class PamaModule {

    @Provides
    fun providesReportSignViewModelFactory() =
            PamaViewModel.Factory()

    @Provides
    fun provideReportSignViewModel(activity: PamaActivity, factory: PamaViewModel.Factory): PamaViewModel =
            ViewModelProviders.of(activity, factory).get(PamaViewModel::class.java)
}