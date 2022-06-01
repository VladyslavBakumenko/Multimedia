package com.example.multimedia.presentation.mediaPlayerActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.multimedia.data.SoundModel
import com.example.multimedia.databinding.ActivityMainBinding
import com.example.multimedia.presentation.exoPlayerActivity.ExoPlayerActivity
import com.example.multimedia.presentation.soundRecyclerView.SoundRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var coroutineScope = CoroutineScope(Dispatchers.IO)

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var soundRecyclerViewAdapter: SoundRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = MainViewModel(application)
        setRecyclerView()
        recyclerViewClickListener()
        buttonsClickListeners()

        if(savedInstanceState != null) {
            if(savedInstanceState.getBoolean(IS_PLAY_BEFORE)) viewModel.currentSound.value?.start()
            viewModel.currentSound.value?.seekTo(savedInstanceState.getInt(SEEK_TIME))
        }
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
                    viewModel.clickToRecyclerViewItem(soundModel)
                    setSeekBarListener()
                    resetSeekBar()
                }
            }
    }

   private fun resetSeekBar() {
        coroutineScope.launch {
            while (true) {
                binding.seekbar.max = viewModel.currentSound.value?.duration ?: 0
                binding.seekbar.progress = viewModel.currentSound.value?.currentPosition ?: 0
                Log.d("fdff", "e")
                delay(1000)
            }
        }
    }

    private fun setSeekBarListener() {
        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) viewModel.currentSound.value?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun buttonsClickListeners() {
        binding.playSong.setOnClickListener {
          // viewModel.currentSound.value?.start()
            startActivity(Intent(this, ExoPlayerActivity::class.java))
        }

        binding.pause.setOnClickListener {
            viewModel.currentSound.value?.pause()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SEEK_TIME, viewModel.currentSound.value?.currentPosition ?: 0)
        outState.putBoolean(IS_PLAY_BEFORE, viewModel.currentSound.value?.isPlaying ?: false)
    }

    override fun onDestroy() {
        super.onDestroy()
        //viewModel.currentSound.value?.release()
        //viewModel.previousSound.value?.release()
        coroutineScope.cancel()
    }

    companion object {
        private const val SEEK_TIME = "seek_time"
        private const val IS_PLAY_BEFORE = "is_play_before"
        private const val MEDIA_ITEM = "media_item"
    }
}
