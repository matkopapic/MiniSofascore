package com.example.minisofascore.ui.team_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.minisofascore.R
import com.example.minisofascore.TeamDetailsActivity
import com.example.minisofascore.TournamentActivity
import com.example.minisofascore.data.models.Team
import com.example.minisofascore.data.models.TeamDetails
import com.example.minisofascore.databinding.EventItemLayoutBinding
import com.example.minisofascore.databinding.FragmentTeamDetailsBinding
import com.example.minisofascore.databinding.TournamentHeaderLayoutBinding
import com.example.minisofascore.databinding.TournamentItemLayoutBinding
import com.example.minisofascore.ui.main_list.EVENT_INFO
import com.example.minisofascore.ui.main_list.adapters.EventAdapter
import com.example.minisofascore.util.loadFlag
import com.example.minisofascore.util.loadTournamentLogo

class TeamDetailsFragment : Fragment() {

    companion object {
        fun newInstance(team: Team) = TeamDetailsFragment().apply {
            arguments = Bundle().apply {
                putSerializable(TeamDetailsActivity.TEAM_DETAILS, team)
            }
        }
    }

    private var _binding: FragmentTeamDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TeamDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamDetailsBinding.inflate(inflater, container, false)

        val team = requireArguments().getSerializable(TeamDetailsActivity.TEAM_DETAILS) as Team

        viewModel.getTeamDetails(team.id)

        viewModel.teamDetails.observe(viewLifecycleOwner) {
            populateTeamDetails(it)
        }




        return binding.root
    }

    private fun populateTeamDetails(details: TeamDetails) {
        val coachText = "${requireContext().getString(R.string.coach)}: ${details.team.managerName ?: ""}"
        binding.coachLayout.playerName.text = coachText
        binding.coachLayout.playerCountryName.text = details.team.country.name
        binding.coachLayout.playerCountryLogo.loadFlag(details.team.country.name)
        val totalPlayers = details.players.size
        binding.totalPlayers.text = totalPlayers.toString()
        val foreignPlayers = details.players.count { it.country.id != details.team.country.id }
        binding.foreignPlayersGraph.setProgress((100f * foreignPlayers / totalPlayers).toInt())
        binding.foreignPlayers.text = foreignPlayers.toString()

        binding.teamTournamentsLayout.removeAllViews()
        details.tournament.forEach { tournament ->
            val tournamentItemLayout = TournamentItemLayoutBinding.inflate(layoutInflater)
            tournamentItemLayout.tournamentLogo.loadTournamentLogo(tournament.id)
            tournamentItemLayout.tournamentName.text = tournament.name
            binding.teamTournamentsLayout.addView(tournamentItemLayout.root)
            tournamentItemLayout.root.layoutParams.width = binding.teamTournamentsLayout.width / 3
        }
        val nextMatch = details.nextMatches.sortedBy { it.startDate }[0]

        binding.venueName.text = details.team.venue ?: ""

        binding.nextMatchLayout.removeAllViews()

        val tournamentView = TournamentHeaderLayoutBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        val tournamentViewHolder = EventAdapter.TournamentHeaderViewHolder(tournamentView){
            val intent = TournamentActivity.newInstance(requireContext(), nextMatch.tournament)
            startActivity(intent)
        }
        tournamentViewHolder.bind(nextMatch.tournament)
        binding.nextMatchLayout.addView(tournamentView.root)

        val eventView = EventItemLayoutBinding.inflate(LayoutInflater.from(context), null, false)
        val eventViewHolder = EventAdapter.EventInfoViewHolder(eventView, requireContext()) {
            findNavController().navigate(R.id.action_fragmentTeamViewPager_to_fragmentEventDetails, Bundle().apply {
                putSerializable(EVENT_INFO, nextMatch)
            })
        }
        eventViewHolder.bind(nextMatch)
        binding.nextMatchLayout.addView(eventView.root)



    }
}