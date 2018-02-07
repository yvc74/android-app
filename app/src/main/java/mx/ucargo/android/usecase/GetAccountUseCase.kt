package mx.ucargo.android.usecase

import mx.ucargo.android.entity.Account

interface GetAccountUseCase {
    fun execute(success : (Account) -> Unit, failure : (Throwable) -> Unit)
}
