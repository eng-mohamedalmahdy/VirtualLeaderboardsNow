package com.example.virtualleaderboardsnow.domain

import com.example.virtualleaderboardsnow.application.FireStoreAPI
import com.example.virtualleaderboardsnow.presentation.home.model.HomePageLeaderboard
import com.example.virtualleaderboardsnow.domain.adapters.toHomePageLeaderboard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class HomePageRepository(private val source: FireStoreAPI) {
    fun getUserLeaderboardsList(): Flow<List<HomePageLeaderboard>> =
        source.getUserLeaderboardsList()
            .map { it.map(::toHomePageLeaderboard) }

    fun searchLeaderboard(name: String): StateFlow<List<HomePageLeaderboard>> {
        return MutableStateFlow(listOf())
    }

    fun addLeaderboard(boardName: String) = source.addLeaderboard(boardName)
}