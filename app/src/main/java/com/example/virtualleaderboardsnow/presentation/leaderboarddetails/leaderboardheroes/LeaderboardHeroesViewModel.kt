package com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardheroes

import androidx.lifecycle.ViewModel
import com.example.virtualleaderboardsnow.domain.LeaderboardDetailsRepository
import kotlinx.coroutines.flow.Flow

class LeaderboardHeroesViewModel(private val repository: LeaderboardDetailsRepository) :
    ViewModel() {
    fun getLeaderBoardHeroes(boardId: String): Flow<List<Hero>> =
        repository.getLeaderboardHeroes(boardId)

    fun addHero(boardId: String, heroName: String) = repository.addHero(boardId, heroName)
}