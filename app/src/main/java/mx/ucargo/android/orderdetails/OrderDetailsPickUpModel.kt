package mx.ucargo.android.orderdetails


data class OrderDetailsPickUpModel (
        var address: String = "",
        var label: String = "",
        var date: String = "",
        var hour: String ="",
        var attendant: String = ""
)