package com.example.myfoodapp.model

class Size {

    var name: String? = null
    var price: Double? = null

        constructor()
        constructor(name: String?, price: Double?) {
            this.name = name
            this.price = price
        }
}