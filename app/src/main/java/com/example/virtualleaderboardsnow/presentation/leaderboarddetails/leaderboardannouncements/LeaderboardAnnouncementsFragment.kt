package com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardannouncements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.virtualleaderboardsnow.R
import com.example.virtualleaderboardsnow.databinding.FragmentLeaderboardAnnouncementsBinding
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.FragmentsArgs
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.FragmentsArgs.IS_BOARD_ADMIN
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.LeaderboardDetailsFragmentDirections
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.ext.android.viewModel


class LeaderboardAnnouncementsFragment : Fragment() {

    private lateinit var binding: FragmentLeaderboardAnnouncementsBinding
    private val viewModel: LeaderboardAnnouncementsViewModel by viewModel()
    private val boardId by lazy { requireArguments().getString(FragmentsArgs.LEADERBOARD_ID) ?: "" }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderboardAnnouncementsBinding.inflate(inflater)
        requireArguments()[IS_BOARD_ADMIN]
        val isAdmin = requireArguments().getBoolean(IS_BOARD_ADMIN)
        binding.addAnnouncement.visibility = if (isAdmin) View.VISIBLE else View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewLifecycleOwner.lifecycleScope) {
            launchWhenCreated {
                viewModel.getLeaderBoardAnnouncements(boardId)
                    .map(::LeaderboardAnnouncementsAdapter)
                    .collect(binding.announcementsList::setAdapter)
            }
            binding.addAnnouncement.setOnClickListener {
                findNavController().navigate(
                    LeaderboardDetailsFragmentDirections.actionLeaderboardDetailsFragmentToCreateAnnouncementDialog(
                        boardId
                    )
                )
            }
        }
    }

}