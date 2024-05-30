package com.example.minisofascore.ui.main_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.MainActivity
import com.example.minisofascore.R
import com.example.minisofascore.TournamentActivity
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.databinding.FragmentMainListBinding
import com.example.minisofascore.databinding.TabItemDateBinding
import com.example.minisofascore.databinding.TabItemSportBinding
import com.example.minisofascore.ui.main_list.adapters.EventAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val EVENT_INFO = "event_info"
const val NUM_OF_DATE_TABS = 7 + 1 + 7 // 1 week before + today + 1 week after

class MainListFragment : Fragment() {

    private var _binding: FragmentMainListBinding? = null
    private val mainListViewModel by viewModels<MainListViewModel>()
    private val binding get() = _binding!!

    private var selectedTabDateIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainListBinding.inflate(inflater, container, false)

        val eventAdapter = EventAdapter(requireContext()) {
            findNavController().navigate(
                R.id.action_navigation_main_list_to_navigation_event_details,
                Bundle().apply {
                    putSerializable(EVENT_INFO, it)
                }
            )
        }

        binding.eventRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
            itemAnimator = null
        }

        val tabLayoutSports = binding.tabLayoutSports
        for (tabNumber in MainActivity.sports.indices) {
            val newTab = tabLayoutSports.newTab()
            val tabBinding = TabItemSportBinding.inflate(layoutInflater)
            tabBinding.tabText.text = MainActivity.sports[tabNumber].name
            tabBinding.tabIcon.setImageResource(
                when (tabNumber) {
                    0 -> R.drawable.ic_football
                    1 -> R.drawable.ic_basketball
                    else -> R.drawable.ic_american_football
                }
            )
            if (MainActivity.sports[tabNumber] == mainListViewModel.selectedSport) {
                newTab.select()
            }
            newTab.setCustomView(tabBinding.root)
            tabLayoutSports.addTab(newTab)
        }

        tabLayoutSports.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                mainListViewModel.selectedSport = MainActivity.sports[tabLayoutSports.selectedTabPosition]
                mainListViewModel.getEventsBySportAndDate()
                startLoadingAnimations()
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

        })

        val tabLayoutCalendar = binding.tabLayoutCalendar
        val dateToday = LocalDate.now()
        val todayTabNumber = NUM_OF_DATE_TABS / 2L
        for (tabNumber in 0L..<NUM_OF_DATE_TABS) {
            val newTab = tabLayoutCalendar.newTab()
            val tabDate = dateToday.minusDays(todayTabNumber - tabNumber)
            val tabBinding = TabItemDateBinding.inflate(layoutInflater)
            if (tabNumber == todayTabNumber) {
                tabBinding.dayOfWeek.text = getString(R.string.today)
            } else {
                tabBinding.dayOfWeek.text = tabDate.format(DateTimeFormatter.ofPattern("EEE"))
            }
            tabBinding.date.text = tabDate.format(DateTimeFormatter.ofPattern("dd.MM."))
            if (tabDate == mainListViewModel.selectedDate) {
                newTab.select()
                selectedTabDateIndex = tabNumber.toInt()
            }
            newTab.setCustomView(tabBinding.root)
            tabLayoutCalendar.addTab(newTab)
        }



        lifecycleScope.launch {
            // re-selects today's tab after sometime so the TabLayout scrolls to it
            delay(100)
            tabLayoutCalendar.setScrollPosition(selectedTabDateIndex, 0f, true)
        }

        tabLayoutCalendar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabDate = LocalDate.now().minusDays(todayTabNumber - tabLayoutCalendar.selectedTabPosition)
                mainListViewModel.selectedDate = tabDate
                mainListViewModel.getEventsBySportAndDate()
                startLoadingAnimations()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        var tournament: Tournament? = null

        mainListViewModel.events.observe(viewLifecycleOwner) {
            eventAdapter.updateItems(mainListViewModel.selectedDate, it)
            endLoadingAnimations()
            if (it.isNotEmpty())
                tournament = it[0].tournament
        }


        binding.tournamentIcon.setOnClickListener {
            startActivity(TournamentActivity.newInstance(requireContext(), tournament!!))
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mainListViewModel.cancelCurrentLoading()
    }

    fun startLoadingAnimations() {
        binding.loadingIndicator.visibility = View.VISIBLE
        binding.eventRecyclerView.visibility = View.GONE
    }

    private fun endLoadingAnimations() {
        binding.loadingIndicator.visibility = View.GONE
        binding.eventRecyclerView.visibility = View.VISIBLE
    }

}