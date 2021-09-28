package com.example.serverfoodapp.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast


class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            if (isOnline(context!!)) {
//                dialog(true)
//                Log.e("keshav", "Online Connect Intenet ")
            } else {
                Toast.makeText(context,"Conectivity Failure",Toast.LENGTH_LONG).show()
                Log.e("networkStatus", "Conectivity Failure !!! ")
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun isOnline(context: Context): Boolean {
        return try {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            //should check null because in airplane mode it will be null
            netInfo != null && netInfo.isConnected
        } catch (e: java.lang.NullPointerException) {
            e.printStackTrace()
            false
        }
    }

}