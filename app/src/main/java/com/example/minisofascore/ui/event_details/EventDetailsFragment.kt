package com.example.minisofascore.ui.event_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.databinding.FragmentEventDetailsBinding
import com.example.minisofascore.ui.main_list.EVENT_INFO

class EventDetailsFragment : Fragment() {

    private var _binding: FragmentEventDetailsBinding? = null
    private val eventDetailsViewModel by viewModels<EventDetailsViewModel>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false)

        val incidentAdapter = IncidentAdapter(requireContext())

        binding.incidentRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = incidentAdapter
        }

        val event = (requireArguments().getSerializable(EVENT_INFO)) as Event

        eventDetailsViewModel.getIncidents(1)
        eventDetailsViewModel.incidents.observe(viewLifecycleOwner){
            incidentAdapter.updateItems(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}