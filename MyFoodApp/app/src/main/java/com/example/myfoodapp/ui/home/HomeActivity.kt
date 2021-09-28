package com.example.myfoodapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodapp.R
import com.example.myfoodapp.adapter.MenuAdapter
import com.example.myfoodapp.model.Menu
import com.example.myfoodapp.ui.cart.CartActivity
import com.example.myfoodapp.ui.orders.OrdersActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class HomeActivity : AppCompatActivity() {
    private lateinit var layoutManager: GridLayoutManager
    var homeViewModel:HomeViewModel ? = null
    var recyclerView:RecyclerView ? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        homeViewModel=ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(HomeViewModel::class.java)
        recyclerView=findViewById(R.id.recyclerMenu)
        layoutManager=GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager

        recyclerView!!.addItemDecoration(DividerItemDecoration(this@HomeActivity,DividerItemDecoration.VERTICAL))
        homeViewModel!!.allMenu.observe(this, object : Observer<List<Menu>> {
            override fun onChanged(listOfMenu: List<Menu>?) {
                Toast.makeText(this@HomeActivity,listOfMenu!![0].name,Toast.LENGTH_LONG).show()
                var adapter=MenuAdapter(listOfMenu)
                recyclerView!!.adapter = adapter
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val isLastItem = position == adapter.listOfMenu!!.lastIndex

                        return if (isLastItem) {
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

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.menu_cart){
            var intent=Intent(this@HomeActivity,CartActivity::class.java)
            startActivity(intent)
        }
        else if (item.itemId==R.id.menu_orders){
            var intent=Intent(this@HomeActivity,OrdersActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}