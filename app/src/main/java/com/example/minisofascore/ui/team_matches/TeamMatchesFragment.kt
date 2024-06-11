@file:Suppress("deprecation")
package com.example.minisofascore.ui.team_matches

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.R
import com.example.minisofascore.TeamDetailsActivity
import com.example.minisofascore.TournamentActivity
import com.example.minisofascore.data.models.Team
import com.example.minisofascore.databinding.FragmentTeamMatchesBinding
import com.example.minisofascore.ui.main_list.EVENT_INFO
import com.example.minisofascore.ui.team_matches.adapters.TournamentHeaderDecorator
import com.example.minisofascore.ui.team_matches.adapters.TeamPagingAdapter
import com.example.minisofascore.ui.tournament_matches.adapters.EventsLoadStateAdapter
import kotlinx.coroutines.launch

class TeamMatchesFragment : Fragment() {

    companion object {
        fun newInstance(team: Team) = TeamMatchesFragment().apply {
            arguments = Bundle().apply {
                putSerializable(TeamDetailsActivity.TEAM_DETAILS, team)
            }
        }
    }

    private val matchesViewModel: TeamMatchesViewModel by viewModels()

    private var _binding: FragmentTeamMatchesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamMatchesBinding.inflate(layoutInflater, container, false)

        val team = requireArguments().getSerializable(TeamDetailsActivity.TEAM_DETAILS) as Team

        val pagingAdapter = TeamPagingAdapter(requireContext(),
            onEventClick = {
                findNavController().navigate(
                    R.id.action_fragmentTeamViewPager_to_fragmentEventDetails,
                    Bundle().apply {
                        putSerializable(EVENT_INFO, it)
                    }
                )
            },
            onTournamentClick = {
                startActivity(TournamentActivity.newInstance(requireContext(), it))
            })

        binding.recyclerView.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = EventsLoadStateAdapter{ pagingAdapter.retry() },
                footer = EventsLoadStateAdapter{ pagingAdapter.retry() }
            )
        }

        val stickyHeaderItemDecorator = TournamentHeaderDecorator()
        binding.recyclerView.let {
            stickyHeaderItemDecorator.attachRecyclerView(
                pagingAdapter,
                it,
                pagingAdapter
            )

        }

        lifecycleScope.launch {
            matchesViewModel.getEventPageFlow(team.id).collect{ pagingData ->
                pagingAdapter.submitData(pagingData)
                stickyHeaderItemDecorator.refreshHeader()
            }
        }

        binding.retryButton.setOnClickListener {
            pagingAdapter.retry()
        }

        lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collect { loadState ->
                // initial loading of list, before any page is loaded
                val isListEmpty = loadState.refresh is LoadState.NotLoading && pagingAdapter.itemCount == 0
                binding.emptyList.isVisible = isListEmpty
                binding.recyclerView.isVisible = !isListEmpty
                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error
            }
        }
        return binding.root
    }
}