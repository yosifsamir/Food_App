package com.example.myfoodapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodapp.R
import com.example.myfoodapp.common.Comm
import com.example.myfoodapp.model.Food
import com.example.myfoodapp.ui.Foods.FoodDetailsActivity
import com.example.myfoodapp.ui.Foods.FoodsActivity
import com.squareup.picasso.Picasso

class FoodAdapter : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{
    lateinit var listOfFoods:List<Food>


    constructor(listOfFoods: List<Food>) {
        this.listOfFoods = listOfFoods
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.menu_item,parent,false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfFoods.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        Picasso.get().load(listOfFoods[position].image).into(holder.foodImageView)
        holder.foodTextView.text=listOfFoods[position].name
        holder.itemView.setOnClickListener({
            Comm.selectedFood=listOfFoods[position]
            holder.itemView.context.startActivity(
                Intent(holder.itemView.context,
                    FoodDetailsActivity::class.java)
            )
        })
    }


    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foodImageView=itemView.findViewById<ImageView>(R.id.menu_image_view)
        var foodTextView=itemView.findViewById<TextView>(R.id.menu_text_view)

    }
}