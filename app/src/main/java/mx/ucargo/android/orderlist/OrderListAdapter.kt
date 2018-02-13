package mx.ucargo.android.orderlist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import mx.ucargo.android.R
import mx.ucargo.android.entity.Order
import mx.ucargo.android.orderdetails.OrderDetailsActivity
import mx.ucargo.android.orderdetails.OrderDetailsModel


class OrderListAdapter : RecyclerView.Adapter<OrderListAdapter.ViewHolder> {


    private val orderList: ArrayList<OrderDetailsModel>
    private val context: Context

    constructor(context: Context) : super() {
        this.context = context
        this.orderList = ArrayList<OrderDetailsModel>()
        setHasStableIds(true)
    }


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = orderList[position]
        holder?.bind(item, context)
    }



    fun removeAt(position: Int) {
        orderList.removeAt(position)
        notifyItemRemoved(position)
    }


    fun replaceBiddings(orderList: List<OrderDetailsModel>) {
        this.orderList.clear()
        this.orderList.addAll(orderList)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = orderList.size

    inner class ViewHolder(val biddingView: View?) : RecyclerView.ViewHolder(biddingView) {
        val txtDestination = biddingView?.findViewById<TextView>(R.id.txtDestination)
        val txtOrigin = biddingView?.findViewById<TextView>(R.id.txtOrigin)
        val imageStar = biddingView?.findViewById<ImageView>(R.id.imgfavorite)
        val txtOrdertype = biddingView?.findViewById<TextView>(R.id.txtOrderType)

        fun bind(order: OrderDetailsModel, context: Context) {
            txtDestination?.text = order.destinationName
            txtOrigin?.text = order.originName
            val orderType = if (order.orderType == Order.Type.IMPORT) {
                txtOrdertype?.text = "Importacion"
            } else {
                txtOrdertype?.text = "Exportacion"
            }

            itemView.setOnClickListener({
                context.startActivity(OrderDetailsActivity.newIntent(context, order.orderNumber))
            })


            imageStar?.isEnabled = true

            imageStar?.setOnClickListener({
                if (imageStar.isEnabled) {
                    imageStar.setImageResource(R.drawable.abc_ic_star_black_16dp)
                } else {
                    imageStar.setImageResource(R.drawable.abc_ic_star_half_black_16dp)
                }
            })
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.order_list_item, parent, false) as View)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}