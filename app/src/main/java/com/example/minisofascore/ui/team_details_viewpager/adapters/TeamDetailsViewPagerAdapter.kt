package com.example.minisofascore.ui.team_details_viewpager.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.minisofascore.data.models.Team
import com.example.minisofascore.ui.team_details.TeamDetailsFragment
import com.example.minisofascore.ui.team_details_squad.TeamDetailsSquadFragment
import com.example.minisofascore.ui.team_matches.TeamMatchesFragment
import com.example.minisofascore.ui.team_standings.TeamStandingsFragment

private const val NUM_TABS = 4
class TeamDetailsViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val team: Team): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = NUM_TABS
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return TeamDetailsFragment.newInstance(team)
            1 -> return TeamMatchesFragment.newInstance(team)
            2 -> return TeamStandingsFragment.newInstance(team)
            3 -> return TeamDetailsSquadFragment.newInstance(team)
        }
        return TeamDetailsFragment.newInstance(team)
    }
}