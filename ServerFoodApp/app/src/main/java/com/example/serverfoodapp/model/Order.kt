package com.example.serverfoodapp.model

class Order {
    var userId:String ? =null
    var userName:String ? =null
    var userPhone:String ? =null
    var shippingAddress:String ? =null
    var comment:String ? =null
    var transactionId:String ? =null

    var lat:Double ? =null
    var lng:Double ? =null
    var totalPayment:Double ? =null
    var finalPayment:Double ? =null

    var cod:Boolean ? =null
    var discount:Int ? =null

    var cartItemList:List<Cart> ? =null

    var orderNumber:String ? =null
    var orderStatus:Int ? =null
    var createdAt:Long ?=null

    constructor()

    constructor(
        userId: String?,
        userName: String?,
        userPhone: String?,
        shippingAddress: String?,
        comment: String?,
        transactionId: String?,
        lat: Double?,
        lng: Double?,
        totalPayment: Double?,
        finalPayment: Double?,
        cod: Boolean?,
        discount: Int?,
        cartItemList: List<Cart>?,
        orderNumber: String?,
        orderStatus: Int?,
        createdAt: Long?
    ) {
        this.userId = userId
        this.userName = userName
        this.userPhone = userPhone
        this.shippingAddress = shippingAddress
        this.comment = comment
        this.transactionId = transactionId
        this.lat = lat
        this.lng = lng
        this.totalPayment = totalPayment
        this.finalPayment = finalPayment
        this.cod = cod
        this.discount = discount
        this.cartItemList = cartItemList
        this.orderNumber = orderNumber
        this.orderStatus = orderStatus
        this.createdAt = createdAt
    }


}