package mx.ucargo.android.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class SignInResponseDataModel(
        @SerializedName("account") var account: AccountDataModel = AccountDataModel()
)
