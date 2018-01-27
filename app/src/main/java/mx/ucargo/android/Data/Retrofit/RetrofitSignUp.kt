package mx.ucargo.android.Data.Retrofit

import android.nfc.Tag
import android.util.Log
import mx.ucargo.android.Data.SignUpGateway
import retrofit2.Call
import retrofit2.Response
import java.net.PasswordAuthentication
import javax.security.auth.callback.Callback

/**
 * Created by noeperezchamorro on 26/01/18.
 */
class RetrofitSignUp(val signUpService: SignUpService): SignUpGateway {

    companion object {
        val TAG = "RetrofitImages"
    }

    override fun registerUser(user: UserDataModel): Boolean {
        val call  = signUpService.signUser(user)
        var status = false;
        call.enqueue(object : retrofit2.Callback<UserDataModel?> {
            override fun onResponse(call: Call<UserDataModel?>?, response: Response<UserDataModel?>?) {
                if (!response?.isSuccessful!!) {
                    Log.d(TAG, response?.errorBody().toString())
                    status = false;
                    return
                }
                else{
                    status = true;
                }
            }

            override fun onFailure(call: Call<UserDataModel?>?, t: Throwable?) {
                Log.d(TAG, "onFailure", t)
                status = false
            }
        })
        return status
    }


}