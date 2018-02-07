package mx.ucargo.android.app

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mx.ucargo.android.orderdetails.OrderDetailsActivity
import mx.ucargo.android.orderdetails.OrderDetailsModule
import mx.ucargo.android.signin.SignInActivity
import mx.ucargo.android.signin.SignInModule
import mx.ucargo.android.signup.SignUpActivity
import mx.ucargo.android.signup.SignUpModule


@Module
abstract class ActivityBinder {
    @ContributesAndroidInjector(modules = arrayOf(SignInModule::class))
    abstract fun bindSignInActivity(): SignInActivity

    @ContributesAndroidInjector(modules = arrayOf(SignUpModule::class))
    abstract fun bindSignUpActivity(): SignUpActivity

    @ContributesAndroidInjector(modules = arrayOf(OrderDetailsModule::class))
    abstract fun bindOrderDetailsActivity(): OrderDetailsActivity
}
