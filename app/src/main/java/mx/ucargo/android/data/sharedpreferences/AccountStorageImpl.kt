package mx.ucargo.android.data.sharedpreferences

import android.content.SharedPreferences
import com.google.gson.Gson
import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.entity.Account

private const val ACCOUNT = "ACCOUNT"

class AccountStorageImpl(private val sharedPreferences: SharedPreferences, private val gson: Gson) : AccountStorage {

    var cachedAccount: Account? = null

    override fun get(): Account {
        if (cachedAccount == null) {
            cachedAccount = gson.fromJson(sharedPreferences.getString(ACCOUNT, ""), Account::class.java)
        }

        return cachedAccount!!
    }

    override fun put(account: Account) {
        sharedPreferences.edit().putString(ACCOUNT, gson.toJson(account)).commit()
        cachedAccount = account
    }

    override fun delete(): Boolean {
       sharedPreferences.edit().clear().commit()
        cachedAccount = null
        return true
    }
}
