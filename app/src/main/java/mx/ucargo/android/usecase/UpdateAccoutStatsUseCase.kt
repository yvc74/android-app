package mx.ucargo.android.usecase

import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.entity.Account
import kotlin.concurrent.thread


interface UpdateAccoutStatsUseCase {
    fun execute(success: (Account) -> Unit, failure: ((Throwable) -> Unit)? = null)
}


class UpdateAccoutStatsUseCaseImpl(private val uCargoGateway: ApiGateway,
                                   private val accountStorage: AccountStorage) : UpdateAccoutStatsUseCase {

    override fun execute(success: (Account) -> Unit, failure: ((Throwable) -> Unit)?) {
        thread {
            try {
                success.invoke(executeSync())
            } catch (e: Exception) {
                failure?.invoke(e)
            }
        }
    }

    internal fun executeSync(): Account {
        val account = uCargoGateway.updateAccout()
        accountStorage.put(account)
        return account
    }
}
