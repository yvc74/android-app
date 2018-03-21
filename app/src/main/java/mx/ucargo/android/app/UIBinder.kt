package mx.ucargo.android.app

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mx.ucargo.android.orderdetails.OrderDetailsActivity
import mx.ucargo.android.orderdetails.OrderDetailsModule
import mx.ucargo.android.orderlist.OrderListActivity
import mx.ucargo.android.orderlist.OrderListModule
import mx.ucargo.android.sendquote.SendQuoteFragment
import mx.ucargo.android.sendquote.SendQuoteModule
import mx.ucargo.android.sentquote.SentQuoteFragment
import mx.ucargo.android.sentquote.SentQuoteModule
import mx.ucargo.android.signin.SignInActivity
import mx.ucargo.android.signin.SignInModule
import mx.ucargo.android.signup.SignUpActivity
import mx.ucargo.android.signup.SignUpModule


@Module
abstract class UIBinder {
    @ContributesAndroidInjector(modules = [(SignInModule::class)])
    abstract fun bindSignInActivity(): SignInActivity

    @ContributesAndroidInjector(modules = [(SignUpModule::class)])
    abstract fun bindSignUpActivity(): SignUpActivity

    @ContributesAndroidInjector(modules = [(OrderDetailsModule::class)])
    abstract fun bindOrderDetailsActivity(): OrderDetailsActivity

    @ContributesAndroidInjector(modules = [(OrderListModule::class)])
    abstract fun bindOrderListActivity(): OrderListActivity

    @ContributesAndroidInjector(modules = [(SendQuoteModule::class)])
    abstract fun bindSendQuoteFragment(): SendQuoteFragment

    @ContributesAndroidInjector(modules = [(SentQuoteModule::class)])
    abstract fun bindSentQuoteModule(): SentQuoteFragment
}
