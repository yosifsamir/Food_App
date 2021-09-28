package com.example.myfoodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.myfoodapp.R
import com.example.myfoodapp.common.Comm
import com.example.myfoodapp.model.Cart
import com.example.myfoodapp.model.Order
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private var context: Context? =null
    private var listOfOrders : List<Order> ? =null
    private lateinit var calendar:Calendar
    private lateinit var simpleDateFormat: SimpleDateFormat

    constructor(context: Context?, listOfOrders: List<Order>?) : super() {
        this.context = context
        this.listOfOrders = listOfOrders
        calendar= Calendar.getInstance()
        simpleDateFormat= SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.orders_layout,parent,false)
        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfOrders!!.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        Picasso.get().load(this!!.listOfOrders!![position].cartItemList!![0].foodImg).into(holder.orderImg)
        holder.orderNumber.setText(this!!.listOfOrders!![position].orderNumber)
        var date=Date()
        date.time= this!!.listOfOrders!![position].createdAt!!
        calendar.time=date
        holder.orderDate.setText(Comm.getDateOfWeeks(calendar.get(Calendar.DAY_OF_WEEK)) +" " + simpleDateFormat.format(date) )
        holder.orderStatus.setText(Comm.getStatusInfo(this!!.listOfOrders!![position].orderStatus))
        holder.orderComments.visibility=View.GONE
//        holder.orderDate.setText()

    }


    inner class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderImg=itemView.findViewById<ImageView>(R.id.order_image)
        var orderDate=itemView.findViewById<TextView>(R.id.order_date_txt)
        var orderNumber=itemView.findViewById<TextView>(R.id.order_number_txt)
        var orderComments=itemView.findViewById<TextView>(R.id.order_comment_txt)
        var orderStatus=itemView.findViewById<TextView>(R.id.order_status_txt)
    }

}