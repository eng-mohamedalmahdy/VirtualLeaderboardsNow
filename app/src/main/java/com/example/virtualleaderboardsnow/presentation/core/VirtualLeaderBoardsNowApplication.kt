package com.example.virtualleaderboardsnow.presentation.core

import android.app.Application
import com.example.virtualleaderboardsnow.application.FireStoreAPI
import com.example.virtualleaderboardsnow.domain.HomePageRepository
import com.example.virtualleaderboardsnow.domain.LeaderboardDetailsRepository
import com.example.virtualleaderboardsnow.presentation.home.view.HomeViewModel
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardannouncements.LeaderboardAnnouncementsViewModel
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardannouncements.createannouncementdialog.CreateAnnouncementViewModel
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardheroes.LeaderboardHeroesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level.*
import org.koin.dsl.module

class VirtualLeaderBoardsNowApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@VirtualLeaderBoardsNowApplication)
            modules(appModule)
        }
    }
}

val appModule = module {

    single { FireStoreAPI() }

    //Home page
    single { HomePageRepository(get()) }
    viewModel { HomeViewModel(get()) }

    //LeaderboardDetails
    single { LeaderboardDetailsRepository(get()) }
    viewModel { LeaderboardAnnouncementsViewModel(get()) }
    viewModel { LeaderboardHeroesViewModel(get()) }
    viewModel { params -> CreateAnnouncementViewModel(get(), params.get()) }

}