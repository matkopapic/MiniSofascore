@file:Suppress("DEPRECATION")
package com.example.minisofascore.ui.event_details

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.R
import com.example.minisofascore.TeamDetailsActivity
import com.example.minisofascore.TournamentActivity
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.EventStatus
import com.example.minisofascore.data.models.TeamSide
import com.example.minisofascore.data.models.getSportType
import com.example.minisofascore.databinding.FragmentEventDetailsBinding
import com.example.minisofascore.ui.main_list.EVENT_INFO
import com.example.minisofascore.ui.main_list.adapters.getTotalAsString
import com.example.minisofascore.ui.settings.DateFormat
import com.example.minisofascore.ui.settings.SettingsFragment
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
    private val preferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }
    private val binding get() = _binding!!
    private var currentEvent: Event? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false)

        var event = (requireArguments().getSerializable(EVENT_INFO)) as Event
        currentEvent = event
        val sportType = event.tournament.sport.getSportType()

        val incidentAdapter = IncidentAdapter(requireContext(), sportType, event.status == EventStatus.IN_PROGRESS)


        binding.incidentRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = incidentAdapter
        }

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.homeLayout.setOnClickListener {
            val intent = TeamDetailsActivity.newInstance(requireContext(), event.homeTeam)
            startActivity(intent)
        }

        binding.awayLayout.setOnClickListener {
            val intent = TeamDetailsActivity.newInstance(requireContext(), event.awayTeam)
            startActivity(intent)
        }

        binding.buttonViewTournamentDetails.setOnClickListener {
            if (activity is TournamentActivity) {
                findNavController().popBackStack()
            } else {
                val intent = TournamentActivity.newInstance(requireContext(), event.tournament)
                startActivity(intent)
            }

        }

        eventDetailsViewModel.eventStatus.observe(viewLifecycleOwner) {
            if (it.status != event.status) {
                event = it
                setupHeaderWithEvent(event)
                incidentAdapter.refreshEventStatus(it.status)
                eventDetailsViewModel.getIncidents(event.id)
                if (it.status == EventStatus.FINISHED) eventDetailsViewModel.stopEventUpdates()
            } else {
                // game is live so we still want incident updates
                eventDetailsViewModel.getIncidents(event.id)
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

        eventDetailsViewModel.getIncidents(event.id)
        eventDetailsViewModel.incidents.observe(viewLifecycleOwner){
            if (it.isNotEmpty())
                incidentAdapter.updateItems(it)
        }


        return binding.root
    }

    private fun setupHeaderWithEvent(event: Event) {

        val dateFormat = "${preferences.getString(SettingsFragment.DATE_FORMAT, DateFormat.EUROPEAN.formatString)}yyyy."

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

                binding.incidentRecyclerView.visibility = View.VISIBLE
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

                binding.incidentRecyclerView.visibility = View.VISIBLE
                binding.notStartedLayout.visibility = View.GONE

                binding.teamHomeScore.text = event.homeScore.getTotalAsString()
                binding.teamAwayScore.text = event.awayScore.getTotalAsString()

                binding.startTime.text = getString(R.string.full_time)

                binding.startTime.setTextColor(timeFinishedColor)
                binding.teamHomeScore.setTextColor(timeFinishedColor)
                binding.minus.setTextColor(timeFinishedColor)
                binding.teamAwayScore.setTextColor(timeFinishedColor)
                if (event.winnerCode == TeamSide.HOME) {
                    binding.teamHomeScore.setTextColor(winnerColor)
                }
                if (event.winnerCode == TeamSide.AWAY) {
                    binding.teamAwayScore.setTextColor(winnerColor)
                }
            }
        }
    }

    private fun requestEventUpdates(event: Event){
        // we start EventStatus updates if the game is live or it's 5 minutes before startTime
        val startDateTime = event.startDate.getLocalDateTime()
        if (event.status == EventStatus.IN_PROGRESS ||
            (event.status == EventStatus.NOT_STARTED
                    &&
            ChronoUnit.MINUTES.between(startDateTime, LocalDateTime.now()) < 5)
        ) {
            eventDetailsViewModel.startEventUpdates(event.id)
        }
    }

    override fun onPause() {
        eventDetailsViewModel.stopEventUpdates()
        super.onPause()
    }

    override fun onResume() {
        currentEvent?.let {
            requestEventUpdates(it)
        }
        super.onResume()
    }
}