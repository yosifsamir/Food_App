package com.example.myfoodapp.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "Cart",primaryKeys = ["foodId","uid","foodAddon","foodSize"])
class Cart {
    @NonNull
    @ColumnInfo(name = "foodId")
    var foodId:String =""
    @ColumnInfo(name = "foodName")
    var foodName:String ? =null
    @ColumnInfo(name = "foodImg")
    var foodImg:String ? =null
    @ColumnInfo(name = "foodPrice")
    var foodPrice:Double ? =null
    @ColumnInfo(name = "foodQuantity")
    var foodQuantity:Int ? =null
    @ColumnInfo(name = "userPhone")
    var userPhone:String ? =null
    @ColumnInfo(name = "foodExtraPrice")
    var foodExtraPrice:Double ?=null
    @NonNull
    @ColumnInfo(name = "foodAddon")
    var foodAddon:String =""
    @NonNull
    @ColumnInfo(name = "foodSize")
    var foodSize:String =""
    @NonNull
    @ColumnInfo(name = "uid")
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