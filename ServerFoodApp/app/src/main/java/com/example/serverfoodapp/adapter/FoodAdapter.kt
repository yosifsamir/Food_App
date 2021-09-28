package com.example.serverfoodapp.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.serverfoodapp.R
import com.example.serverfoodapp.common.Comm
import com.example.serverfoodapp.model.Food
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
            Log.d("ttt",position.toString())
            Comm.selectedFood=listOfFoods[position]
            Toast.makeText(holder.foodImageView.context,"ttt",Toast.LENGTH_SHORT).show()
//            holder.itemView.context.startActivity(
//                Intent(holder.itemView.context,
//                    FoodDetailsActivity::class.java)
//            )
        })
    }


    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foodImageView=itemView.findViewById<ImageView>(R.id.menu_image_view)
        var foodTextView=itemView.findViewById<TextView>(R.id.menu_text_view)

    }
}