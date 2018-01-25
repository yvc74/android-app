package mx.ucargo.android.signin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class SignInViewModel : ViewModel {
    val isSignIn = MutableLiveData<Boolean>()

    constructor() {
        isSignIn.value = false
    }

    fun send(username: String, password: String) {
        isSignIn.postValue(true)
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SignInViewModel() as T
        }

    }
}
