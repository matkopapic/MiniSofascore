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
import com.example.minisofascore.R
import com.example.minisofascore.TournamentActivity
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.EventStatus
import com.example.minisofascore.data.models.SportType
import com.example.minisofascore.data.models.TeamSide
import com.example.minisofascore.databinding.FragmentEventDetailsBinding
import com.example.minisofascore.ui.main_list.EVENT_INFO
import com.example.minisofascore.ui.main_list.SPORT_TYPE_INFO
import com.example.minisofascore.ui.main_list.adapters.getTotalAsString
import com.example.minisofascore.util.getLocalDateTime
import com.example.minisofascore.util.loadTeamLogo
import com.example.minisofascore.util.loadTournamentLogo
import com.google.android.material.color.MaterialColors
import java.time.LocalDateTime
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

        val event = (requireArguments().getSerializable(EVENT_INFO)) as Event
        val sportType = (requireArguments().getSerializable(SPORT_TYPE_INFO)) as SportType

        val incidentAdapter = IncidentAdapter(requireContext(), sportType, event.status == EventStatus.IN_PROGRESS)

        binding.incidentRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = incidentAdapter
        }

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonViewTournamentDetails.setOnClickListener {
            if (activity is TournamentActivity) {
                findNavController().popBackStack()
            } else {
                val intent = TournamentActivity.newInstance(requireContext(), event.tournament)
                startActivity(intent)
            }

        }

        // we request EventStatus updates if the game is live or it's 5 minutes before startTime
        val startDateTime = event.startDate.getLocalDateTime()
        if (event.status == EventStatus.IN_PROGRESS ||
            (
                event.status == EventStatus.NOT_STARTED &&
                ChronoUnit.MINUTES.between(LocalDateTime.now(), startDateTime) < 5)
            ) {
            eventDetailsViewModel.startEventStatusUpdates(event.id)
            eventDetailsViewModel.eventStatus.observe(viewLifecycleOwner) {
                if (it != event.status) {
                    event.status = it
                    setupHeaderWithEvent(event)
                    incidentAdapter.refreshEventStatus(it)
                    eventDetailsViewModel.getIncidents(event.id)
                    if (it == EventStatus.FINISHED) eventDetailsViewModel.stopEventStatusUpdates()
                } else {
                    // game is live so we still want incident updates
                    eventDetailsViewModel.getIncidents(event.id)
                }
            }
        }

        binding.tournamentLogo.loadTournamentLogo(event.tournament.id)
        val tournamentRoundText = "${sportType.sportName}, ${event.tournament.country.name}, ${event.tournament.name}, ${getString(R.string.round)} ${event.round}"
        binding.tournamentRoundText.text = tournamentRoundText

        binding.teamHomeName.text = event.homeTeam.name
        binding.teamAwayName.text = event.awayTeam.name

        binding.teamHomeLogo.loadTeamLogo(event.homeTeam.id)
        binding.teamAwayLogo.loadTeamLogo(event.awayTeam.id)

        setupHeaderWithEvent(event)

        eventDetailsViewModel.getIncidents(1)
        eventDetailsViewModel.incidents.observe(viewLifecycleOwner){
            incidentAdapter.updateItems(it)
        }


        return binding.root
    }

    private fun setupHeaderWithEvent(event: Event) {

        val dateFormat = "dd.MM.yyyy."

        val winnerColor = MaterialColors.getColor(binding.root, R.attr.on_surface_lv1)
        val timeFinishedColor = MaterialColors.getColor(binding.root, R.attr.on_surface_lv2)
        val liveColor = MaterialColors.getColor(binding.root, R.attr.live)

        val startDateTime = event.startDate.getLocalDateTime()
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        eventDetailsViewModel.stopEventStatusUpdates()
        _binding = null
    }
}