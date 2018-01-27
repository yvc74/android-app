package mx.ucargo.android.bidding

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mx.ucargo.android.data.retrofit.BiddingDataModel
import mx.ucargo.android.R


class BiddingAdapter : RecyclerView.Adapter<BiddingAdapter.ViewHolder> {


    private val bidddings: ArrayList<BiddingDataModel>
    private val context: Context

    constructor(context: Context) : super() {
        this.context = context
        this.bidddings = ArrayList<BiddingDataModel>()
        setHasStableIds(true)
    }


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.txtDestination?.text = bidddings[position].destination
        holder?.txtOrigin?.text = bidddings[position].origin
    }


    fun replaceBiddings(biddinglist: List<BiddingDataModel>) {
        this.bidddings.clear()
        this.bidddings.addAll(biddinglist)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = bidddings.size

    inner class ViewHolder(val biddingView: View?) : RecyclerView.ViewHolder(biddingView) {
        val txtDestination = biddingView?.findViewById<TextView>(R.id.txtDestination)
        val txtOrigin = biddingView?.findViewById<TextView>(R.id.txtOrigin)
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.bidding_item, parent, false) as View)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}