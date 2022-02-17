package com.example.virtualleaderboardsnow.application.model

import com.google.firebase.firestore.PropertyName

data class FireStoreHero(
    var id: String? = "0",

    @PropertyName("name")
    var name: String? = null,

    @PropertyName("score")
    var score: Int = 0
) {
    constructor(map: Map<String, Any>) : this(
        map["id"] as String,
        map["name"] as String,
        (map["score"] as Long).toInt()
    )
}