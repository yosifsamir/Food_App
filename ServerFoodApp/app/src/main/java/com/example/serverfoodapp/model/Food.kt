package com.example.serverfoodapp.model
class Food {

    var key:String? = null
    var addon: List<Addon>? = null
    var description: String? = null
    var id: String? = null
    var image: String? = null
    var name: String? = null
    var price: Double? = null
    var size: List<Size>? = null

    constructor()
    constructor(
        key: String?,
        addon: List<Addon>?,
        description: String?,
        id: String?,
        image: String?,
        name: String?,
        price: Double?,
        size: List<Size>?
    ) {
        this.key = key
        this.addon = addon
        this.description = description
        this.id = id
        this.image = image
        this.name = name
        this.price = price
        this.size = size
    }


}