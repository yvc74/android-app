package mx.ucargo.android.app

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mx.ucargo.android.signin.SignInActivity
import mx.ucargo.android.signin.SignInModule


@Module
abstract class ActivityBinder {
    @ContributesAndroidInjector(modules = arrayOf(SignInModule::class))
    abstract fun bindTripListActivity(): SignInActivity
}
