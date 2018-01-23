package mx.ucargo.android.signup

import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(SignUpModule::class))
interface SignUpComponent {
    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: SignUpActivity): Builder

        fun build(): SignUpComponent

    }

    fun inject(activity: SignUpActivity)
}
