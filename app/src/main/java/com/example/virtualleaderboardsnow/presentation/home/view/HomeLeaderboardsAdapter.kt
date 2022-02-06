package com.example.virtualleaderboardsnow.presentation.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.virtualleaderboardsnow.R
import com.example.virtualleaderboardsnow.databinding.ListItemLeaderboardBinding
import com.example.virtualleaderboardsnow.presentation.home.model.HomePageLeaderboard

class HomeLeaderboardsAdapter(
    private val leaderboards: List<HomePageLeaderboard>,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<HomeLeaderboardsAdapter.ViewHolder>() {

    val ADMIN_ID = "test"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        DataBindingUtil.bind<ListItemLeaderboardBinding>(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_leaderboard, parent, false)
        )?.let {
            ViewHolder(
                it
            )
        }!!

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(leaderboards[position])

    override fun getItemCount() = leaderboards.size

    inner class ViewHolder(private val binding: ListItemLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(homePageLeaderboard: HomePageLeaderboard) {
            with(binding) {
                leaderboardTitle.text = homePageLeaderboard.leaderboardName
                announcementsCount.text = homePageLeaderboard.announcementsCount.toString()
                scoreCount.text = homePageLeaderboard.totalScore.toString()
                heroesCount.text = homePageLeaderboard.heroesCount.toString()
                itemView.setOnClickListener {
                    fragment.findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToLeaderboardDetailsFragment(
                            homePageLeaderboard.adminsIds.contains(ADMIN_ID),
                            homePageLeaderboard.id
                        )
                    )
                }
            }
        }
    }
}