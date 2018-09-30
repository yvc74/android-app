package mx.ucargo.android.data

import android.location.Location
import mx.ucargo.android.entity.Account
import mx.ucargo.android.entity.Order

interface ApiGateway {
    fun findById(orderId: String): Order
    fun getOrderList(): List<Order>
    fun getOrderAssigned(): List<Order>
    fun getOrderLog(): List<Order>
    fun sendQuote(order: Order): Order
    fun signIn(username: String, password: String): Account
    fun editAccout(account: Account): Account
    fun updateAccout():Account
    fun beginRouteToCustom(order: Order):Order
    fun reportGreen(order: Order,customType: String): Order
    fun reportLock(order: Order,imageUrl : String): Order
    fun reportLocation(order: Order,location: Location): Order
    fun reportSign(order: Order): Order
}
