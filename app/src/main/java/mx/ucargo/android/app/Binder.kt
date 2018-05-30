package mx.ucargo.android.app

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import mx.ucargo.android.begin.BeginFragment
import mx.ucargo.android.begin.BeginViewModel
import mx.ucargo.android.customscheck.CustomsCheckFragment
import mx.ucargo.android.customscheck.CustomsCheckViewModel
import mx.ucargo.android.orderdetails.OrderDetailsActivity
import mx.ucargo.android.orderdetails.OrderDetailsViewModel
import mx.ucargo.android.orderlist.OrderListActivity
import mx.ucargo.android.orderlist.OrderListViewModel
import mx.ucargo.android.sendquote.SendQuoteFragment
import mx.ucargo.android.sendquote.SendQuoteViewModel
import mx.ucargo.android.sentquote.SentQuoteFragment
import mx.ucargo.android.signin.SignInActivity
import mx.ucargo.android.signin.SignInModule
import mx.ucargo.android.signup.SignUpActivity
import mx.ucargo.android.signup.SignUpModule


@Module(includes = [ViewModelFactoryModule::class])
abstract class Binder {
    @ContributesAndroidInjector(modules = [(SignInModule::class)])
    abstract fun bindSignInActivity(): SignInActivity

    @ContributesAndroidInjector(modules = [(SignUpModule::class)])
    abstract fun bindSignUpActivity(): SignUpActivity


    @ContributesAndroidInjector
    abstract fun bindOrderDetailsActivity(): OrderDetailsActivity

    @Binds
    @IntoMap
    @ViewModelKey(OrderDetailsViewModel::class)
    abstract fun bindOrderDetailsViewModel(viewModel: OrderDetailsViewModel): ViewModel


    @ContributesAndroidInjector
    abstract fun bindOrderListActivity(): OrderListActivity

    @Binds
    @IntoMap
    @ViewModelKey(OrderListViewModel::class)
    abstract fun bindOrderListViewModel(viewModel: OrderListViewModel): ViewModel


    @ContributesAndroidInjector
    abstract fun bindSendQuoteFragment(): SendQuoteFragment

    @Binds
    @IntoMap
    @ViewModelKey(SendQuoteViewModel::class)
    abstract fun bindSendQuoteViewModel(viewModel: SendQuoteViewModel): ViewModel


    @ContributesAndroidInjector
    abstract fun bindSentQuoteFragment(): SentQuoteFragment


    @ContributesAndroidInjector
    abstract fun bindBeginFragment(): BeginFragment

    @Binds
    @IntoMap
    @ViewModelKey(BeginViewModel::class)
    abstract fun bindBeginViewModel(viewModel: BeginViewModel): ViewModel


    @ContributesAndroidInjector
    abstract fun bindCustomsCheckFragment(): CustomsCheckFragment

    @Binds
    @IntoMap
    @ViewModelKey(CustomsCheckViewModel::class)
    abstract fun bindCustomsCheckViewModel(viewModel: CustomsCheckViewModel): ViewModel
}
