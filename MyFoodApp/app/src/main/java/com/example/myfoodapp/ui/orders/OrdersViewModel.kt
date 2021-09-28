package com.example.myfoodapp.ui.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myfoodapp.common.Comm
import com.example.myfoodapp.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrdersViewModel : AndroidViewModel {
    var orderMutableLiveData:MutableLiveData<List<Order>> ? =null
    constructor(application: Application) : super(application){
        orderMutableLiveData= MutableLiveData()
    }

    init {
        var arrayList=ArrayList<Order>()
        FirebaseDatabase.getInstance().reference
            .child("Order")
            .orderByChild("userId")
            .equalTo(FirebaseAuth.getInstance().currentUser.uid)
            .limitToLast(100)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot in snapshot.children){
                        var order=snapshot.getValue(Order::class.java)!!
                        order.orderNumber=snapshot.key
                        arrayList.add(0,order)
                    }

                    orderMutableLiveData!!.postValue(arrayList)
                }
            })
    }
}