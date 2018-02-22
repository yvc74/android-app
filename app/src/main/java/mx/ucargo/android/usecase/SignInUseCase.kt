package mx.ucargo.android.usecase

import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.entity.Account
import kotlin.concurrent.thread

interface SignInUseCase {
    fun execute(username: String, password: String, success: (Account) -> Unit, failure: ((Throwable) -> Unit)? = null)
}

class SignInUseCaseImpl(private val uCargoGateway: ApiGateway,
                        private val accountStorage: AccountStorage) : SignInUseCase {

    override fun execute(username: String, password: String, success: (Account) -> Unit, failure: ((Throwable) -> Unit)?) {
        thread {
            try {
                success.invoke(executeSync(username, password))
            } catch (t: Throwable) {
                failure?.invoke(t)
            }
        }
    }

    internal fun executeSync(username: String, password: String): Account {
        val account = uCargoGateway.signIn(username, password)
        accountStorage.put(account)
        return account
    }
}
