package com.example.minisofascore.ui.leagues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.TournamentActivity
import com.example.minisofascore.data.models.SportType
import com.example.minisofascore.databinding.FragmentLeaguesBinding
import com.example.minisofascore.databinding.TabItemSportBinding
import com.example.minisofascore.ui.leagues.adapters.LeaguesAdapter
import com.google.android.material.tabs.TabLayout

class LeaguesFragment : Fragment() {

    companion object {
        const val SPORT_TYPE = "sport_type"
    }

    private val leaguesViewModel: LeaguesViewModel by viewModels()
    private var _binding: FragmentLeaguesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaguesBinding.inflate(layoutInflater, container, false)

        binding.toolbar.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        SportType.entries.forEach { sportType ->
            val newTab = binding.tabLayoutSports.newTab()
            val tabBinding = TabItemSportBinding.inflate(layoutInflater)
            tabBinding.tabText.text = getString(sportType.stringRes)
            tabBinding.tabIcon.setImageResource(sportType.drawableRes)
            if (sportType == leaguesViewModel.selectedSport) {
                newTab.select()
            }
            newTab.setCustomView(tabBinding.root)
            binding.tabLayoutSports.addTab(newTab)
        }
        binding.tabLayoutSports.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                leaguesViewModel.getLeaguesBySport(SportType.entries[binding.tabLayoutSports.selectedTabPosition])
                enableLoadingAnimations(true)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

        })
        val leaguesAdapter = LeaguesAdapter{
            val intent = TournamentActivity.newInstance(requireContext(), it)
            startActivity(intent)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = leaguesAdapter
            itemAnimator = null
        }

        leaguesViewModel.leagues.observe(viewLifecycleOwner) {
            leaguesAdapter.updateItems(it)
            enableLoadingAnimations(false)
        }
        return binding.root
    }
    fun enableLoadingAnimations(isLoading: Boolean) {
        binding.run {
            loadingIndicator.isVisible = isLoading
            recyclerView.isVisible = !isLoading
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        leaguesViewModel.cancelCurrentLoading()
    }
}