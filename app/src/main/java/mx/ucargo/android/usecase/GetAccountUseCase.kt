package mx.ucargo.android.usecase

import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.entity.Account
import kotlin.concurrent.thread

interface GetAccountUseCase {
    fun execute(success: (Account) -> Unit, failure: ((Throwable) -> Unit)? = null)
}


class GetAccountUseCaseImpl(private val accountStorage: AccountStorage) : GetAccountUseCase {
    override fun execute(success: (Account) -> Unit, failure: ((Throwable) -> Unit)?) {
        thread {
            try {
                success.invoke(executeSync())
            } catch (e: Exception) {
                failure?.invoke(e)
            }
        }
    }

    internal fun executeSync() = accountStorage.get()
}
