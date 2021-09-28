package com.example.myfoodapp.model

class Comments {
    var comment:String ? =null
    var userName:String ? =null  // remember firebase allows duplicate data rather than relational database.
    var uid:String ? =null
    var timestamp:Map<String,Any> ? =null

    constructor(comment: String?, userName: String?, uid: String?, timestamp: Map<String,Any>?) {
        this.comment = comment
        this.userName = userName
        this.uid = uid
        this.timestamp = timestamp
    }

    constructor()
}