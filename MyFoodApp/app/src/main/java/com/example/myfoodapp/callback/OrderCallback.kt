package com.example.myfoodapp.callback

interface OrderCallback {
    fun success(isSuccessful: Boolean)
    fun failed(onError: Throwable)
}