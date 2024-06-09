@file:Suppress("deprecation")
package com.example.minisofascore.ui.tournament_viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.minisofascore.R
import com.example.minisofascore.TournamentActivity.Companion.TOURNAMENT_INFO
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.databinding.FragmentViewPagerBinding
import com.example.minisofascore.databinding.TabItemTextBinding
import com.example.minisofascore.ui.tournament_viewpager.adapters.TournamentViewPagerAdapter
import com.example.minisofascore.util.loadFlag
import com.example.minisofascore.util.loadTournamentLogo
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs

class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val tournament = requireArguments().getSerializable(TOURNAMENT_INFO) as Tournament


        binding.toolbarBackArrow.backArrow.setOnClickListener {
            activity?.finish()
        }

        binding.toolbarBackArrow.toolbarName.text = tournament.name

        // animating text opacity in toolbar when scrolling
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percentage = abs(verticalOffset).toFloat() / totalScrollRange.toFloat()
            binding.toolbarBackArrow.toolbarName.alpha = percentage
        }

        binding.toolbarImageText.name.text = tournament.name
        binding.toolbarImageText.mainLogo.loadTournamentLogo(tournament.id)
        binding.toolbarImageText.countryName.text = tournament.country.name
        binding.toolbarImageText.countryLogo.loadFlag(tournament.country.name)


        val viewpager = binding.viewpager
        val tabLayout = binding.tabLayout
        val adapter = TournamentViewPagerAdapter(childFragmentManager, lifecycle, tournament)
        viewpager.adapter = adapter

        val tabNames = arrayOf(
            getString(R.string.matches),
            getString(R.string.standings)
        )

        TabLayoutMediator(tabLayout, viewpager){ tab, position ->
            val tabBinding = TabItemTextBinding.inflate(layoutInflater)
            tabBinding.tabText.text = tabNames[position]
            tab.customView = tabBinding.root
        }.attach()

        return binding.root
    }
}