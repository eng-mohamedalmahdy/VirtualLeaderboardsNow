package com.example.virtualleaderboardsnow.presentation.home.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtualleaderboardsnow.domain.HomePageRepository
import com.example.virtualleaderboardsnow.presentation.home.model.HomePageLeaderboard
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel(private val repository: HomePageRepository) : ViewModel() {


    private val _displayedLeaderBoards: MutableStateFlow<List<HomePageLeaderboard>> =
        MutableStateFlow(listOf())
    val displayedLeaderBoards: StateFlow<List<HomePageLeaderboard>> =
        _displayedLeaderBoards.asStateFlow()

    init {
        viewModelScope.launch {
            getLeaderboardsList().collect(_displayedLeaderBoards::emit)
        }
    }

    private fun getLeaderboardsList(): Flow<List<HomePageLeaderboard>> =
        repository.getUserLeaderboardsList()

    suspend fun searchLeaderBoard(name: String) {
        Log.d(TAG, "searchLeaderBoard: reloading $name")
        if (name.isEmpty() || name.isBlank()) {
            getLeaderboardsList().collect(_displayedLeaderBoards::emit)
        } else {
            repository.searchLeaderboard(name).collect(_displayedLeaderBoards::emit)
        }
    }

    fun addLeaderboard(boardName: String) = repository.addLeaderboard(boardName)
}