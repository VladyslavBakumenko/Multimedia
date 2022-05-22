package com.example.multimedia.presentation.mediaPlayerActivity

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.multimedia.data.SoundModel
import com.example.multimedia.databinding.ActivityMainBinding
import com.example.multimedia.presentation.soundRecyclerView.SoundRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var coroutineScope = CoroutineScope(Dispatchers.IO)

    private lateinit var viewModel : MainViewModel


    private lateinit var binding: ActivityMainBinding
    private lateinit var soundRecyclerViewAdapter: SoundRecyclerViewAdapter
    private lateinit var currentSound: MediaPlayer
    private lateinit var previousSound: MediaPlayer
    private var itFirstTab = true
    //private val isPlaying = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = MainViewModel(application)
        setRecyclerView()
        recyclerViewClickListener()
        buttonsClickListeners()
    }

    private fun setRecyclerView() {
        soundRecyclerViewAdapter = SoundRecyclerViewAdapter()
        soundRecyclerViewAdapter.soundList = viewModel.getSoundList()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = soundRecyclerViewAdapter
    }

    private fun recyclerViewClickListener() {

        soundRecyclerViewAdapter.onSoundClickListener =
            object : SoundRecyclerViewAdapter.OnSoundClickListener {
                override fun onSoundClickListener(soundModel: SoundModel) {
                    currentSound = soundModel.sound
                    binding.seekbar.max = currentSound.duration
                    setSeekBarListener()
                    resetSeekBar()
                    if (itFirstTab) {
                        currentSound.start()
                        previousSound = currentSound
                        itFirstTab = false
                    } else {
                        if (previousSound.isPlaying) {
                            previousSound.pause()
                            previousSound.seekTo(0)
                        }
                        currentSound.start()
                        previousSound = currentSound
                    }
                }
            }
    }

    private fun resetSeekBar() {
        coroutineScope.launch {
            while (true) {
                binding.seekbar.progress = currentSound.currentPosition
                delay(1000)
            }
        }
    }

    private fun setSeekBarListener() {
        binding.seekbar.setOnSeekBarChangeListener (object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) currentSound.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun buttonsClickListeners() {
        binding.playSong.setOnClickListener {
            currentSound.start()
        }

        binding.pause.setOnClickListener {
            currentSound.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentSound.stop()
        itFirstTab = true
    }
}
