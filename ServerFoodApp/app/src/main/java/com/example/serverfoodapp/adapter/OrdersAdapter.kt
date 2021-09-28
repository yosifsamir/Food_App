package com.example.serverfoodapp.adapter

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.serverfoodapp.R
import com.example.serverfoodapp.common.Comm
import com.example.serverfoodapp.model.Order
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private var context: Context? =null
    private var listOfOrders : ArrayList<Order> ? =null
    private lateinit var calendar: Calendar
    private lateinit var simpleDateFormat: SimpleDateFormat

    constructor(context: Context?, listOfOrders: ArrayList<Order>?) : super() {
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
        var date= Date()
        date.time= this!!.listOfOrders!![position].createdAt!!
        calendar.time=date
        holder.orderDate.setText(Comm.getDateOfWeeks(calendar.get(Calendar.DAY_OF_WEEK)) +" " + simpleDateFormat.format(date) )
        holder.orderStatus.setText(Comm.getStatusInfo(this!!.listOfOrders!![position].orderStatus))
        holder.orderName.text= getSpannableText(this!!.listOfOrders!![position].userName)
//        holder.orderDate.setText()

    }

    fun getOrderAtPos(pos:Int):Order{
        return listOfOrders!!.get(pos)
    }

    fun deleteOrderAtPos(pos:Int){
        listOfOrders!!.removeAt(pos)
    }

    private fun getSpannableText(userName: String?): CharSequence? {

        var spannableString=SpannableStringBuilder(userName)
        var foregroundColorSpan=ForegroundColorSpan(Color.parseColor("#FF0090FF"))
        spannableString.setSpan(foregroundColorSpan,0,userName!!.length,Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return spannableString
    }



    inner class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderImg=itemView.findViewById<ImageView>(R.id.order_image)
        var orderDate=itemView.findViewById<TextView>(R.id.order_date_txt)
        var orderNumber=itemView.findViewById<TextView>(R.id.order_number_txt)
        var orderName=itemView.findViewById<TextView>(R.id.order_name_txt)
        var orderStatus=itemView.findViewById<TextView>(R.id.order_status_txt)
    }


}