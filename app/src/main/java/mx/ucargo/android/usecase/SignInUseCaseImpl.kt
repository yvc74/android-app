package mx.ucargo.android.usecase

import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.UCargoGateway


class SignInUseCaseImpl(private val uCargoGateway: UCargoGateway,
                        private val accountStorage: AccountStorage) : SignInUseCase {

    override fun execute(username: String, password: String, success: () -> Unit, failure: (Throwable) -> Unit) {
        return uCargoGateway.signIn(username, password, {
            accountStorage.put(it)
            success.invoke()
        }, failure)
    }
}
