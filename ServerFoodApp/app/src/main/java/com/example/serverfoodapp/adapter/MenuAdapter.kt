package com.example.serverfoodapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.serverfoodapp.R
import com.example.serverfoodapp.common.Comm
import com.example.serverfoodapp.model.Menu
import com.example.serverfoodapp.ui.food.FoodsActivity
import com.squareup.picasso.Picasso

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    var listOfMenu:List<Menu> ? =null
    constructor(listOfMenu: List<Menu>){
        this.listOfMenu=listOfMenu
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.menu_item,parent,false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfMenu!!.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        Picasso.get().load(this!!.listOfMenu!![position].image).into(holder.menuImageView)
        holder.menuTextView.text= this!!.listOfMenu!![position].name
        holder.itemView.setOnClickListener({
            Comm.selectedMenu= this!!.listOfMenu!![position]
            holder.itemView.context.startActivity(Intent(holder.itemView.context, FoodsActivity::class.java))
        })
    }


    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var menuImageView=itemView.findViewById<ImageView>(R.id.menu_image_view)
        var menuTextView=itemView.findViewById<TextView>(R.id.menu_text_view)
    }
}