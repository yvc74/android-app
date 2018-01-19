package mx.ucargo.android.signin

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class SignInViewModel : ViewModel() {
    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SignInViewModel() as T
        }
    }
}
