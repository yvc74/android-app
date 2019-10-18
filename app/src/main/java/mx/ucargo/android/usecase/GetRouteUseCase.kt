package mx.ucargo.android.usecase

import mx.ucargo.android.data.GoogleMapsApiGateway
import mx.ucargo.android.entity.Route
import kotlin.concurrent.thread


interface GetRouteUseCase {
    fun execute(origin: String,delivery: String, pickUp : String?,success: (List<Route>) -> Unit, failure: ((Throwable) -> Unit)? = null)
}

class GetRouteUseCaseImpl(private val apiGateway: GoogleMapsApiGateway) : GetRouteUseCase {

    override fun execute(origin: String,delivery: String, pickUp : String?,success: (List<Route>) -> Unit, failure: ((Throwable) -> Unit)?) {
        thread {
            try {
                success.invoke(executeSync(origin,delivery,pickUp))
            } catch (t: Throwable) {
                failure?.invoke(t)
            }
        }
    }

    internal fun executeSync(origin: String,delivery: String, pickUp : String?) = apiGateway.getRoute(origin,delivery,pickUp)
}