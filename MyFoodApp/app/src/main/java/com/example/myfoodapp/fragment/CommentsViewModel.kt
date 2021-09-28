package com.example.myfoodapp.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myfoodapp.common.Comm
import com.example.myfoodapp.model.Comments
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommentsViewModel(application: Application) : AndroidViewModel(application) {
    var foodCommentsLivData: MutableLiveData<List<Comments>> = MutableLiveData()

    init {
        FirebaseDatabase.getInstance().reference.child("Comments")
            .child(Comm.selectedFood!!.id!!)
            .orderByChild("timestamp").limitToLast(100).addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            var arrayList=ArrayList<Comments>()
                            for (snapshot in snapshot.children){
                                arrayList.add(snapshot.getValue(Comments::class.java)!!)
                            }
                            foodCommentsLivData.postValue(arrayList)
                        }

                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                })
    }
}