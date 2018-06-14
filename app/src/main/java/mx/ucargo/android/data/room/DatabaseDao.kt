package mx.ucargo.android.data.room

import android.arch.persistence.room.*
import io.reactivex.Flowable
import mx.ucargo.android.data.room.model.EventDBModel
import mx.ucargo.android.data.room.model.OrderDBModel
import mx.ucargo.android.data.room.model.OrderDetailDBModel
import mx.ucargo.android.data.room.model.STATUS_PENDING

@Dao
interface DatabaseDao {
    @Query("SELECT * FROM Event WHERE status = $STATUS_PENDING ORDER BY id ASC LIMIT 1")
    fun findQueuedEvent(): Flowable<EventDBModel>


    @Query("SELECT * FROM `Order` WHERE id = :orderId")
    fun findOrderById(orderId: String): OrderDBModel?

    @Update
    fun updateOrder(order: OrderDBModel)

    @Insert
    fun insertOrder(order: OrderDBModel)

    @Query("SELECT * FROM OrderDetail WHERE orderId = :orderId")
    fun findOrderDetailsByOrderId(orderId: String): List<OrderDetailDBModel>

    @Insert
    fun insertEvent(event: EventDBModel): Long

    @Query("SELECT * FROM `Order`")
    fun findOrders(): Flowable<List<OrderDBModel>>

    @Insert
    fun insertOrderDetail(orderDetail: OrderDetailDBModel)

    @Delete
    fun deleteOrderDetails(orderDetails: List<OrderDetailDBModel>)
}
