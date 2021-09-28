package com.example.serverfoodapp.ui.food

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.serverfoodapp.R
import com.example.serverfoodapp.adapter.FoodAdapter
import com.example.serverfoodapp.callback.MyItemTouch
import com.example.serverfoodapp.common.Comm
import com.example.serverfoodapp.model.Food
import com.example.serverfoodapp.ui.addon.AddonActivity
import com.example.serverfoodapp.ui.size.SizeActivity
import java.util.*

class FoodsActivity : AppCompatActivity() {
    private lateinit var adapter: FoodAdapter
    lateinit var foodsViewModel: FoodsViewModel
    private lateinit var layoutManager: LinearLayoutManager
    private var recyclerView: RecyclerView? =null

    private var itemTouchHelper: ItemTouchHelper? =null
    private var myItemTouch:MyItemTouch ? =null

    private var listOfFoods= mutableListOf<Food>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foods)
        recyclerView=findViewById(R.id.foodsRecyclerView)
        layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
        adapter= FoodAdapter(listOfFoods!!)
        recyclerView!!.adapter = adapter
        initMyItemTouch()


        foodsViewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(FoodsViewModel::class.java)

        foodsViewModel.listOfFoods!!.observe(this, object : Observer<List<Food>> {
            override fun onChanged(listOfFood: List<Food>?) {

                listOfFoods.clear()
                listOfFoods.addAll(listOfFood!!)
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun initMyItemTouch() {
        myItemTouch= object : MyItemTouch(context = this@FoodsActivity, recyclerView = recyclerView!!) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>?
            ) {
                underlayButtons!!.add(UnderlayButton("UPDATE",0,Color.parseColor("#FF3C30"),
                    object : UnderlayButtonClickListener {
                        override fun onClick(pos: Int) {
                            Comm.selectedFood=listOfFoods[pos]
                            Toast.makeText(this@FoodsActivity, Comm.selectedFood!!.name,Toast.LENGTH_LONG).show()

                        }
                    }))

                underlayButtons!!.add(UnderlayButton("DELETE",0,Color.RED,
                    object : UnderlayButtonClickListener {
                        override fun onClick(pos: Int) {

                            var food=listOfFoods.removeAt(pos)
                            adapter.notifyItemRemoved(pos)
                            adapter.notifyItemRangeChanged(pos,listOfFoods.size);
//                            foodsViewModel.deleteFood(
//                                food.key!!,
//                                object : DeleteCallback {
//                                    override fun onSuccess(task: Task<Void>) {
//                                        foodsViewModel.getFoods()
//                                    }
//
//                                    override fun onFailed(exception: Exception) {
//                                        Toast.makeText(this@FoodsActivity,exception.message,Toast.LENGTH_SHORT).show()
//
//                                    }
//                                })

                            val hashMap = HashMap<String, Any>()
                            hashMap["foods"] = listOfFoods
                            foodsViewModel.updateFoods(hashMap)

                        }
                    }))
                underlayButtons!!.add(UnderlayButton("SIZE",0,Color.parseColor("#9430ff"),
                    object : UnderlayButtonClickListener {
                        override fun onClick(pos: Int) {
                            Comm.selectedFood=listOfFoods[pos]
                            Log.d("pos","${pos}")
                            startActivity(Intent(this@FoodsActivity,SizeActivity::class.java))
                        }
                    }))

                underlayButtons!!.add(UnderlayButton("ADDON",0,Color.parseColor("#ff3060"),
                    object : UnderlayButtonClickListener {
                        override fun onClick(pos: Int) {
                            Comm.selectedFood=listOfFoods[pos]
                            Log.d("pos","${pos}")
                            startActivity(Intent(this@FoodsActivity, AddonActivity::class.java))


                        }
                    }))


            }
        }

        itemTouchHelper= ItemTouchHelper(myItemTouch!!)

    }



}