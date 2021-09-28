package com.example.serverfoodapp.ui.order

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.serverfoodapp.R
import com.example.serverfoodapp.adapter.FoodAdapter
import com.example.serverfoodapp.adapter.OrdersAdapter
import com.example.serverfoodapp.callback.MyItemTouch
import com.example.serverfoodapp.common.Comm
import com.example.serverfoodapp.fragment.BottomSheetOrderFragment
import com.example.serverfoodapp.model.Order
import com.example.serverfoodapp.ui.addon.AddonActivity
import com.example.serverfoodapp.ui.size.SizeActivity
import com.google.firebase.database.FirebaseDatabase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.lang.StringBuilder
import java.util.HashMap

class OrderActivity : AppCompatActivity() {
    private lateinit var itemTouchHelper: ItemTouchHelper
    private  var myItemTouch: MyItemTouch?=null
    lateinit var ordersViewModel: OrdersViewModel
    private lateinit var layoutManager: LinearLayoutManager
    private var recyclerView: RecyclerView? =null
    private var orderToolbar:Toolbar ? =null
    private var listOfOrders:ArrayList<Order>?= ArrayList()
    private lateinit var orderAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        orderToolbar=findViewById(R.id.orderToolbar)
        setSupportActionBar(orderToolbar)
        if (getSupportActionBar() != null){
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()!!.setDisplayShowHomeEnabled(true);
        }
        ordersViewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(OrdersViewModel::class.java)

        initRecyclerView()
        orderAdapter= OrdersAdapter(this,listOfOrders!!)
        recyclerView!!.adapter=orderAdapter


        ordersViewModel.orderMutableLiveData!!.observe(this, object : Observer<List<Order>> {
            override fun onChanged(orders: List<Order>?) {
                this@OrderActivity.listOfOrders!!.clear()
                this@OrderActivity.listOfOrders!!.addAll(orders!!)
                orderAdapter!!.notifyDataSetChanged()
                initMyTouch()
                Toast.makeText(this@OrderActivity, "Changed", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun initMyTouch() {



        if (myItemTouch != null) {
            myItemTouch!!.deattachSwipe()
        }
        myItemTouch = object : MyItemTouch(this@OrderActivity, recyclerView) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>?
            ) {
                underlayButtons!!.add(
                    UnderlayButton(
                        "Delete",
                        0,
                        Color.RED,
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                deleteFromFirebase(null,pos)

                            }
                        })
                )

                underlayButtons!!.add(
                    UnderlayButton(
                        "Directions",
                        0,
                        Color.BLUE,
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                Log.d("ddd","Directions")
                            }
                        })
                )

                underlayButtons!!.add(
                    UnderlayButton(
                        "CALL",
                        0,
                        Color.GREEN,
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                Log.d("ddd","CALL")
                                var intent=Intent(Intent.ACTION_DIAL)
                                intent.setData(Uri.parse(StringBuilder("tel:").append(orderAdapter!!.getOrderAtPos(pos).userPhone)
                                    .toString()))
                                startActivity(Intent.createChooser(intent,"Choose Call App"))
                            }
                        })
                )
            }

        }


        itemTouchHelper= ItemTouchHelper(myItemTouch!!)
    }

    private fun deleteFromFirebase(orderNumber: String?,pos:Int) {
        var deletedOrder=listOfOrders!!.removeAt(pos)
//        listOfOrders!!.removeAt(pos)
        orderAdapter.notifyItemRemoved(pos)
        orderAdapter.notifyItemRangeChanged(pos,listOfOrders!!.size)
        FirebaseDatabase.getInstance().reference
            .child("Order")
            .child(deletedOrder.orderNumber!!)
            .removeValue()
            .addOnFailureListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
//                orderAdapter.notifyItemRemoved(pos)
//                orderAdapter.notifyItemRangeChanged(pos,listOfOrders!!.size)
            }
        Log.d("ddd", deletedOrder.orderNumber!!)

    }

    private fun initRecyclerView() {
        recyclerView=findViewById(R.id.orders_recycler_view)
        layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.order_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.order_filter->{
                Log.d("ddd","order clicked")
                BottomSheetOrderFragment(ordersViewModel).showNow(supportFragmentManager,"orderFragment")
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}