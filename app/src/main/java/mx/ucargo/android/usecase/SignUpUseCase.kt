package mx.ucargo.android.usecase

import android.content.Context
import mx.ucargo.android.Data.Retrofit.UserDataModel

/**
 * Created by noeperezchamorro on 26/01/18.
 */
interface SignUpUseCase {

    fun execute(user: UserDataModel,succes: (message: String)-> Unit)

}