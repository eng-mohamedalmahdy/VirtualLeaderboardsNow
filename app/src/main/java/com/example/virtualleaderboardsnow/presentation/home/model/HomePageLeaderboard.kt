package com.example.virtualleaderboardsnow.presentation.home.model

data class HomePageLeaderboard(
    val id: String,
    val leaderboardName: String,
    val heroesCount: Int,
    val totalScore: Int,
    val announcementsCount: Int,
    val adminsIds: List<String>
)
