package mx.ucargo.android.usecase

interface SignInUseCase {
    fun execute(username: String, password: String, success: () -> Unit, failure: (Throwable) -> Unit)
}
