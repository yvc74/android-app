package mx.ucargo.android.data

import mx.ucargo.android.entity.Account
import mx.ucargo.android.entity.Order

interface ApiGateway {
    fun findById(orderId: String): Order
    fun getOrderList(): List<Order>
    fun sendQuote(order: Order): Order
    fun signIn(username: String, password: String): Account
    fun editAccout(account: Account): Account
}
