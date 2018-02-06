package mx.ucargo.android.data

import mx.ucargo.android.entity.Account

interface UCargoGateway {
    fun signIn(username: String, password: String, success: (Account) -> Unit, failure : (Throwable) -> Unit)
}
