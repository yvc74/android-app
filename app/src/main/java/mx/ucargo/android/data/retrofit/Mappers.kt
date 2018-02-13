package mx.ucargo.android.data.retrofit

import mx.ucargo.android.data.retrofit.model.*
import mx.ucargo.android.entity.Account
import mx.ucargo.android.entity.Location
import mx.ucargo.android.entity.Order
import java.text.SimpleDateFormat

object Mappers {
    val dateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss.SSSSSSZ")

    fun mapAccount(accountDataModel: AccountDataModel) = Account(
            name = accountDataModel.name,
            email = accountDataModel.email,
            picture = accountDataModel.picture,
            token = accountDataModel.token
    )

    fun mapOrder(response: OrdersResponseDataModel): Order {
        val orderDataModel = response.orders.first()
        return Order(
                origin = mapLocation(orderDataModel.origin, orderDataModel.pickUpAddress),
                destination = mapLocation(orderDataModel.destination),
                type = if (orderDataModel.type == 1) Order.Type.IMPORT else Order.Type.EXPORT,
                quoteDeadline = dateFormat.parse(orderDataModel.deadline),
                orderNumber = orderDataModel.orderNumber,
                details = orderDataModel.details.map { mapOrderDetailDataModel(it) }
        )
    }

    private fun mapOrderDetailDataModel(orderDetailDataModel: OrderDetailDataModel) = Order.Detail(
            icon = orderDetailDataModel.url,
            label = orderDetailDataModel.label,
            value = orderDetailDataModel.value
    )

    private fun mapLocation(locationDataModel: LocationDataModel, address: String = "") = Location(
            name = locationDataModel.name,
            address = address,
            latitude = locationDataModel.latitude,
            longitude = locationDataModel.longitude
    )

    fun mapQuoteDataModel(quote: Int): QuoteDataModel {
        val quoteDataModel = QuoteDataModel()
        quoteDataModel.quote = quote
        return quoteDataModel
    }
}
