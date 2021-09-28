package com.example.myfoodapp.ui.Foods

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myfoodapp.common.Comm
import com.example.myfoodapp.database.MyDatabase
import com.example.myfoodapp.model.Cart
import com.example.myfoodapp.model.Food
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Flowable
import io.reactivex.Single

class FoodsViewModel(application: Application) : AndroidViewModel(application) {
    var listOfFoods:MutableLiveData<List<Food>> ? = MutableLiveData()
    private lateinit var myDatabase:MyDatabase

    init {
        FirebaseDatabase.getInstance().reference.child("Category").child(Comm.selectedMenu!!.key!!).child("foods").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var arrayList=ArrayList<Food>()
                    for (snapshot in snapshot.children){
                        arrayList.add(snapshot.getValue(Food::class.java)!!)
                    }

                    listOfFoods!!.postValue(arrayList)
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(application.applicationContext,error.message,Toast.LENGTH_LONG).show()
                }
            })
        myDatabase= MyDatabase.getDatabase(application.baseContext)

    }

    fun countItemInCart() :Single<Int>{
        return myDatabase.cartDao().countItemInCart(FirebaseAuth.getInstance().currentUser.uid)
    }

}