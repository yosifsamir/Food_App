package com.example.myfoodapp.model

class Menu {
    var key:String? = null
    var name:String ? =null
    var image:String ? =null
    var foods:List<Food> ? =null




    constructor()
    constructor(key: String?, name: String?, image: String?, foods: List<Food>?) {
        this.key = key
        this.name = name
        this.image = image
        this.foods = foods
    }
}