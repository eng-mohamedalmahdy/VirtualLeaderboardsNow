package com.example.virtualleaderboardsnow.presentation.leaderboarddetails


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.virtualleaderboardsnow.R
import com.example.virtualleaderboardsnow.databinding.FragmentLeaderboardDetailsBinding
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.FragmentsArgs.IS_BOARD_ADMIN
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.FragmentsArgs.LEADERBOARD_ID
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardannouncements.LeaderboardAnnouncementsFragment
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardheroes.LeaderboardHeroesFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class LeaderboardDetailsFragment : Fragment() {
    private lateinit var binding: FragmentLeaderboardDetailsBinding
    private val args by navArgs<LeaderboardDetailsFragmentArgs>()
    private val tabsText = listOf(R.string.heroes, R.string.announcementes)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderboardDetailsBinding.inflate(inflater)
        binding.pager.adapter = LeaderboardDetailsPagerAdapter(this)
        val tabLayout: TabLayout = binding.tabLayout
        TabLayoutMediator(
            tabLayout, binding.pager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = getString(tabsText[position % tabsText.size])
        }.attach()
        return binding.root
    }

    inner class LeaderboardDetailsPagerAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            // Return a NEW fragment instance in createFragment(int)
            val fragment: Fragment = when (position) {
                0 -> LeaderboardHeroesFragment()
                else -> LeaderboardAnnouncementsFragment()
            }
            fragment.arguments = Bundle().apply {
                putString(LEADERBOARD_ID, args.leaderboardId)
                putBoolean(IS_BOARD_ADMIN, args.isAdmin)
            }
            return fragment
        }
    }


}