package com.example.serverfoodapp.ui.size

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.serverfoodapp.common.Comm
import com.example.serverfoodapp.model.Size
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class SizeViewModel(application: Application) : AndroidViewModel(application) {

    val mutableLiveData: MutableLiveData<List<Size>> = MutableLiveData()

    init {
        getSize()
    }

    private fun getSize() {
        if (Comm.selectedFood!!.size!=null){
            mutableLiveData.postValue(Comm.selectedFood!!.size)
        }else{
            FirebaseDatabase.getInstance().reference.child("Category")
                .child(Comm.selectedMenu!!.key!!).child("foods").child(Comm.selectedFood!!.key!!).child("size").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val sizeList = ArrayList<Size>()
                        for (dataSnapshot in snapshot.children) {
                            val addon = dataSnapshot.getValue(Size::class.java)
                            sizeList.add(addon!!)
                        }
                        mutableLiveData.postValue(sizeList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(getApplication(), error.message, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}