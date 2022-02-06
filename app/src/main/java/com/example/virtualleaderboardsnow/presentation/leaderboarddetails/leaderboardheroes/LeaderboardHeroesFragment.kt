package com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardheroes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.virtualleaderboardsnow.databinding.FragmentLeaderboardHeroesBinding
import com.example.virtualleaderboardsnow.domain.showDialog
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.FragmentsArgs.IS_BOARD_ADMIN
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.FragmentsArgs.LEADERBOARD_ID
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.ext.android.viewModel


class LeaderboardHeroesFragment : Fragment() {

    private lateinit var binding: FragmentLeaderboardHeroesBinding
    private val viewModel: LeaderboardHeroesViewModel by viewModel()
    private val boardId by lazy { requireArguments().getString(LEADERBOARD_ID) ?: "" }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderboardHeroesBinding.inflate(inflater)
        val isAdmin = requireArguments().getBoolean(IS_BOARD_ADMIN)
        binding.addHero.visibility = if (isAdmin) View.VISIBLE else View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewLifecycleOwner.lifecycleScope) {
            launchWhenCreated {
                viewModel.getLeaderBoardHeroes(boardId)
                    .map(::LeaderboardHeroesAdapter)
                    .collect(binding.heroesList::setAdapter)
            }
            binding.addHero.setOnClickListener {
                requireContext().showDialog("Add Hero") {
                    viewModel.addHero(boardId, it)
                }
            }
        }
    }
}