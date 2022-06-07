package com.example.multimedia.presentation.glideActivity

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.multimedia.R
import com.example.multimedia.databinding.ActivityGlideBinding

class GlideActivity: AppCompatActivity() {

    private lateinit var binding: ActivityGlideBinding
    private lateinit var inAnimation: Animation
    private lateinit var outAnimation: Animation
    private lateinit var buttonAnimation: Animation
    private lateinit var imageAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAnimation()

        if(savedInstanceState == null) {
            startDefAnimation()
        }

        animationListener()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.changeImageButton.setOnClickListener {
            binding.loremTextView.startAnimation(outAnimation)
            binding.glideImage.startAnimation(outAnimation)
        }
    }

    private fun startDefAnimation() {
        binding.glideImage.startAnimation(imageAnimation)
        binding.changeImageButton.startAnimation(buttonAnimation)
    }

    private fun initAnimation() {
        inAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_in)
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_out)
        buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_anim)
        imageAnimation = AnimationUtils.loadAnimation(this, R.anim.image_anim)

        outAnimation.setAnimationListener(animationListener())
    }

    private fun animationListener(): Animation.AnimationListener {
       return object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                with(binding.loremTextView)  {
                    startAnimation(inAnimation)
                    text = application.getString(R.string.lorem2)
                }

                with(binding.glideImage) {
                    startAnimation(inAnimation)
                    Glide.with(this)
                        .load("https://source.unsplash.com/random/800x600")
                        .into(binding.glideImage)
                }
            }

            override fun onAnimationRepeat(p0: Animation?) {}
        }
    }
}