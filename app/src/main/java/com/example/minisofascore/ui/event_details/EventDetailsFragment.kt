@file:Suppress("DEPRECATION")
package com.example.minisofascore.ui.event_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.minisofascore.MainActivity
import com.example.minisofascore.R
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.EventStatus
import com.example.minisofascore.data.models.Sport
import com.example.minisofascore.data.models.TeamSide
import com.example.minisofascore.data.repository.Repository
import com.example.minisofascore.databinding.FragmentEventDetailsBinding
import com.example.minisofascore.ui.main_list.EVENT_INFO
import com.example.minisofascore.ui.main_list.SPORT_INFO
import com.example.minisofascore.ui.main_list.adapters.getTotalAsString
import com.google.android.material.color.MaterialColors
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class EventDetailsFragment : Fragment() {

    private var _binding: FragmentEventDetailsBinding? = null
    private val eventDetailsViewModel by viewModels<EventDetailsViewModel>()

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false)

        val dateFormat = "dd.MM.yyyy."
        val event = (requireArguments().getSerializable(EVENT_INFO)) as Event
        val sport = (requireArguments().getSerializable(SPORT_INFO)) as Sport
        val sportType = MainActivity.getSportTypeFromSport(sport)

        val incidentAdapter = IncidentAdapter(requireContext(), sportType, event.status == EventStatus.IN_PROGRESS)

        binding.incidentRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = incidentAdapter
        }

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }



        binding.tournamentLogo.load(Repository.getTournamentLogoUrl(event.tournament.id))
        val tournamentRoundText = "${sport.name}, ${event.tournament.country.name}, ${event.tournament.name}, ${getString(R.string.round)} ${event.round}"
        binding.tournamentRoundText.text = tournamentRoundText

        binding.teamHomeName.text = event.homeTeam.name
        binding.teamAwayName.text = event.awayTeam.name

        binding.teamHomeLogo.load(Repository.getTeamLogoUrl(event.homeTeam.id))
        binding.teamAwayLogo.load(Repository.getTeamLogoUrl(event.awayTeam.id))


        val winnerColor = MaterialColors.getColor(binding.root, R.attr.on_surface_lv1)
        val timeFinishedColor = MaterialColors.getColor(binding.root, R.attr.on_surface_lv2)
        val liveColor = MaterialColors.getColor(binding.root, R.attr.live)

        val startDateTime = Instant.ofEpochMilli(event.startDate.time).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val startDate = startDateTime.format(DateTimeFormatter.ofPattern(dateFormat))
        val startTime = startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        when(event.status) {
            EventStatus.NOT_STARTED -> {
                binding.startDate.text = startDate
                binding.startTime.text = startTime
                binding.scoreLayout.visibility = View.GONE
                binding.startDate.visibility = View.VISIBLE

                binding.incidentRecyclerView.visibility = View.GONE
                binding.notStartedLayout.visibility = View.VISIBLE
            }
            EventStatus.IN_PROGRESS -> {
                binding.startDate.visibility = View.GONE
                binding.scoreLayout.visibility = View.VISIBLE

                binding.notStartedLayout.visibility = View.GONE

                binding.teamHomeScore.text = event.homeScore.getTotalAsString()
                binding.teamAwayScore.text = event.awayScore.getTotalAsString()

                binding.teamHomeScore.setTextColor(liveColor)
                binding.teamAwayScore.setTextColor(liveColor)
                binding.minus.setTextColor(liveColor)

                val elapsedMinutesString = "${ChronoUnit.MINUTES.between(startDateTime, LocalDateTime.now())}'"
                binding.startTime.text = elapsedMinutesString
                binding.startTime.setTextColor(liveColor)
            }
            EventStatus.FINISHED -> {
                binding.startDate.visibility = View.GONE
                binding.scoreLayout.visibility = View.VISIBLE

                binding.notStartedLayout.visibility = View.GONE

                binding.teamHomeScore.text = event.homeScore.getTotalAsString()
                binding.teamAwayScore.text = event.awayScore.getTotalAsString()

                binding.startTime.text = getString(R.string.full_time)
                binding.startTime.setTextColor(timeFinishedColor)
                if (event.winnerCode == TeamSide.HOME) {
                    binding.teamHomeScore.setTextColor(winnerColor)
                }
                if (event.winnerCode == TeamSide.AWAY) {
                    binding.teamAwayScore.setTextColor(winnerColor)
                }
            }
        }

        eventDetailsViewModel.getIncidents(1)
        eventDetailsViewModel.incidents.observe(viewLifecycleOwner){
            incidentAdapter.updateItems(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}