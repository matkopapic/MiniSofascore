package com.example.minisofascore.ui.tournament_standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.TeamDetailsActivity
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.data.models.getSportType
import com.example.minisofascore.databinding.FragmentTournamentStandingsBinding
import com.example.minisofascore.ui.tournament_standings.adapters.TournamentStandingsAdapter

class TournamentStandingsFragment : Fragment() {

    companion object {
        private const val TOURNAMENT_INFO = "tournament_info"
        fun newInstance(tournament: Tournament): TournamentStandingsFragment {
            return TournamentStandingsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(TOURNAMENT_INFO, tournament)
                }
            }
        }
    }

    private var _binding: FragmentTournamentStandingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TournamentStandingsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTournamentStandingsBinding.inflate(inflater, container, false)

        val tournament = requireArguments().getSerializable(TOURNAMENT_INFO) as Tournament

        viewModel.getStandingsForTournament(tournament.id)

        viewModel.standings.observe(viewLifecycleOwner){
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = TournamentStandingsAdapter(it, tournament.sport.getSportType(), null) {
                    val intent = TeamDetailsActivity.newInstance(requireContext(), it)
                    startActivity(intent)
                }
            }
        }
        return binding.root
    }
}