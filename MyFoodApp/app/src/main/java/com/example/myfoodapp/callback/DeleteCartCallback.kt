package com.example.myfoodapp.callback

interface DeleteCartCallback {
    fun success(onNext: Int)
    fun failed(onError: Throwable)
}