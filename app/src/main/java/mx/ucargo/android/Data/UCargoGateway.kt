package mx.ucargo.android.Data

import mx.ucargo.android.Data.Retrofit.UserDataModel

/**
 * Created by noeperezchamorro on 26/01/18.
 */
interface UCargoGateway{
    fun registerUser(user: UserDataModel,succes: (message: String) -> Unit)
}