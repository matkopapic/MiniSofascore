package com.example.minisofascore.ui.team_standings.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.minisofascore.R
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.databinding.TournamentSpinnerItemBinding
import com.example.minisofascore.util.loadTournamentLogo

class TournamentSpinnerAdapter(
    context: Context,
    private val tournaments: List<Tournament>
): ArrayAdapter<Tournament>(context, R.layout.tournament_spinner_item, tournaments) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, parent)
    }

    private fun createView(position: Int, parent: ViewGroup): View {
        val binding = TournamentSpinnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.tournamentIcon.loadTournamentLogo(tournaments[position].id)
        binding.tournamentName.text = tournaments[position].name
        return binding.root
    }
}