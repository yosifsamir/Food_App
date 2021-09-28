package com.example.serverfoodapp.ui.addon

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.serverfoodapp.common.Comm
import com.example.serverfoodapp.model.Addon
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class AddonViewModel(application: Application) : AndroidViewModel(application) {
    val mutableLiveData:MutableLiveData<List<Addon>> = MutableLiveData()

    init {
        getAddon()
    }

    private fun getAddon() {
        if (Comm.selectedFood!!.addon!=null){
            mutableLiveData.postValue(Comm.selectedFood!!.addon)
        }else{
        FirebaseDatabase.getInstance().reference.child("Category")
            .child(Comm.selectedMenu!!.key!!).child("foods").child(Comm.selectedFood!!.key!!).child("addon").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val addonList = ArrayList<Addon>()
                    for (dataSnapshot in snapshot.children) {
                        val addon = dataSnapshot.getValue(Addon::class.java)
                        addonList.add(addon!!)
                    }
                    mutableLiveData.postValue(addonList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(getApplication(), error.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}