package mx.ucargo.android.app

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import mx.ucargo.android.data.retrofit.RetrofitModule
import mx.ucargo.android.usecase.UseCaseModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityBinder::class,
        UseCaseModule::class,
        RetrofitModule::class
))
interface AppComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent

    }

    fun inject(app: App)
}
