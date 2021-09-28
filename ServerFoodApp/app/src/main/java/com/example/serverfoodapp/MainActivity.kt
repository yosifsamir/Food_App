package com.example.serverfoodapp

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.developer.kalert.KAlertDialog
import com.example.serverfoodapp.common.Comm
import com.example.serverfoodapp.common.NetworkChangeReceiver
import com.example.serverfoodapp.model.User
import com.example.serverfoodapp.ui.SignUpActivity
import com.example.serverfoodapp.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var handler:Handler
    private lateinit var networkChangeReceiver:NetworkChangeReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler=Handler(Looper.getMainLooper())

//        networkChangeReceiver= NetworkChangeReceiver()

        if (FirebaseAuth.getInstance().currentUser!=null){
//            Thread({
//                Thread.sleep(700)
//                handler.post({
//                    startActivity()
//                })
//            }).start()

            FirebaseDatabase.getInstance().reference.child("Server").child(FirebaseAuth.getInstance().uid!!).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Comm.currentUser=snapshot.getValue(User::class.java)
                        goHome()
                        finish()
                    }
                })
        }
        else{
            goToSignUp()
        }

    }



    private fun goToSignUp() {
        startActivity(Intent(this,SignUpActivity::class.java))
        finish()
    }

    private fun goHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
//        registerReceiver(networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(networkChangeReceiver)
    }

}