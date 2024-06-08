package com.example.minisofascore.ui.team_details_squad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.R
import com.example.minisofascore.TeamDetailsActivity
import com.example.minisofascore.data.models.Team
import com.example.minisofascore.databinding.FragmentTeamDetailsSquadBinding
import com.example.minisofascore.ui.player_details.PlayerDetailsFragment
import com.example.minisofascore.ui.team_details.TeamDetailsViewModel
import com.example.minisofascore.ui.team_details_squad.adapters.PlayerAdapter

class TeamDetailsSquadFragment : Fragment() {

    companion object {
        fun newInstance(team: Team) = TeamDetailsSquadFragment().apply {
            arguments = Bundle().apply {
                putSerializable(TeamDetailsActivity.TEAM_DETAILS, team)
            }
        }
    }

    private val viewModel: TeamDetailsViewModel by activityViewModels()

    private var _binding: FragmentTeamDetailsSquadBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamDetailsSquadBinding.inflate(layoutInflater, container, false)

        val team = requireArguments().getSerializable(TeamDetailsActivity.TEAM_DETAILS) as Team

        val playerAdapter = PlayerAdapter(requireContext()){
            findNavController().navigate(
                R.id.action_fragmentTeamViewPager_to_fragmentPlayerDetails,
                Bundle().apply {
                    putSerializable(PlayerDetailsFragment.PLAYER_INFO, it)
                    putSerializable(TeamDetailsActivity.TEAM_DETAILS, team)
                })
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playerAdapter
        }


        viewModel.teamDetails.observe(viewLifecycleOwner) {
            playerAdapter.updateItems(it.players, it.team.managerName ?: "")
        }


        return binding.root
    }
}