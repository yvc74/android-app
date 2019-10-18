package mx.ucargo.android.data.retrofit

import mx.ucargo.android.data.GoogleMapsApiGateway
import mx.ucargo.android.entity.Route
import com.google.maps.android.PolyUtil
import mx.ucargo.android.data.retrofit.model.RouteDataModel


class RetrofitGoogleMapsApiGateway(private val googleMapsApiService: GoogleMapsApiService) : GoogleMapsApiGateway {
    override fun getRoute(origin: String,delivery: String, pickUp : String?): List<Route> {
        if (pickUp == null) {
            val response = googleMapsApiService.getRoutesImport(origin, delivery).execute()
            return response.body()?.routes?.map { it.toRoute() } ?: emptyList()
        }
        else{
            val response = googleMapsApiService.getRoutesExport(origin, delivery,pickUp).execute()
            return response.body()?.routes?.map { it.toRoute() } ?: emptyList()
        }
    }
}

private fun RouteDataModel.toRoute() = Route(
        points = PolyUtil.decode(polyline.points)
)
