package com.example.virtualleaderboardsnow.domain

import com.example.virtualleaderboardsnow.application.FireStoreAPI
import com.example.virtualleaderboardsnow.domain.adapters.toLeaderboardDetailsAnnouncement
import com.example.virtualleaderboardsnow.domain.adapters.toLeaderboardDetailsHero
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardannouncements.Announcement
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardheroes.Hero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LeaderboardDetailsRepository(private val source: FireStoreAPI) {
    fun getLeaderboardHeroes(boardId: String): Flow<List<Hero>> =
        source.getLeaderBoardHeroes(boardId)
            .map { it.map(::toLeaderboardDetailsHero) }

    fun getLeaderboardAnnouncements(boardId: String): Flow<List<Announcement>> =
        source.getLeaderboardAnnouncements(boardId)
            .map { it.map(::toLeaderboardDetailsAnnouncement) }

    fun addHero(boardId: String, heroName: String) = source.addHero(boardId, heroName)
}