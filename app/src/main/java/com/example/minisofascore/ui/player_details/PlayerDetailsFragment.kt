@file:Suppress("deprecation")
package com.example.minisofascore.ui.player_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.R
import com.example.minisofascore.TeamDetailsActivity
import com.example.minisofascore.TournamentActivity
import com.example.minisofascore.data.models.Player
import com.example.minisofascore.data.models.Team
import com.example.minisofascore.databinding.FragmentPlayerDetailsBinding
import com.example.minisofascore.ui.main_list.EVENT_INFO
import com.example.minisofascore.ui.team_matches.adapters.TeamPagingAdapter
import com.example.minisofascore.ui.team_matches.adapters.TournamentHeaderDecorator
import com.example.minisofascore.ui.tournament_matches.adapters.EventsLoadStateAdapter
import com.example.minisofascore.util.loadFlag
import com.example.minisofascore.util.loadPlayerImage
import com.example.minisofascore.util.loadTeamLogo
import kotlinx.coroutines.launch
import kotlin.math.abs

class PlayerDetailsFragment : Fragment() {

    companion object {
        const val PLAYER_INFO = "player_info"
        fun newInstance(player: Player, team: Team) = PlayerDetailsFragment().apply {
            arguments = Bundle().apply {
                putSerializable(PLAYER_INFO, player)
                putSerializable(TeamDetailsActivity.TEAM_DETAILS, team)
            }
        }
    }

    private val viewModel: PlayerDetailsViewModel by viewModels()
    private var _binding: FragmentPlayerDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerDetailsBinding.inflate(layoutInflater, container, false)

        val player = requireArguments().getSerializable(PLAYER_INFO) as Player
        val team = requireArguments().getSerializable(TeamDetailsActivity.TEAM_DETAILS) as Team


        binding.run {
            toolbarBackArrow.backArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            toolbarBackArrow.toolbarName.text = player.name

            // animating text opacity in toolbar when scrolling
            appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                val totalScrollRange = appBarLayout.totalScrollRange
                val percentage = abs(verticalOffset).toFloat() / totalScrollRange.toFloat()
                toolbarBackArrow.toolbarName.alpha = percentage
            }

            toolbarImageText.name.text = player.name
            toolbarImageText.mainLogo.loadPlayerImage(player.id)

            teamLogo.loadTeamLogo(team.id)
            teamName.text = team.name

            countryAbbr.text = player.country.name.substring(0,3)
            countryLogo.loadFlag(player.country.name)

            playerPosition.text = player.position

            // don't have info on player DOB, placeholder info
            playerDob.text = getString(R.string.playerDOB)
            playerAge.text = getString(R.string.playerAge)
        }

        with(binding.matchesHeader){
            dayInfoDate.text = getString(R.string.matches)
            dayInfoEvents.text = ""
        }

        val pagingAdapter = TeamPagingAdapter(requireContext(),
            onEventClick = {
                findNavController().navigate(
                    R.id.action_fragmentPlayerDetails_to_fragmentEventDetails,
                    Bundle().apply {
                        putSerializable(EVENT_INFO, it)
                    }
                )
            },
            onTournamentClick = {
                startActivity(TournamentActivity.newInstance(requireContext(), it))
            })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = EventsLoadStateAdapter{ pagingAdapter.retry() },
                footer = EventsLoadStateAdapter{ pagingAdapter.retry() }
            )
        }

        val stickyHeaderItemDecorator = TournamentHeaderDecorator()
        binding.recyclerView.let {
            stickyHeaderItemDecorator.attachRecyclerView(
                pagingAdapter,
                it,
                pagingAdapter
            )

        }

        lifecycleScope.launch {
            viewModel.getEventPageFlow(player.id).collect{ pagingData ->
                pagingAdapter.submitData(pagingData)
                stickyHeaderItemDecorator.refreshHeader()
            }
        }

        binding.retryButton.setOnClickListener {
            pagingAdapter.retry()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                pagingAdapter.loadStateFlow.collect { loadState ->
                    // initial loading of list, before any page is loaded
                    val isListEmpty = loadState.refresh is LoadState.NotLoading && pagingAdapter.itemCount == 0
                    binding.run {
                        emptyList.isVisible = isListEmpty
                        recyclerView.isVisible = !isListEmpty
                        progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                        retryButton.isVisible = loadState.source.refresh is LoadState.Error
                    }

                }
            }
        }

        return binding.root
    }
}