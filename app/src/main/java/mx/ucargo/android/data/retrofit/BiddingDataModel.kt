package mx.ucargo.android.data.retrofit

import com.google.gson.annotations.SerializedName


class BiddingDataModel {
    @SerializedName("destination")
    var destination = ""
    @SerializedName("origin")
    var origin = ""
    @SerializedName("typeOfBidding")
    var typeOfBidding = ""
    @SerializedName("timeToExpire")
    var timeToExpire = ""

    constructor(destination: String, origin: String, typeOfBidding: String, timeToExpire: String) {
        this.destination = destination
        this.origin = origin
        this.typeOfBidding = typeOfBidding
        this.timeToExpire = timeToExpire
    }
}