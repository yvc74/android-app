package mx.ucargo.android.usecase

import mx.ucargo.android.Data.Retrofit.UserDataModel

import mx.ucargo.android.Data.UCargoGateway


class SignUpUseCaseImpl(val uCargoGateway: UCargoGateway) : SignUpUseCase{
    override fun execute(user: UserDataModel, succes: (message: String) -> Unit) {
        return uCargoGateway.registerUser(user,succes)
    }


}