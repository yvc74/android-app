package mx.ucargo.android.usecase

import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.entity.Account
import kotlin.concurrent.thread


interface SendEditProfileUseCase {
    fun execute(account: Account, success: (Account) -> Unit, failure: ((Throwable) -> Unit)? = null)
}


class SendEditProfileUseCaseImpl(private val apiGateway: ApiGateway,
                                 private val accountStorage: AccountStorage) : SendEditProfileUseCase {
    override fun execute(account: Account, success: (Account) -> Unit, failure: ((Throwable) -> Unit)?) {
        thread {
            try {
                success.invoke(executeSync(account))
            } catch (t: Throwable) {
                failure?.invoke(t)
            }
        }
    }

    private fun executeSync(account: Account): Account {
        val editedAccount = apiGateway.editAccout(account)
        accountStorage.put(editedAccount)
        return editedAccount
    }
}
