package com.example.serverfoodapp.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo

class Cart {
    
    var foodId:String =""
    var foodName:String ? =null
    var foodImg:String ? =null
    var foodPrice:Double ? =null
    var foodQuantity:Int ? =null
    var userPhone:String ? =null
    var foodExtraPrice:Double ?=null
    var foodAddon:String =""
    var foodSize:String =""
    var uid:String =""

    constructor()
    constructor(
        foodId: String,
        foodName: String?,
        foodImg: String?,
        foodPrice: Double?,
        foodQuantity: Int?,
        userPhone: String?,
        foodExtraPrice: Double?,
        foodAddon: String,
        foodSize: String,
        uid: String
    ) {
        this.foodId = foodId
        this.foodName = foodName
        this.foodImg = foodImg
        this.foodPrice = foodPrice
        this.foodQuantity = foodQuantity
        this.userPhone = userPhone
        this.foodExtraPrice = foodExtraPrice
        this.foodAddon = foodAddon
        this.foodSize = foodSize
        this.uid = uid
    }


}