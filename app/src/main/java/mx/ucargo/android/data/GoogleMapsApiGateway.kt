package mx.ucargo.android.data

import mx.ucargo.android.entity.Route


interface GoogleMapsApiGateway {
   fun getRoute(origin: String,delivery: String, pickUp : String?): List<Route>
}