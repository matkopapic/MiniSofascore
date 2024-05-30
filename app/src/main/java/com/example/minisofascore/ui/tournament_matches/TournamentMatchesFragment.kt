package com.example.minisofascore.ui.tournament_matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.minisofascore.databinding.FragmentTournamentMatchesBinding

class TournamentMatchesFragment : Fragment() {

    private var _binding: FragmentTournamentMatchesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TournamentMatchesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTournamentMatchesBinding.inflate(inflater, container, false)

        return binding.root
    }
}