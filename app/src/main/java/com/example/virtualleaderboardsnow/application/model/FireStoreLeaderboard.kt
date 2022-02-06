package com.example.virtualleaderboardsnow.application.model

import com.google.firebase.firestore.PropertyName

data class FireStoreLeaderboard(
    @PropertyName("Heroes")
    var Heroes: List<FireStoreHero?>? = null,
    @PropertyName("announcement")
    var announcements: List<FireStoreAnnouncement?>? = null,
    var adminId: List<String?>? = null,
    var id: String? = null,
    var name: String? = null,
) {
    val announcementsCount: Int
        get() = 0

    val score: Int?
        get() = Heroes?.sumOf { it?.score ?: 0 }

    val heroesCount: Int?
        get() = Heroes?.size


}
