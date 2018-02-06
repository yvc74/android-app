package mx.ucargo.android.data.retrofit

import mx.ucargo.android.entity.Account

object Mappers {
    fun mapAccount(accountDataModel: AccountDataModel) = Account(
            name = accountDataModel.name,
            email = accountDataModel.email,
            picture = accountDataModel.picture,
            token = accountDataModel.token
    )
}
