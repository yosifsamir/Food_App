package com.example.myfoodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.myfoodapp.R
import com.example.myfoodapp.model.Cart
import com.example.myfoodapp.ui.cart.CartActivity
import com.squareup.picasso.Picasso

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private var context:Context ? =null
    private var listOfCartItem : List<Cart> ? =null
    private var cartCallback:CartCallback ? =null

    constructor(context: Context?, listOfCartItem: List<Cart>?) : super() {
        this.context = context
        this.listOfCartItem = listOfCartItem

        cartCallback=context as CartActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.cart_layout,parent,false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfCartItem!!.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.cartFoodName.text= this!!.listOfCartItem!![position].foodName
        holder.cartFoodPrice.text= this!!.listOfCartItem!![position].foodPrice.toString()
        Picasso.get().load(this!!.listOfCartItem!![position].foodImg).into(holder.cartFoodImg)
        holder.cartFoodQuantity.number= this!!.listOfCartItem!![position].foodQuantity.toString()


        holder.cartFoodQuantity.setOnValueChangeListener({ elegantNumberButton: ElegantNumberButton, oldValue: Int, newValue: Int ->
            var quantity=holder.cartFoodQuantity.number
            this@CartAdapter!!.listOfCartItem!![position].foodQuantity=newValue
            cartCallback!!.numberOfQuantity(this@CartAdapter!!.listOfCartItem!![position],holder.adapterPosition)

        })


        holder.cartFoodName.setOnClickListener({
//            Toast.makeText(context,"${holder.cartFoodName}",Toast.LENGTH_LONG).show()
        })

    }

    fun getCartItem(pos: Int):Cart {
        return listOfCartItem!!.get(pos)
    }




    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cartFoodImg=itemView.findViewById<ImageView>(R.id.cart_food_img)
        var cartFoodPrice=itemView.findViewById<TextView>(R.id.cart_food_price)
        var cartFoodName=itemView.findViewById<TextView>(R.id.cart_food_name)
        var cartFoodQuantity=itemView.findViewById<ElegantNumberButton>(R.id.cart_counter_btn)
    }

    interface CartCallback{
        fun numberOfQuantity(cart: Cart, currentPosition:Int)

    }
}