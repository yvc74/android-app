package mx.ucargo.android.usecase

import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.entity.Account

class GetAccountUseCaseImpl(private val accountStorage: AccountStorage) : GetAccountUseCase {
    override fun execute(success: (Account) -> Unit, failure: (Throwable) -> Unit) {
        try {
            success.invoke(accountStorage.get())
        } catch (e: Exception) {
            failure.invoke(e)
        }
    }
}
