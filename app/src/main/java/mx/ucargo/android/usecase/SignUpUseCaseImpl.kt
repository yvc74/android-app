package mx.ucargo.android.usecase

import mx.ucargo.android.Data.Retrofit.UserDataModel
import mx.ucargo.android.Data.SignUpGateway

/**
 * Created by noeperezchamorro on 26/01/18.
 */
class SignUpUseCaseImpl(val signUpGateway: SignUpGateway) : SignUpUseCase{
    override fun execute(user: UserDataModel): Boolean {
       return signUpGateway.registerUser(user)
    }


}