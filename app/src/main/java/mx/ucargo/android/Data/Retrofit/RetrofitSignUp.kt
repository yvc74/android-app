package mx.ucargo.android.Data.Retrofit

import android.nfc.Tag
import android.util.Log
import mx.ucargo.android.Data.UCargoGateway
import retrofit2.Call
import retrofit2.Response
import java.net.PasswordAuthentication
import javax.security.auth.callback.Callback


class RetrofitSignUp(val uCargoApiService: UCargoApiService): UCargoGateway {

    companion object {
        val TAG = "RetrofitImages"
    }

    override fun registerUser(user: UserDataModel,succes: (message: String) -> Unit) {
        val call  = uCargoApiService.signUpUser(user)

        call.enqueue(object : retrofit2.Callback<UserDataModel?> {
            override fun onResponse(call: Call<UserDataModel?>?, response: Response<UserDataModel?>?) {
                if (!response?.isSuccessful!!) {
                    Log.d(TAG, response?.errorBody().toString())
                    succes.invoke("Mensaje de Error del servidor")
                    return
                }
                else{
                    succes.invoke("Mensaje de exito")
                }
            }

            override fun onFailure(call: Call<UserDataModel?>?, t: Throwable?) {
                Log.d(TAG, "onFailure", t)
                    succes.invoke("Error de conecion con el servidor")
            }
        })

    }


}