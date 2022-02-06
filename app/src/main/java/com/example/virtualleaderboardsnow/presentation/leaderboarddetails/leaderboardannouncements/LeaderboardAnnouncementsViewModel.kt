package com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardannouncements

import androidx.lifecycle.ViewModel
import com.example.virtualleaderboardsnow.domain.LeaderboardDetailsRepository
import kotlinx.coroutines.flow.Flow

class LeaderboardAnnouncementsViewModel(private val repository: LeaderboardDetailsRepository) :
    ViewModel() {
    fun getLeaderBoardAnnouncements(boardId: String): Flow<List<Announcement>> =
        repository.getLeaderboardAnnouncements(boardId)
}