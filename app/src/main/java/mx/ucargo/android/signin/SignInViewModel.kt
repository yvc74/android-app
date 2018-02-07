package mx.ucargo.android.signin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import mx.ucargo.android.usecase.GetAccountUseCase
import mx.ucargo.android.usecase.SignInUseCase

class SignInViewModel(private val signInUseCase: SignInUseCase,
                      private val getAccountUseCase: GetAccountUseCase) : ViewModel() {
    val isSignIn = MutableLiveData<Boolean>()
    val formError = MutableLiveData<Throwable>()

    init {
        isSignIn.value = false

        getAccountUseCase.execute({
            isSignIn.postValue(true)
        }, {
            // Ignore it
        })
    }

    fun send(username: String, password: String) {
        signInUseCase.execute(username, password, {
            isSignIn.postValue(true)
        }, {
            formError.postValue(it)
        })
    }

    class Factory(private val signInUseCase: SignInUseCase,
                  private val getAccountUseCase: GetAccountUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SignInViewModel(signInUseCase, getAccountUseCase) as T
        }
    }
}
