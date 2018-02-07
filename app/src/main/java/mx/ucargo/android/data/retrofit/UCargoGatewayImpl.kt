package mx.ucargo.android.data.retrofit

import mx.ucargo.android.data.UCargoGateway
import mx.ucargo.android.data.retrofit.Mappers.mapAccount
import mx.ucargo.android.entity.Account
import mx.ucargo.android.entity.Unauthorized
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Response

class UCargoGatewayImpl(private val uCargoApiService: UCargoApiService) : UCargoGateway {
    companion object {
        val UNAUTHORIZED = 401
    }

    override fun signIn(username: String, password: String, success: (Account) -> Unit, throwable: (Throwable) -> Unit) {
        uCargoApiService.signIn(Credentials.basic(username, password)).enqueue(object : retrofit2.Callback<SignInResponseDataModel?> {
            override fun onResponse(call: Call<SignInResponseDataModel?>?, response: Response<SignInResponseDataModel?>?) {
                response?.let {
                    if (it.isSuccessful && it.body() is SignInResponseDataModel) {
                        success.invoke(mapAccount(it.body()!!.account))
                    } else if (it.code() == UNAUTHORIZED) {
                        throwable.invoke(Unauthorized())
                    } else {
                        throwable.invoke(Throwable())
                    }
                }
            }

            override fun onFailure(call: Call<SignInResponseDataModel?>?, t: Throwable?) {
                t?.let { throwable.invoke(it) }
            }
        })
    }
}
