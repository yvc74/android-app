package mx.ucargo.android.signin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import mx.ucargo.android.usecase.GetAccountUseCase
import mx.ucargo.android.usecase.SignInUseCase
import mx.ucargo.android.usecase.SignOutUseCase

class SignInViewModel(private val signInUseCase: SignInUseCase,
                      private val getAccountUseCase: GetAccountUseCase,
                      private val signOutUseCase: SignOutUseCase) : ViewModel() {
    val isSignIn = MutableLiveData<Boolean>()
    val formError = MutableLiveData<Throwable>()


    fun signOut() {
        signOutUseCase.execute({

        })
    }

    fun checkSession() {
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

    @Suppress("UNCHECKED_CAST")
    class Factory(private val signInUseCase: SignInUseCase,
                  private val getAccountUseCase: GetAccountUseCase,
                  private val signOutUseCase: SignOutUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SignInViewModel(signInUseCase, getAccountUseCase, signOutUseCase) as T
        }
    }
}
