@file:Suppress("deprecation")
package com.example.minisofascore.ui.team_standings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.TeamDetailsActivity
import com.example.minisofascore.data.models.SportType
import com.example.minisofascore.data.models.Team
import com.example.minisofascore.data.models.getSportType
import com.example.minisofascore.databinding.FragmentTeamStandingsBinding
import com.example.minisofascore.ui.team_details.TeamDetailsViewModel
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

    private val viewModel: TeamDetailsViewModel by activityViewModels()
    private val standingsViewModel: TeamStandingsViewModel by viewModels()
    private var _binding: FragmentTeamStandingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamStandingsBinding.inflate(layoutInflater, container, false)

        val team = requireArguments().getSerializable(TeamDetailsActivity.TEAM_DETAILS) as Team

        var teamSport = SportType.FOOTBALL

        viewModel.teamDetails.observe(viewLifecycleOwner){

            binding.tournamentSpinner.adapter = TournamentSpinnerAdapter(requireContext(), it.tournaments)
            if (it.tournaments.isNotEmpty()) {
                teamSport = it.tournaments[0].sport.getSportType()
            }
            binding.tournamentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    standingsViewModel.getStandingsForTournament(it.tournaments[position].id)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        }


        standingsViewModel.standings.observe(viewLifecycleOwner){
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = TournamentStandingsAdapter(it, teamSport, team) {
                    val intent = TeamDetailsActivity.newInstance(requireContext(), it)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
            }
        }


        return binding.root
    }
}