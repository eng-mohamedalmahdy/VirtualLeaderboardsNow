package com.example.virtualleaderboardsnow.presentation.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.virtualleaderboardsnow.databinding.FragmentHomeBinding
import com.example.virtualleaderboardsnow.domain.showDialog
import com.example.virtualleaderboardsnow.domain.textFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.ext.android.viewModel


@ExperimentalCoroutinesApi
@FlowPreview
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewLifecycleOwner.lifecycleScope) {
            launchWhenCreated {
                viewModel.displayedLeaderBoards.map {
                    HomeLeaderboardsAdapter(it, this@HomeFragment)
                }.collect(binding.leaderboardsList::setAdapter)
            }

            launchWhenCreated {
                binding.leaderboardSearch.textFlow.debounce(500)
                    .collectLatest(viewModel::searchLeaderBoard)
            }
            binding.addLeaderboard.setOnClickListener {
                requireContext().showDialog("Add Leaderboard", viewModel::addLeaderboard)
            }
        }

    }

}