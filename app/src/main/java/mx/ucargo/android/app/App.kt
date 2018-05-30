package mx.ucargo.android.app

import android.app.Activity
import android.app.Application
import android.app.Service
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject


private val TAG = App::class.java.simpleName

class App : Application(), HasActivityInjector, HasServiceInjector {
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var eventQueue: EventQueue

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun activityInjector() = dispatchingActivityInjector

    override fun serviceInjector() = dispatchingServiceInjector
}
