package com.example.minisofascore.ui.home

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascore.data.repository.Repository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repo = Repository()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val image = MutableLiveData<Bitmap>()

    init {
        viewModelScope.launch {
            image.value = repo.getTeamLogoById()
//            when(val response = repo.getTeamLogoById()) {
//                is Result.Error -> Log.d("aaaa", "error:${response.error}")
//                is Result.Success -> Log.d("aaaa", "success: ${response.data} ")
//            }
        }
    }
}