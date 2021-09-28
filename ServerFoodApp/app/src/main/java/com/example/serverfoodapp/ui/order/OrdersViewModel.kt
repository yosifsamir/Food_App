package com.example.serverfoodapp.ui.order

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.serverfoodapp.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class OrdersViewModel(application: Application) : AndroidViewModel(application) {
    var orderMutableLiveData: MutableLiveData<List<Order>>? = MutableLiveData()


    init {
        getOrdersData(0.0)
    }

    fun getOrdersData(value:Double){
        var arrayList=ArrayList<Order>()
        FirebaseDatabase.getInstance().reference
            .child("Order")
            .orderByChild("orderStatus")
            .equalTo(value)
            .limitToLast(100)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot in snapshot.children){
                        var order=snapshot.getValue(Order::class.java)!!
                        order.orderNumber=snapshot.key
//                        order.cartSize=
                        Log.d("ddd", order.cartItemList!!.size.toString())
                        arrayList.add(0,order)
                    }

//                    if(arrayList.size>0){
//                        arrayList.sortWith(Comparator { o1: Order, o2: Order ->
//                            if (o1.createdAt!! < o2.createdAt!!)
//                                return@Comparator -1
//                               return@Comparator if (o1.createdAt == o2.createdAt)  0 else 1
//                        })
//                    }

                    orderMutableLiveData!!.postValue(arrayList)
                }
            })
    }

}