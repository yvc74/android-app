package mx.ucargo.android.app

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import mx.ucargo.android.orderdetails.OrderDetailsActivityTest
import mx.ucargo.android.signin.SignInActivityTest
import mx.ucargo.android.usecase.UseCaseModule
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    UIBinder::class,
    UseCaseModule::class,
    MockModule::class
])
interface TestAppComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TestAppComponent

    }

    fun inject(app: TestApp)

    fun inject(test: SignInActivityTest)

    fun inject(test: OrderDetailsActivityTest)
}
