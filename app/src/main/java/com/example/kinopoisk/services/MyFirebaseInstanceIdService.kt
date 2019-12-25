package com.example.kinopoisk.services

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {

    val TAG = "PushNotifService"
    lateinit var name: String

    override fun onTokenRefresh() {
        val token = FirebaseInstanceId.getInstance().token

        Log.d(TAG, "Token : ${token}")


    }

}