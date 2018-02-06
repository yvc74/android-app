package mx.ucargo.android.usecase

import mx.ucargo.android.data.UCargoGateway


class SignInUseCaseImpl(val uCargoGateway: UCargoGateway) : SignInUseCase {
    override fun execute(username: String, password: String, success: () -> Unit, failure: (Throwable) -> Unit) {
        return uCargoGateway.signIn(username, password, {
            success.invoke()
        }, failure)
    }
}
