package com.example.myfoodapp.ui.Foods

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myfoodapp.common.Comm
import com.example.myfoodapp.database.MyDatabase
import com.example.myfoodapp.model.Cart
import com.example.myfoodapp.model.Comments
import com.example.myfoodapp.model.Food
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable

class FoodDetailsViewModel(application: Application) : AndroidViewModel(application) {


    var foodMutableLiveData:MutableLiveData<Food> = MutableLiveData()
    var isSuccessFully:MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var myDatabase:MyDatabase
//    var foodCommentsLivData:MutableLiveData<List<Comments>> = MutableLiveData()
    init {
    foodMutableLiveData.postValue(Comm.selectedFood)
    myDatabase= MyDatabase.getDatabase(application.baseContext)
}
//    fun getComments(){
//        FirebaseDatabase.getInstance().reference.child("Comments")
//            .child(Comm.selectedFood!!.id!!)
//            .orderByChild("timestamp").limitToLast(100).addValueEventListener(
//            object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()){
//                        var arrayList=ArrayList<Comments>()
//                        for (snapshot in snapshot.children){
//                            arrayList.add(snapshot.getValue(Comments::class.java)!!)
//                        }
//                        foodCommentsLivData.postValue(arrayList)
//                    }
//
//                }
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//            })
//    }


    fun insertComment(comment:Comments) {
        FirebaseDatabase.getInstance().reference.child("Comments")
            .child(Comm.selectedFood!!.id!!).push().setValue(comment).addOnCompleteListener({
                if (!it.isSuccessful){
                    isSuccessFully.postValue(false)
                    return@addOnCompleteListener
                }
                    isSuccessFully.postValue(true)
            })

    }

    fun insertIntoDatabase(cart: Cart):Completable{
        return myDatabase.cartDao().insertOrReplaceCartItems(cart)
    }

}