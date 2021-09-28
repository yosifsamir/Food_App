package com.example.myfoodapp.ui.orders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodapp.R
import com.example.myfoodapp.adapter.OrdersAdapter
import com.example.myfoodapp.model.Order
import com.example.myfoodapp.ui.Foods.FoodsViewModel

class OrdersActivity : AppCompatActivity() {

    lateinit var ordersViewModel: OrdersViewModel
    private lateinit var layoutManager: LinearLayoutManager
    private var recyclerView: RecyclerView? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        initRecyclerView()
        ordersViewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(OrdersViewModel::class.java)

        ordersViewModel.orderMutableLiveData!!.observe(this, object : Observer<List<Order>> {
            override fun onChanged(listOfOrders: List<Order>?) {
               var ordersAdapter=OrdersAdapter(this@OrdersActivity,listOfOrders)
                recyclerView!!.adapter=ordersAdapter
            }
        })
    }

    private fun initRecyclerView() {
        recyclerView=findViewById(R.id.orders_recycler_view)
        layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
    }
}