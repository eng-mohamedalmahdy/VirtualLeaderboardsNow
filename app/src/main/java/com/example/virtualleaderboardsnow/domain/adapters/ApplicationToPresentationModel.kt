package com.example.virtualleaderboardsnow.domain.adapters

import com.example.virtualleaderboardsnow.application.model.FireStoreAnnouncement
import com.example.virtualleaderboardsnow.application.model.FireStoreHero
import com.example.virtualleaderboardsnow.application.model.FireStoreLeaderboard
import com.example.virtualleaderboardsnow.presentation.home.model.HomePageLeaderboard
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardannouncements.Announcement
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardannouncements.createannouncementdialog.AnnouncementConfig
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardheroes.Hero

fun toHomePageLeaderboard(leaderboard: FireStoreLeaderboard) =
    HomePageLeaderboard(
        id = leaderboard.id ?: "",
        adminsIds = leaderboard.adminId?.filterNotNull() ?: listOf(),
        leaderboardName = leaderboard.name ?: "",
        heroesCount = leaderboard.heroesCount ?: 0,
        totalScore = leaderboard.score ?: 0,
        announcementsCount = leaderboard.announcementsCount ?: 0
    )

fun toLeaderboardDetailsHero(hero: FireStoreHero) =
    Hero(hero.id ?: "", hero.name ?: "", hero.score)

fun toLeaderboardDetailsAnnouncement(announcement: FireStoreAnnouncement) =
    Announcement(announcement.title ?: "", announcement.contributors)

fun toAnnouncementConfig(hero: FireStoreHero) =
    AnnouncementConfig(heroId = hero.id.orEmpty(), heroName = hero.name.orEmpty(), 0)