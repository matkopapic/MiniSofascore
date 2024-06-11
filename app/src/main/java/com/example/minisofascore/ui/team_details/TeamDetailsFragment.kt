package com.example.minisofascore.ui.team_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.example.minisofascore.util.loadPlayerImage
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

    private val viewModel: TeamDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamDetailsBinding.inflate(inflater, container, false)

        viewModel.teamDetails.observe(viewLifecycleOwner) {
            populateTeamDetails(it)
        }

        return binding.root
    }

    private fun populateTeamDetails(details: TeamDetails) {
        val coachText = "${requireContext().getString(R.string.coach)}: ${details.team.managerName ?: ""}"
        binding.run {

            coachLayout.run {
                playerImage.loadPlayerImage(0)
                playerName.text = coachText
                playerCountryName.text = details.team.country.name
                playerCountryLogo.loadFlag(details.team.country.name)
            }
            val totalPlayersCount = details.players.size
            totalPlayers.text = totalPlayersCount.toString()

            val foreignPlayersCount = details.players.count { it.country.id != details.team.country.id }
            foreignPlayersGraph.setProgress((100f * foreignPlayersCount / totalPlayersCount).toInt())
            foreignPlayers.text = foreignPlayersCount.toString()
            foreignPlayersGraph.animate(1500)

            teamTournamentsLayout.removeAllViews()
            details.tournaments.forEach { tournament ->
                val tournamentItemLayout = TournamentItemLayoutBinding.inflate(layoutInflater)
                tournamentItemLayout.tournamentLogo.loadTournamentLogo(tournament.id)
                tournamentItemLayout.tournamentName.text = tournament.name
                teamTournamentsLayout.post {
                    teamTournamentsLayout.addView(tournamentItemLayout.root)
                    tournamentItemLayout.root.layoutParams.width = binding.teamTournamentsLayout.width / 3
                    tournamentItemLayout.root.setOnClickListener {
                        val intent = TournamentActivity.newInstance(requireContext(), tournament)
                        startActivity(intent)
                    }
                }

            }
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
            findNavController().navigate(
                R.id.action_fragmentTeamViewPager_to_fragmentEventDetails,
                Bundle().apply {
                    putSerializable(EVENT_INFO, nextMatch)
            })
        }
        eventViewHolder.bind(nextMatch)
        binding.nextMatchLayout.addView(eventView.root)
    }
}