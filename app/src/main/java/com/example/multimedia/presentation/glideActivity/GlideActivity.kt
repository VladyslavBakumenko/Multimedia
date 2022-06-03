package com.example.multimedia.presentation.glideActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.multimedia.databinding.ActivityGlideBinding

class GlideActivity: AppCompatActivity() {

    private lateinit var binding: ActivityGlideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.changeImageButton.setOnClickListener {
            Glide.with(this)
                .load("https://source.unsplash.com/random/800x600")
                .into(binding.glideImage)
        }
    }
}