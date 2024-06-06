package com.example.minisofascore.ui.team_details_viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.minisofascore.R
import com.example.minisofascore.TeamDetailsActivity
import com.example.minisofascore.data.models.Team
import com.example.minisofascore.databinding.FragmentViewPagerTeamDetailsBinding
import com.example.minisofascore.databinding.TabItemTextBinding
import com.example.minisofascore.ui.team_details_viewpager.adapters.TeamDetailsViewPagerAdapter
import com.example.minisofascore.util.loadTeamLogo
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs


class ViewPagerTeamDetailsFragment : Fragment() {

    companion object {
        fun newInstance(team: Team) =
            ViewPagerTeamDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(TeamDetailsActivity.TEAM_DETAILS, team)
                }
            }
    }

    private var _binding: FragmentViewPagerTeamDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerTeamDetailsBinding.inflate(inflater, container, false)

        val team = requireArguments().getSerializable(TeamDetailsActivity.TEAM_DETAILS) as Team

        binding.toolbarBackArrow.backArrow.setOnClickListener {
            activity?.finish()
        }

        binding.toolbarBackArrow.toolbarName.text = team.name

        // animating text opacity in toolbar when scrolling
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percentage = abs(verticalOffset).toFloat() / totalScrollRange.toFloat()
            binding.toolbarBackArrow.toolbarName.alpha = percentage
        }

        binding.toolbarImageText.name.text = team.name
        binding.toolbarImageText.mainLogo.loadTeamLogo(team.id)
        binding.toolbarImageText.countryName.text = team.country.name
        binding.toolbarImageText.countryLogo.loadTeamLogo(team.id)


        val viewpager = binding.viewpager
        val tabLayout = binding.tabLayout
        val adapter = TeamDetailsViewPagerAdapter(childFragmentManager, lifecycle, team)
        viewpager.adapter = adapter

        val tabNames = arrayOf(
            getString(R.string.details),
            getString(R.string.matches),
            getString(R.string.standings),
            getString(R.string.squad)
        )

        TabLayoutMediator(tabLayout, viewpager){ tab, position ->
            val tabBinding = TabItemTextBinding.inflate(layoutInflater)
            tabBinding.tabText.text = tabNames[position]
            tab.customView = tabBinding.root
        }.attach()



        return binding.root
    }


}