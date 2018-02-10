package mx.ucargo.android.app

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import mx.ucargo.android.data.retrofit.RetrofitModule
import mx.ucargo.android.data.sharedpreferences.SharedPreferencesModule
import mx.ucargo.android.usecase.UseCaseModule
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    AppModule::class,
    UIBinder::class,
    UseCaseModule::class,
    RetrofitModule::class,
    SharedPreferencesModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent

    }

    fun inject(app: App)
}
