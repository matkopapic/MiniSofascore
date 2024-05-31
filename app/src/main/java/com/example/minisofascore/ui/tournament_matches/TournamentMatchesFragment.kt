package com.example.minisofascore.ui.tournament_matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.databinding.FragmentTournamentMatchesBinding
import com.example.minisofascore.ui.tournament_matches.adapters.EventPagingAdapter
import com.example.minisofascore.ui.tournament_matches.adapters.StickyHeaderItemDecorator
import kotlinx.coroutines.launch

class TournamentMatchesFragment : Fragment() {

    companion object {
        private const val TOURNAMENT_INFO = "tournament_info"
        fun newInstance(tournament: Tournament): TournamentMatchesFragment {
            return TournamentMatchesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(TOURNAMENT_INFO, tournament)
                }
            }
        }
    }

    private var _binding: FragmentTournamentMatchesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TournamentMatchesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTournamentMatchesBinding.inflate(inflater, container, false)

        val tournament = requireArguments().getSerializable(TOURNAMENT_INFO) as Tournament

        val eventPagingAdapter = EventPagingAdapter(
            requireContext(),
            onEventClick = {}
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventPagingAdapter
            addItemDecoration(StickyHeaderItemDecorator(eventPagingAdapter))
        }

        lifecycleScope.launch {
            viewModel.getEventPageFlow(tournament.id).collect{ pagingData ->
                eventPagingAdapter.submitData(pagingData)
            }
        }

        return binding.root
    }
}