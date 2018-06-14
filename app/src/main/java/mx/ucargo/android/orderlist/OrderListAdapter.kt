package mx.ucargo.android.orderlist

import android.graphics.Color
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.order_list_item.view.*
import mx.ucargo.android.R
import mx.ucargo.android.entity.Order
import mx.ucargo.android.orderdetails.OrderDetailsModel


class OrderListAdapter : RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {
    var onItemSelected: ((OrderDetailsModel) -> Unit)? = null

    private val orderList = ArrayList<OrderDetailsModel>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount() = orderList.size

    override fun getItemId(position: Int) = orderList[position].id.hashCode().toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false) as View)
    }

    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener({
                onItemSelected?.invoke(orderList[adapterPosition].copy())
            })
        }

        fun bind(order: OrderDetailsModel) {
            itemView.destinationTextView.text = order.destinationName
            itemView.originTextView.text = order.originName
            itemView.orderTypeTextView.setText(if (order.orderType == Order.Type.IMPORT) {
                R.string.order_details_type_import
            } else {
                R.string.order_details_type_export
            })
            itemView.orderTypeTextView.setTextColor(if (order.orderType == Order.Type.IMPORT) {
                Color.parseColor("#4a90e2")
            } else {
                Color.parseColor("#83c43d")
            })
            itemView.detailsTextView.text = order.detailsformat
        }
    }

    fun replaceOrderList(orderList: List<OrderDetailsModel>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(this.orderList, orderList))

        this.orderList.clear()
        this.orderList.addAll(orderList)

        diffResult.dispatchUpdatesTo(this)
    }

    class DiffCallback(val oldList: List<OrderDetailsModel>, val newList: List<OrderDetailsModel>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].id == newList[newItemPosition].id
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }
}
