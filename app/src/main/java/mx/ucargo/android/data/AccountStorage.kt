package mx.ucargo.android.data

import mx.ucargo.android.entity.Account

interface AccountStorage {
    @Throws(Exception::class)
    fun get(): Account

    fun put(account: Account)

    fun delete(): Boolean
}
