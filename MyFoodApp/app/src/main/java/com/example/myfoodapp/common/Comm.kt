package com.example.myfoodapp.common

import com.example.myfoodapp.model.Food
import com.example.myfoodapp.model.Menu
import com.example.myfoodapp.model.User
import java.lang.StringBuilder
import java.util.*

object Comm {
    fun getTimeAndRandom(): String {
        return StringBuilder()
            .append(System.currentTimeMillis())
            .append(Math.abs(Random().nextInt()))
            .toString()
    }

    fun getDateOfWeeks(numberOfDay: Int): String {
        when(numberOfDay){
            1-> return "Monday"
            2-> return "Tuesday"
            3-> return "Wendesday"
            4-> return "Thursday"
            5-> return "Friday"
            6-> return "Saturday"
            7-> return "Sunday"
        }
        return "Unk"
    }

    fun getStatusInfo(orderStatus: Int?): String {
        when(orderStatus){
            0-> return "Placed"
            1-> return "Shipping"
            2-> return "Shipped"
            -1-> return "Cancelled"
        }
        return "Unk"
    }

    var currentUser: User? = null
    var selectedMenu: Menu? = null
    var selectedFood:Food?=null

}