package com.example.serverfoodapp.common

import com.example.serverfoodapp.model.Food
import com.example.serverfoodapp.model.Menu
import com.example.serverfoodapp.model.User
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

    fun setStatusInfo(orderStatus: String?): Double {
        when(orderStatus){
            "Placed"-> return 0.0
            "Shipping"-> return 1.0
            "Shipped"-> return 2.0
            "Cancelled"-> return -1.0
        }
        return -2.0
    }
    fun setStatusInfo(orderStatus: Status?): Double {
        when(orderStatus){
            Status.PLACED-> return 0.0
            Status.SHIPPING-> return 1.0
            Status.SHIPPED-> return 2.0
            Status.CANCELD-> return -1.0
        }
        return -2.0
    }

    enum class Status{
        PLACED, SHIPPING , SHIPPED , CANCELD
    }

    var currentUser: User? = null
    var selectedMenu: Menu? = null
    var selectedFood: Food?=null

}