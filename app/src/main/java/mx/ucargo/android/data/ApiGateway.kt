package mx.ucargo.android.data

import mx.ucargo.android.entity.Account
import mx.ucargo.android.entity.Order

interface ApiGateway {
    fun findById(orderId: String): Order?
    fun getOrderList(): List<Order>
    fun signIn(username: String, password: String): Account

    fun sendEvent(order: Order): Order
}
