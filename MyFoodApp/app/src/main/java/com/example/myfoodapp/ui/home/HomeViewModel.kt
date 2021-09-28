package com.example.myfoodapp.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myfoodapp.model.Category
import com.example.myfoodapp.model.Menu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    var allMenu:MutableLiveData<List<Menu>> = MutableLiveData()

    init {
//        FirebaseDatabase.getInstance().reference.child(".info/serverTimeOffset").addListenerForSingleValueEvent(
//            object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val time=System.currentTimeMillis().toString()+ snapshot.getValue(Long::class.java)!!
//                    Log.d("ddd1",time.toString())
//                    var simpleDateFormat=SimpleDateFormat("MM-dd-yyyy HH:mm")
//                    var date=Date(time)
//                    Log.d("ddd1",simpleDateFormat.format(date))
//
//                }
//            })

        // the above code check it later , i think it is for security

        FirebaseDatabase.getInstance().reference.child("Category").addValueEventListener(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val arrayList=ArrayList<Menu>()
                for(snapshopt in snapshot.children){
                    var menu=snapshopt.getValue(Menu::class.java)
//                    Log.d("ddd", snapshopt.key!!)
                    menu!!.key=snapshopt.key
                    arrayList.add(menu!!)
                }
                allMenu.postValue(arrayList)
//                val category=snapshot.getValue(Category::class.java)
//                allMenu.postValue(category!!.listOfMenu)
                Log.d("ddd",snapshot.childrenCount.toString())


            }
            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

}