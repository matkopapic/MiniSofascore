package com.example.minisofascore.ui.team_matches

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.R
import com.example.minisofascore.TeamDetailsActivity
import com.example.minisofascore.TournamentActivity
import com.example.minisofascore.data.models.Team
import com.example.minisofascore.databinding.FragmentTeamMatchesBinding
import com.example.minisofascore.ui.main_list.EVENT_INFO
import com.example.minisofascore.ui.team_details.TeamDetailsViewModel
import com.example.minisofascore.ui.team_matches.adapters.StickyHeaderDecorator
import com.example.minisofascore.ui.team_matches.adapters.TeamPagingAdapter
import com.example.minisofascore.ui.team_standings.TeamStandingsFragment
import com.example.minisofascore.ui.tournament_matches.adapters.StickyHeaderItemDecorator
import kotlinx.coroutines.launch

class TeamMatchesFragment : Fragment() {

    companion object {
        fun newInstance(team: Team) = TeamMatchesFragment().apply {
            arguments = Bundle().apply {
                putSerializable(TeamDetailsActivity.TEAM_DETAILS, team)
            }
        }
    }

    private val viewModel: TeamDetailsViewModel by activityViewModels()
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

        val stickyHeaderItemDecorator = StickyHeaderDecorator()
//        binding.recyclerView.apply {
//            stickyHeaderItemDecorator.attachRecyclerView(
//                listener = pagingAdapter,
//                recyclerView = this,
//                adapter = pagingAdapter
//            )
//        }

        binding.recyclerView.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pagingAdapter
        }

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
        return binding.root
    }
}