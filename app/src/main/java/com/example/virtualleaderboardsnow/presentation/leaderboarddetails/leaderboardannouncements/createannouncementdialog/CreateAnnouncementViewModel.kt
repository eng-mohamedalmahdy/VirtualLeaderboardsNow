package com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardannouncements.createannouncementdialog

import androidx.lifecycle.ViewModel
import com.example.virtualleaderboardsnow.domain.LeaderboardDetailsRepository

class CreateAnnouncementViewModel(
    private val repository: LeaderboardDetailsRepository,
    private val boardId: String
) :
    ViewModel() {

    fun getAnnouncementConfigList() = repository.getAnnouncementConfigList(boardId)

    fun submitUpdates(title: String, configs: List<AnnouncementConfig>) =
        repository.submitUpdates(boardId,title, configs)
}