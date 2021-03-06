package mx.ucargo.android.app

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class App : Application(), HasActivityInjector {
    @Inject
    @JvmField
    var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>? = null

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector as AndroidInjector<Activity>
    }

}
