package mx.ucargo.android.usecase

import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.entity.Account
import kotlin.concurrent.thread

interface SignOutUseCase {
    fun execute(success: (Boolean) -> Unit, failure: ((Throwable) -> Unit)? = null)
}


class SignOutUseCaseImpl(private val accountStorage: AccountStorage) : SignOutUseCase {
    override fun execute(success: (Boolean) -> Unit, failure: ((Throwable) -> Unit)?) {
        thread {
            try {
                success.invoke(executeSync())
            } catch (e: Exception) {
                failure?.invoke(e)
            }
        }
    }

    internal fun executeSync() = accountStorage.delete()
}

