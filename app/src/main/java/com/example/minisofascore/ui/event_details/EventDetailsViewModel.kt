package com.example.minisofascore.ui.event_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.Incident
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.EventRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val LENGTH_BETWEEN_API_CALLS = 10000L // 10 seconds
class EventDetailsViewModel : ViewModel() {


    private val _incidents = MutableLiveData<List<Incident>>()
    val incidents: LiveData<List<Incident>> = _incidents

    private val _eventStatus = MutableLiveData<Event>()
    val eventStatus: LiveData<Event> = _eventStatus

    private var eventUpdater: Job? = null

    fun startEventUpdates(eventId: Int) {
        eventUpdater?.cancel()
        eventUpdater = viewModelScope.launch {
            while (true) {
                delay(LENGTH_BETWEEN_API_CALLS)
                when (val response = EventRepository.getEvent(eventId)) {
                    is Result.Success -> _eventStatus.value = response.data
                    is Result.Error -> Log.e("aaaa", response.error.toString())
                }
            }
        }
    }

    fun stopEventUpdates() {
        eventUpdater?.cancel()
        eventUpdater = null
    }

    fun getIncidents(eventId: Int){
        viewModelScope.launch {
            when (val response = EventRepository.getIncidentsForEvent(eventId)) {
                is Result.Error -> Log.e("aaaa", response.error.toString())
                is Result.Success -> _incidents.value = response.data
            }
        }
    }
}