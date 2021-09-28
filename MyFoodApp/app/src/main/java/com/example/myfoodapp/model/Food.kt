package com.example.myfoodapp.model

class Food {

    var addon: List<Addon>? = null
    var description: String? = null
    var id: String? = null
    var image: String? = null
    var name: String? = null
    var price: Double? = null
    var size: List<Size>? = null

    constructor()
    constructor(
        addon: List<Addon>?,
        description: String?,
        id: String?,
        image: String?,
        name: String?,
        price: Double?,
        size: List<Size>?
    ) {
        this.addon = addon
        this.description = description
        this.id = id
        this.image = image
        this.name = name
        this.price = price
        this.size = size
    }


}