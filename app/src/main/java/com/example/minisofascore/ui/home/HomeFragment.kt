package com.example.minisofascore.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.databinding.FragmentHomeBinding
import com.example.minisofascore.databinding.TabItemDateBinding
import com.example.minisofascore.ui.home.adapters.EventAdapter
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val NUM_OF_DATE_TABS = 15 // 1 week before + today + 1 week after

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

        binding.dayInfoDate.text = "Today"

        val tabLayoutCalendar = binding.tabLayoutCalendar
        val dateToday = LocalDate.now()
        val todayTabNumber = NUM_OF_DATE_TABS / 2L
        for (tabNumber in 0L..<NUM_OF_DATE_TABS) {
            val newTab = tabLayoutCalendar.newTab()
            val tabDate = dateToday.minusDays(todayTabNumber - tabNumber)
            val tabBinding = TabItemDateBinding.inflate(layoutInflater)
            if (tabNumber == todayTabNumber) {
                tabBinding.dayOfWeek.text = "Today"
                newTab.select()
            } else {
                tabBinding.dayOfWeek.text = tabDate.format(DateTimeFormatter.ofPattern("EEE"))
            }
            tabBinding.date.text = tabDate.format(DateTimeFormatter.ofPattern("dd.MM."))
            newTab.setCustomView(tabBinding.root)
            tabLayoutCalendar.addTab(newTab)
        }

        tabLayoutCalendar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                val date: TextView = (p0?.customView as LinearLayout).getChildAt(1) as TextView
                homeViewModel.getEventsByDate(stringToDate(date.text.toString()))
                val eventDateInfo = stringToDate(date.text.toString())
                val isStartTimeToday = LocalDate.now() == eventDateInfo
                binding.dayInfoDate.text = if(isStartTimeToday) "Today"
                    else eventDateInfo.format(
                        DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy."))
                binding.loadingIndicator.visibility = View.VISIBLE
                binding.dayInfoLayout.visibility = View.GONE
                binding.eventRecyclerView.visibility = View.GONE
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

        })



        homeViewModel.events.observe(viewLifecycleOwner) {
            eventAdapter.updateItems(it)
            var numOfEvents = ""
            if (it.size > 1) {
                numOfEvents = "${it.size} Events"
            } else if (it.size == 1) {
                numOfEvents = "${it.size} Event"
            } else {
                numOfEvents = "No Events"
            }
            binding.dayInfoEvents.text = numOfEvents
            binding.loadingIndicator.visibility = View.GONE
            binding.dayInfoLayout.visibility = View.VISIBLE
            binding.eventRecyclerView.visibility = View.VISIBLE
        }

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun stringToDate(dateString: String): LocalDate {
        // Get the current year
        val currentYear = LocalDate.now().year

        // Create a date formatter
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        // Parse the date string
        val parsedDate = LocalDate.parse("$dateString$currentYear", formatter)

        // Otherwise, use the current year
        return parsedDate
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}