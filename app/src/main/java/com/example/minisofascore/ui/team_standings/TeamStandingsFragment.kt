package com.example.minisofascore.ui.team_standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.TeamDetailsActivity
import com.example.minisofascore.data.models.SportType
import com.example.minisofascore.data.models.Team
import com.example.minisofascore.databinding.FragmentTeamStandingsBinding
import com.example.minisofascore.ui.team_standings.adapters.TournamentSpinnerAdapter
import com.example.minisofascore.ui.tournament_standings.adapters.TournamentStandingsAdapter

class TeamStandingsFragment : Fragment() {

    companion object {
        fun newInstance(team: Team) = TeamStandingsFragment().apply {
            arguments = Bundle().apply {
                putSerializable(TeamDetailsActivity.TEAM_DETAILS, team)
            }
        }
    }

    private val viewModel: TeamStandingsViewModel by viewModels()
    private var _binding: FragmentTeamStandingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamStandingsBinding.inflate(layoutInflater, container, false)

        val team = requireArguments().getSerializable(TeamDetailsActivity.TEAM_DETAILS) as Team

        viewModel.getStandingsForTournament(1)
        viewModel.getTeamDetails(team.id)

        viewModel.teamDetails.observe(viewLifecycleOwner){
            binding.tournamentSpinner.adapter = TournamentSpinnerAdapter(requireContext(), it.tournament)
        }


        viewModel.standings.observe(viewLifecycleOwner){
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = TournamentStandingsAdapter(it, SportType.FOOTBALL, team)
            }
        }


        return binding.root
    }
}