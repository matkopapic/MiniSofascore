package com.example.minisofascore.ui.event_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascore.data.models.EventStatus
import com.example.minisofascore.data.models.Incident
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val LENGTH_BETWEEN_API_CALLS = 10000L // 10 seconds
class EventDetailsViewModel : ViewModel() {

    private val repo = Repository()

    private val _incidents = MutableLiveData<List<Incident>>()
    val incidents: LiveData<List<Incident>> = _incidents

    private val _eventStatus = MutableLiveData<EventStatus>()
    val eventStatus: LiveData<EventStatus> = _eventStatus

    private var eventStatusUpdater: Job? = null

    fun startEventStatusUpdates(eventId: Int) {
        eventStatusUpdater?.cancel()
        eventStatusUpdater = viewModelScope.launch {
            while (true) {
                delay(LENGTH_BETWEEN_API_CALLS)
                when (val response = repo.getEvent(eventId)) {
                    is Result.Success -> _eventStatus.postValue(response.data.status)
                    is Result.Error -> Log.e("aaaa", response.error.toString())
                }
            }
        }
    }

    fun stopEventStatusUpdates() {
        eventStatusUpdater?.cancel()
        eventStatusUpdater = null
    }

    fun getIncidents(eventId: Int){
        viewModelScope.launch {
            when (val response = repo.getIncidentsForEvent(eventId)) {
                is Result.Error -> Log.e("aaaa", response.error.toString())
                is Result.Success -> _incidents.value = response.data
            }
        }
    }
}