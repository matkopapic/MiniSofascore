package com.example.minisofascore.ui.team_details_squad

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.TeamDetailsActivity
import com.example.minisofascore.data.models.Team
import com.example.minisofascore.databinding.FragmentTeamDetailsSquadBinding
import com.example.minisofascore.ui.team_details_squad.adapters.PlayerAdapter

class TeamDetailsSquadFragment : Fragment() {

    companion object {
        fun newInstance(team: Team) = TeamDetailsSquadFragment().apply {
            arguments = Bundle().apply {
                putSerializable(TeamDetailsActivity.TEAM_DETAILS, team)
            }
        }
    }

    private val viewModel: TeamDetailsSquadViewModel by viewModels()

    private var _binding: FragmentTeamDetailsSquadBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamDetailsSquadBinding.inflate(layoutInflater, container, false)

        val team = requireArguments().getSerializable(TeamDetailsActivity.TEAM_DETAILS) as Team

        val playerAdapter = PlayerAdapter(requireContext()){
            // TODO: handle on player click
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playerAdapter
        }

        viewModel.getTeamDetails(team.id)

        viewModel.teamDetails.observe(viewLifecycleOwner) {
            playerAdapter.updateItems(it.players, it.team.managerName ?: "")
            Log.d("aaaa", "onCreateView: ${it.players}")
        }


        return binding.root
    }
}