package com.example.minisofascore.ui.event_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

        val event = requireArguments().getSerializable(EVENT_INFO)

        val textView: TextView = binding.textNotifications
        textView.text = event.toString()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}