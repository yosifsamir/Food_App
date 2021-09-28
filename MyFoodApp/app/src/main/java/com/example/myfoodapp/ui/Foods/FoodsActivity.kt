package com.example.myfoodapp.ui.Foods

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andremion.counterfab.CounterFab
import com.example.myfoodapp.R
import com.example.myfoodapp.adapter.FoodAdapter
import com.example.myfoodapp.adapter.MenuAdapter
import com.example.myfoodapp.model.Food
import com.example.myfoodapp.ui.home.HomeViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FoodsActivity : AppCompatActivity() {
    lateinit var foodsViewModel: FoodsViewModel
    private lateinit var layoutManager: GridLayoutManager
    private var recyclerView: RecyclerView? =null
    private var counterBtn:CounterFab?=null
    private var compsiteDisposable=CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foods)
        recyclerView=findViewById(R.id.foodsRecyclerView)
        layoutManager= GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
        counterBtn=findViewById(R.id.counterBtn)

        foodsViewModel=ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(FoodsViewModel::class.java)

        foodsViewModel.listOfFoods!!.observe(this, object : Observer<List<Food>> {
            override fun onChanged(listOfFood: List<Food>?) {
                var adapter= FoodAdapter(listOfFood!!)
                recyclerView!!.adapter = adapter
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val isLastItem = position == adapter.listOfFoods!!.lastIndex

                        return if (isLastItem && (position%2==0)) {
                            /**
                             * Return this for full width of screen.
                             * It will be always equal 1, dividing only to avoid numbers in code.
                             */
                            2
                        } else {
                            // Return this for place item in your column.
                            1
                        }
                    }
                }
            }
        })


    }

    override fun onStart() {
        super.onStart()
        compsiteDisposable.add(foodsViewModel.countItemInCart()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({onSuccess->
                counterBtn!!.count=onSuccess
            },{onError-> Toast.makeText(this@FoodsActivity,"Error"+onError.message,Toast.LENGTH_LONG).show()}))
    }

    override fun onStop() {
        super.onStop()
        compsiteDisposable.clear()
    }
}