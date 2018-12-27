package mx.ucargo.android.app

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mx.ucargo.android.reportlock.ReportLockFragment
import mx.ucargo.android.reportlock.ReportLockModule
import mx.ucargo.android.begin.BeginFragment
import mx.ucargo.android.begin.BeginModule
import mx.ucargo.android.collectcharge.CollectCheckFragment
import mx.ucargo.android.collectcharge.CollectCheckModule
import mx.ucargo.android.customscheck.CustomsCheckFragment
import mx.ucargo.android.customscheck.CustomsCheckModule
import mx.ucargo.android.destinaionreport.DestinationModule
import mx.ucargo.android.destinaionreport.ReportDestinationFragment
import mx.ucargo.android.editprofile.EditProfileActivity
import mx.ucargo.android.editprofile.EditProfileModule
import mx.ucargo.android.orderdetails.OrderDetailsActivity
import mx.ucargo.android.orderdetails.OrderDetailsModule
import mx.ucargo.android.orderlist.OrderListActivity
import mx.ucargo.android.orderlist.OrderListModule
import mx.ucargo.android.reportedred.PamaActivity
import mx.ucargo.android.reportedred.PamaModule
import mx.ucargo.android.reportlocationtocustom.ReportLocationFragment
import mx.ucargo.android.reportlocationtocustom.ReportLocationModule
import mx.ucargo.android.reportsign.ReportSignActivity
import mx.ucargo.android.reportsign.ReportSignModule
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

    @ContributesAndroidInjector(modules = [(EditProfileModule::class)])
    abstract fun bindEditProfileActivity(): EditProfileActivity

    @ContributesAndroidInjector(modules = [(ReportSignModule::class)])
    abstract fun bindReportSignActivity(): ReportSignActivity

    @ContributesAndroidInjector(modules = [(SendQuoteModule::class)])
    abstract fun bindSendQuoteFragment(): SendQuoteFragment

    @ContributesAndroidInjector(modules = [(SentQuoteModule::class)])
    abstract fun bindSentQuoteFragment(): SentQuoteFragment

    @ContributesAndroidInjector(modules = [(BeginModule::class)])
    abstract fun bindBeginFragment(): BeginFragment

    @ContributesAndroidInjector(modules = [(CustomsCheckModule::class)])
    abstract fun bindCustomsCheckFragment(): CustomsCheckFragment

    @ContributesAndroidInjector(modules = [(ReportLockModule::class)])
    abstract fun bindReportLockFragment(): ReportLockFragment


    @ContributesAndroidInjector(modules = [(ReportLocationModule::class)])
    abstract fun bindReportLocationFragment(): ReportLocationFragment

    @ContributesAndroidInjector(modules = [(DestinationModule::class)])
    abstract fun bindReportDestinationFragment(): ReportDestinationFragment

    @ContributesAndroidInjector(modules = [(CollectCheckModule::class)])
    abstract fun bindCollectFragment(): CollectCheckFragment

    @ContributesAndroidInjector(modules = [(PamaModule::class)])
    abstract fun bindPamaActivity(): PamaActivity
}
