package mx.ucargo.android.data.retrofit

import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.data.retrofit.model.*
import mx.ucargo.android.entity.Account
import mx.ucargo.android.entity.Location
import mx.ucargo.android.entity.Order
import mx.ucargo.android.entity.UnauthorizedException
import okhttp3.Credentials
import java.text.SimpleDateFormat

private const val UNAUTHORIZED = 401

class RetrofitApiGateway(private val uCargoApiService: UCargoApiService,
                         private val accountStorage: AccountStorage) : ApiGateway {
    override fun findById(orderId: String): Order? {
        val response = uCargoApiService.orders(accountStorage.get().token).execute()
        if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }

        return response.body()?.orders?.first()?.toOrder()
    }

    override fun sendEvent(order: Order): Order {
        val response = uCargoApiService.sendQuote(order.quote.toQuoteDataModel(), order.id, accountStorage.get().token).execute()

        if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }

        return order
    }

    override fun getOrderList(): List<Order> {
        val response = uCargoApiService.orders(accountStorage.get().token).execute()

        if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }

        return response.body()?.orders?.map { it.toOrder() } ?: emptyList()
    }

    override fun signIn(username: String, password: String): Account {
        val response = uCargoApiService.signIn(Credentials.basic(username, password)).execute()
        if (!response.isSuccessful && response.code() == UNAUTHORIZED) {
            throw UnauthorizedException()
        } else if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }
        return response.body()?.account?.toAccount() ?: throw Exception("Unknown error")
    }
}

private fun Int.toQuoteDataModel() = QuoteDataModel(this)

private fun OrderDataModel.toOrder() = Order(
        id = id,
        pickup = pickup.toLocation(),
        delivery = delivery.toLocation(),
        type = if (type == 1) Order.Type.IMPORT else Order.Type.EXPORT,
        quoteDeadline = deadline.toDate(),
        details = details.map { it.toDetail() },
        quote = quote
)

private fun OrderDetailDataModel.toDetail() = Order.Detail(
        icon = url,
        label = label,
        value = value
)

private fun String.toDate() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSz").parse(this)

private fun LocationDataModel.toLocation() = Location(
        name = name,
        address = address,
        schedule = schedule,
        latitude = latitude,
        longitude = longitude
)

private fun AccountDataModel.toAccount() = Account(
        name = name,
        email = email,
        picture = picture,
        token = token
)
