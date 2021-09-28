package com.example.myfoodapp.fcm

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseInstanceServiceId : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        Log.d("tokens", FirebaseInstanceId.getInstance().getToken()!!)
    }

}