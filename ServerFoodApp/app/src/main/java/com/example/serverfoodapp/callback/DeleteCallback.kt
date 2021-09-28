package com.example.serverfoodapp.callback

import com.google.android.gms.tasks.Task
import java.lang.Exception

interface DeleteCallback {
    fun onSuccess(task: Task<Void>)
    fun onFailed(exception: Exception)
}