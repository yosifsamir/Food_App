package com.example.serverfoodapp.ui.food


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.serverfoodapp.common.Comm
import com.example.serverfoodapp.model.Food
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List

class FoodsViewModel(application: Application) : AndroidViewModel(application) {
    var listOfFoods:MutableLiveData<List<Food>> ? = MutableLiveData()


    init {
        getFoods()
    }

    fun getFoods() {
        FirebaseDatabase.getInstance().reference.child("Category").child(Comm.selectedMenu!!.key!!).child("foods").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var arrayList=ArrayList<Food>()
                    for (snapshot in snapshot.children){
                        var food=snapshot.getValue(Food::class.java)!!
                        food.key=snapshot.key
                        Log.d("key", food.key!!+" "+food.name+" and finally key is "+ food.key)
                        arrayList.add(food)
                    }

                    listOfFoods!!.postValue(arrayList)
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

    }

    fun updateFoods(hashMap: HashMap<String, Any>) {
        FirebaseDatabase.getInstance().reference.child("Category").child(Comm.selectedMenu!!.key!!)
            .updateChildren(
                hashMap
            ) { error: DatabaseError?, ref: DatabaseReference? ->
                Log.d("pos", "delete successfully")

            }
    }

//    fun deleteFood(key:String , deleteCallback: DeleteCallback){
//        FirebaseDatabase.getInstance().reference
//            .child("Category")
//            .child(Comm.selectedMenu!!.key!!)
//            .child("foods")
//            .child(key)
//            .removeValue()
//            .addOnFailureListener({
//                deleteCallback.onFailed(it)
//            }).addOnCompleteListener({
//                deleteCallback.onSuccess(it)
//            })
//    }

}