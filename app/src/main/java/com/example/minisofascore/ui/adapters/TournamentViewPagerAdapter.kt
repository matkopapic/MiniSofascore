package com.example.minisofascore.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.ui.tournament_matches.TournamentMatchesFragment
import com.example.minisofascore.ui.tournament_standings.TournamentStandingsFragment

private const val NUM_TABS = 2
class TournamentViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle, private val tournament: Tournament): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = NUM_TABS
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return TournamentMatchesFragment()
            1 -> return TournamentStandingsFragment.newInstance(tournament)
        }
        return TournamentMatchesFragment()
    }
}