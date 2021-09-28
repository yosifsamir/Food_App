package com.example.myfoodapp.model

class User {
    var name: String? = null
    var email: String? = null
    var phone: String? = null
//    var phone: String? = null

    constructor() {}
    constructor(name: String?, email: String?) {
        this.name = name
        this.email = email
//        this.phone = phone
    }

}
