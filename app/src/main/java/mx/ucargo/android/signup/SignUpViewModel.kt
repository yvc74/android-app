package mx.ucargo.android.signup

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class SignUpViewModel : ViewModel {

    val isSignUp = MutableLiveData<Boolean>()


    constructor() {
        isSignUp.value = false

    }

    fun signUp(username: String, password: String) {
        isSignUp.postValue(true)
    }

    class Factory() : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SignUpViewModel() as T
        }
    }


}
