package mx.ucargo.android.data.retrofit

import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.data.ApiGateway
import mx.ucargo.android.data.retrofit.model.*
import mx.ucargo.android.entity.Account
import mx.ucargo.android.entity.Location
import mx.ucargo.android.entity.Order
import mx.ucargo.android.entity.Unauthorized
import okhttp3.Credentials
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

private const val UNAUTHORIZED = 401

class RetrofitApiGateway(private val uCargoApiService: UCargoApiService,
                         private val accountStorage: AccountStorage) : ApiGateway {





    override fun findById(orderId: String): Order {
        val response = uCargoApiService.orderById(orderId,accountStorage.get().token).execute()
        if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }
        return response.body()?.toOrder() ?: throw Exception("Not found")
    }

    override fun sendQuote(order: Order): Order {
        val response = uCargoApiService.sendQuote(order.toQuoteDataModel().toMap(), order.id, accountStorage.get().token).execute()
        if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }
        return order
    }

    override fun beginRouteToCustom(order: Order): Order {
        val response = uCargoApiService.sendBegin(toBeginDataModel().toMap(),order.id,accountStorage.get().token).execute()
        if (!response.isSuccessful){
            throw Exception("Unknown error")
        }
        return order
    }


    override fun reportGreen(order: Order,customType: String): Order {
        val response = uCargoApiService.sendCustomType(toCustomDataModel(customType).toMap(),order.id,accountStorage.get().token).execute()
        if (!response.isSuccessful){
            throw Exception("Unknown error")
        }
        return order
    }

    override fun reportLock(order: Order, imageUrl: String): Order {
        val response = uCargoApiService.sendLockImage(toLockDataModel(imageUrl).toMap(),order.id,accountStorage.get().token).execute()
        if (!response.isSuccessful){
            throw Exception("Unknown error")
        }
        return order
    }

    override fun reportBeginRouten(order: Order): Order {
        val response = uCargoApiService.sendBeginRoute(toBeginRoute().toMap(),order.id,accountStorage.get().token).execute()
        if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }
        return order
    }

    override fun reportStore(order: Order): Order {
        val response = uCargoApiService.sendStore(toStore().toMap(),order.id,accountStorage.get().token).execute()
        if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }
        return order

    }

    override fun reportLocation(order: Order, location: android.location.Location): Order {

        val response = uCargoApiService.sendLocation(toLocationEventDataModel(location).toMap(),order.id,accountStorage.get().token).execute()
        if (!response.isSuccessful){
            throw Exception("Unknown error")
        }
        return order
    }


    override fun reportSign(order: Order): Order{

        /*val response = uCargoApiService.sendLocation(toSignEventDataModel().toMap(),order.id,accountStorage.get().token).execute()
        if (!response.isSuccessful){
            throw Exception("Unknown error")
        }*/
        return order
    }


    override fun getOrderList(): List<Order> {
        val response = uCargoApiService.orders(accountStorage.get().token).execute()
        if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }
        return response.body()?.orders?.map { it.toOrder() } ?: emptyList()
    }

    override fun getOrderAssigned(): List<Order> {
        val response = uCargoApiService.ordersLog(accountStorage.get().token,"approved").execute()
        if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }
        return response.body()?.orders?.map { it.toOrder() } ?: emptyList()
    }

    override fun getOrderLog(): List<Order> {
        val response = uCargoApiService.ordersLog(accountStorage.get().token,"all").execute()
        if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }
        return response.body()?.orders?.map { it.toOrder() } ?: emptyList()
    }

    override fun signIn(username: String, password: String): Account {
        val response = uCargoApiService.signIn(Credentials.basic(username, password)).execute()
        if (!response.isSuccessful && response.code() == UNAUTHORIZED) {
            throw Unauthorized()
        } else if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }
        return response.body()?.account?.toAccount() ?: throw Exception("Unknown error")
    }

    override fun editAccout(account: Account): Account {
        val response = uCargoApiService.editDriverAccount(accountStorage.get().token, account.toAccountDataModel().toMap()).execute()
        if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }
        return response.body()?.account?.toAccount() ?: throw Exception("Unknown error")
    }

    override fun updateAccout(): Account {
        val response = uCargoApiService.updateAccount(accountStorage.get().token).execute()
        if (!response.isSuccessful && response.code() == UNAUTHORIZED) {
            throw Unauthorized()
        } else if (!response.isSuccessful) {
            throw Exception("Unknown error")
        }
        return response.body()?.account?.toAccount() ?: throw Exception("Unknown error")
    }
}

private fun Account.toAccountDataModel() = AccountDataModel(
        name = name,
        email = email,
        username = username,
        picture = picture,
        token = token,
        driverid = driverid,
        score = score,
        phone = phone
)

private fun AccountDataModel.toMap(): HashMap<String, AccountDataModel> {
    val myMap = HashMap<String, AccountDataModel>()
    myMap["account"] = this
    return myMap
}

private fun Order.toQuoteDataModel() = QuoteEventDataModel(
        quote = quote,
        uuid = UUID.randomUUID().toString(),
        date = getCurrentDate(),
        name = "Quote"
)

private fun toBeginDataModel() = BeginEventDataModel(
        uuid = UUID.randomUUID().toString(),
        name = "BeginCustom",
        date = getCurrentDate()
)

private fun toCustomDataModel(customType: String) = CustomEventDataModel(
        uuid = UUID.randomUUID().toString(),
        name = customType,
        date = getCurrentDate()
)

private fun toLockDataModel(url:String) = LockEventDataModel(
        uuid = UUID.randomUUID().toString(),
        name = "ReportLock",
        date = getCurrentDate(),
        picture = url
)


private fun toBeginRoute() = EventDataModel(
        uuid = UUID.randomUUID().toString(),
        name = "BeginRoute",
        date = getCurrentDate()
)

private fun toStore() = EventDataModel(
        uuid = UUID.randomUUID().toString(),
        name = "Store",
        date = getCurrentDate()
)

private fun toLocationEventDataModel(location: android.location.Location) = LocationEventDataModel(
        uuid = UUID.randomUUID().toString(),
        name = "ReportLocation",
        date = getCurrentDate(),
        track = TrackEventDataModel(longitude = location.longitude,latitude = location.latitude,bearing = location.bearing)
)


private fun toSignEventDataModel() = BeginEventDataModel(
        uuid = UUID.randomUUID().toString(),
        name = "ReportSign",
        date = getCurrentDate()
)

private fun getCurrentDate(): String{
    val timezone = TimeZone.getTimeZone("UTC")
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") // Quoted "Z" to indicate UTC, no timezone offset
    formatter.timeZone = timezone
    return formatter.format(Date())
}


private fun EventDataModel.toMap(): HashMap<String, EventDataModel>{
    val myMap = HashMap<String, EventDataModel>()
    myMap["event"] = this
    return myMap
}

private fun QuoteEventDataModel.toMap(): HashMap<String, QuoteEventDataModel>{
    val myMap = HashMap<String, QuoteEventDataModel>()
    myMap["event"] = this
    return myMap
}


private fun BeginEventDataModel.toMap(): HashMap<String, BeginEventDataModel>{
    val myMap = HashMap<String, BeginEventDataModel>()
    myMap["event"] = this
    return myMap
}

private fun  CustomEventDataModel.toMap(): HashMap<String , CustomEventDataModel>{
    val myMap = HashMap<String, CustomEventDataModel>()
    myMap["event"] = this
    return myMap
}

private fun  LockEventDataModel.toMap(): HashMap<String , LockEventDataModel>{
    val myMap = HashMap<String, LockEventDataModel>()
    myMap["event"] = this
    return myMap
}

private fun  LocationEventDataModel.toMap(): HashMap<String , LocationEventDataModel>{
    val myMap = HashMap<String, LocationEventDataModel>()
    myMap["event"] = this
    return myMap
}

private fun Int.toQuoteDataModel() = QuoteEventDataModel(this)

private fun OrderDataModel.toOrder() = Order(
        id = orderNumber,
        status = Order.Status.valueOf(status),
        pickup = pickup.toLocation(),
        delivery = delivery.toLocation(),
        customs = customs.toLocation(),
        type = if (type == 1) Order.Type.EXPORT else Order.Type.IMPORT,
        quoteDeadline = deadline.toDate(),
        details = details.map { it.toDetail() },
        quote = quote.toInt()
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
        label = label,
        schedule = schedule,
        latitude = latitude,
        longitude = longitude
)

private fun AccountDataModel.toAccount() = Account(
        name = name,
        email = email,
        token = token,
        driverid = driverid,
        username = username,
        picture = picture,
        score = score,
        phone = phone
)
