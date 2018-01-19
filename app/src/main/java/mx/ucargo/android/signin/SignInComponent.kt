package mx.ucargo.android.signin

import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(SignInModule::class))
interface SignInComponent {
    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: SignInActivity): Builder

        fun build(): SignInComponent

    }

    fun inject(activity: SignInActivity)
}
