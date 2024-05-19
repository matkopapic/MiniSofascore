package com.example.minisofascore.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.R
import com.example.minisofascore.databinding.FragmentHomeBinding
import com.example.minisofascore.databinding.TabItemDateBinding
import com.example.minisofascore.ui.home.adapters.EventAdapter
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val NUM_OF_DATE_TABS = 7 + 1 + 7 // 1 week before + today + 1 week after

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by viewModels<HomeViewModel>()
    private val binding get() = _binding!!

    private val eventAdapter by lazy { EventAdapter() }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.eventRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }

        binding.dayInfoDate.text = getString(R.string.today)

        val tabLayoutCalendar = binding.tabLayoutCalendar
        val dateToday = LocalDate.now()
        val todayTabNumber = NUM_OF_DATE_TABS / 2L
        for (tabNumber in 0L..<NUM_OF_DATE_TABS) {
            val newTab = tabLayoutCalendar.newTab()
            val tabDate = dateToday.minusDays(todayTabNumber - tabNumber)
            val tabBinding = TabItemDateBinding.inflate(layoutInflater)
            if (tabNumber == todayTabNumber) {
                tabBinding.dayOfWeek.text = getString(R.string.today)
                newTab.select()
            } else {
                tabBinding.dayOfWeek.text = tabDate.format(DateTimeFormatter.ofPattern("EEE"))
            }
            tabBinding.date.text = tabDate.format(DateTimeFormatter.ofPattern("dd.MM."))
            newTab.setCustomView(tabBinding.root)
            tabLayoutCalendar.addTab(newTab)
        }

        tabLayoutCalendar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabDate = LocalDate.now().minusDays(todayTabNumber - tabLayoutCalendar.selectedTabPosition)
                homeViewModel.getEventsByDate(tabDate)
                val isStartTimeToday = todayTabNumber.toInt() == tabLayoutCalendar.selectedTabPosition
                binding.dayInfoDate.text = if(isStartTimeToday) getString(R.string.today)
                    else tabDate.format(
                        DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy."))
                binding.loadingIndicator.visibility = View.VISIBLE
                binding.dayInfoLayout.visibility = View.GONE
                binding.eventRecyclerView.visibility = View.GONE
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })



        homeViewModel.events.observe(viewLifecycleOwner) {
            eventAdapter.updateItems(it)
            val numOfEvents = if (it.size > 1) {
                "${it.size} ${getString(R.string.event_plural)}"
            } else if (it.size == 1) {
                "${it.size} ${getString(R.string.event_singular)}"
            } else {
                getString(R.string.event_none)
            }
            binding.dayInfoEvents.text = numOfEvents
            binding.loadingIndicator.visibility = View.GONE
            binding.dayInfoLayout.visibility = View.VISIBLE
            binding.eventRecyclerView.visibility = View.VISIBLE
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}