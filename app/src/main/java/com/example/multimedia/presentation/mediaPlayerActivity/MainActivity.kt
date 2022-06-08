package com.example.multimedia.presentation.mediaPlayerActivity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.multimedia.data.SoundModel
import com.example.multimedia.databinding.ActivityMainBinding
import com.example.multimedia.presentation.exoPlayerActivity.ExoPlayerActivity
import com.example.multimedia.presentation.glideActivity.GlideActivity
import com.example.multimedia.presentation.soundRecyclerView.SoundRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var coroutineScope = CoroutineScope(Dispatchers.IO)

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var soundRecyclerViewAdapter: SoundRecyclerViewAdapter

    private var changeSound = false
    private var itFirstIteration = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = MainViewModel(application)
        setRecyclerView()
        recyclerViewClickListener()
        buttonsClickListeners()

        viewModel.currentSound.observe(this) {
            if (savedInstanceState?.getBoolean(IS_PLAY_BEFORE) == true) startSoundAndSeekBar(it)
            if (savedInstanceState == null) startSoundAndSeekBar(it)
        }

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                viewModel.lastSound = this.getInt(CURRENT_SOUND)
                viewModel.reInitCurrentSound(this.getInt(CURRENT_SOUND))
                viewModel.currentSound.value?.seekTo(this.getInt(SEEK_TIME))
            }
        }
    }

    private fun setRecyclerView() {
        soundRecyclerViewAdapter = SoundRecyclerViewAdapter()
        soundRecyclerViewAdapter.soundList = viewModel.getSoundList()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = soundRecyclerViewAdapter
    }

    private fun startSoundAndSeekBar(mediaPlayer: MediaPlayer) {
        mediaPlayer.start()
        changeSound = true
        resetSeekBar()
    }

    private fun recyclerViewClickListener() {

        soundRecyclerViewAdapter.onSoundClickListener =
            object : SoundRecyclerViewAdapter.OnSoundClickListener {
                override fun onSoundClickListener(soundModel: SoundModel) {
                    viewModel.clickToRecyclerViewItem(soundModel)
                    setSeekBarListener()
                }
            }
    }

    private fun resetSeekBar() {
        CoroutineScope(Dispatchers.IO).launch {

            if (changeSound) testCancelOldCoroutine(this)

            var counter = 0
            while (true) {
                Log.d("fdfdfdfdfd", counter++.toString())
                binding.seekbar.max = viewModel.currentSound.value?.duration ?: 9999
                binding.seekbar.progress = viewModel.currentSound.value?.currentPosition ?: 9999
                delay(1000)
            }
        }
    }

    private  fun testCancelOldCoroutine (coroutineScope: CoroutineScope) {
        changeSound = false
        coroutineScope.cancel()
        resetSeekBar()
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
            viewModel.currentSound.value?.start()
        }

        binding.pause.setOnClickListener {
            viewModel.currentSound.value?.pause()
        }

        binding.testExoPlayerActivity.setOnClickListener {
            startActivity(Intent(this, ExoPlayerActivity::class.java))
        }

        binding.testGlideActivity.setOnClickListener {
            startActivity(Intent(this, GlideActivity::class.java))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SEEK_TIME, viewModel.currentSound.value?.currentPosition ?: 33333)
        outState.putBoolean(IS_PLAY_BEFORE, viewModel.currentSound.value?.isPlaying ?: false)
        outState.putInt(CURRENT_SOUND, viewModel.lastSound)
        viewModel.currentSound.value?.release()
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.currentSound.value?.release()
        coroutineScope.cancel()
    }

    companion object {
        private const val SEEK_TIME = "seek_time"
        private const val IS_PLAY_BEFORE = "is_play_before"
        private const val CURRENT_SOUND = "current_sound"

        const val DOES_NOT_EXIST = 0
    }
}
