package com.example.serverfoodapp.callback

import android.view.View
import com.example.serverfoodapp.model.Addon

interface AddonCallback {
    fun AddonItem(food: Addon?, pos: Int)
    fun deleteAdodon(addon: Addon?, pos: Int, view: View?)
}