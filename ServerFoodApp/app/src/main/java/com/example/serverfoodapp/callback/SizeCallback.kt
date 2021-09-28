package com.example.serverfoodapp.callback

import android.view.View
import com.example.serverfoodapp.model.Size

interface SizeCallback {
    fun sizeItem(size: Size?, pos: Int)
    fun deleteSize(size: Size?, pos: Int, view: View?)
}