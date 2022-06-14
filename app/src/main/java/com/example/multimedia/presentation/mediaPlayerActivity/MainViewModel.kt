package com.example.multimedia.presentation.mediaPlayerActivity

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.multimedia.R
import com.example.multimedia.data.SoundModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val application: Application) : ViewModel() {


    private val _currentSound = MutableLiveData<MediaPlayer>()
    val currentSound: LiveData<MediaPlayer>
        get() = _currentSound

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int>
        get() = _progress

    private val _maxProgress = MutableLiveData<Int>()
    val maxProgress: LiveData<Int>
        get() = _maxProgress

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("nnnnnnnn", "exception")
    }
    private lateinit var coroutineJob: Any

    private var coroutineScope = CoroutineScope(Dispatchers.IO)

    var lastSound = 0

    fun getSoundList(): List<SoundModel> {
        val soundList = mutableListOf<SoundModel>()
        with(soundList) {
            add(SoundModel("Sound 1", R.raw.sound1))
            add(SoundModel("Sound 2", R.raw.sound2))
            add(SoundModel("Sound 3", R.raw.sound3))
        }
        return soundList
    }


    fun updateProgress() {
        _maxProgress.value = _currentSound.value?.duration

        coroutineJob = coroutineScope.launch {
            while (true) {
                _progress.postValue(_currentSound.value ?.currentPosition)
                delay(1000)
            }
        }
    }

    fun reInitCoroutineScope() {
        coroutineScope = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler)
    }


    fun clickToRecyclerViewItem(soundModel: SoundModel) {
        lastSound = soundModel.soundRes
        with(_currentSound) {
            value?.release()
            value = MediaPlayer.create(application, soundModel.soundRes)
        }
    }

    fun reInitCurrentSound(lastSound: Int) {
        _currentSound.value?.release()
        _currentSound.value = MediaPlayer.create(application, lastSound)
    }

    override fun onCleared() {
        super.onCleared()
        (coroutineJob as Job).cancel()
    }
}