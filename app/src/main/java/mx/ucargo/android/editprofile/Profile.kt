package mx.ucargo.android.editprofile

data class Profile(
        var name: String = "",
        var username: String = "",
        var email: String = "",
        var picture: String = "",
        var score: Int = 0,
        var phone: String = ""
)
