package mx.ucargo.android.data.retrofit

import mx.ucargo.android.R
import mx.ucargo.android.data.retrofit.model.AccountDataModel
import mx.ucargo.android.data.retrofit.model.LocationDataModel
import mx.ucargo.android.data.retrofit.model.OrdersResponseDataModel
import mx.ucargo.android.data.retrofit.model.QuoteDataModel
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
                details = listOf(
                        Order.Detail(icon = R.drawable.ic_map_24dp, name = R.string.order_details_distance, value = orderDataModel.distance),
                        Order.Detail(icon = R.drawable.ic_assignment_24dp, name = R.string.order_details_content, value = orderDataModel.merchandiseType),
                        Order.Detail(icon = R.drawable.ic_title_24dp, name = R.string.order_details_order_number, value = orderDataModel.orderNumber),
                        Order.Detail(icon = R.drawable.ic_local_shipping_24dp, name = R.string.order_details_transport, value = orderDataModel.transport),
                        Order.Detail(icon = R.drawable.ic_settings_overscan_black_24dp, name = R.string.order_details_weight, value = orderDataModel.weight)
                )
        )
    }

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
