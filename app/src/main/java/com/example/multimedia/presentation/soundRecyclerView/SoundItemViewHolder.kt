package com.example.multimedia.presentation.soundRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.multimedia.data.SoundModel
import com.example.multimedia.databinding.SoundItemBinding

class SoundItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var binding = SoundItemBinding.bind(view)
    var soundName = binding.soundName
    private val item = binding.item

    fun bind(
        soundModel: SoundModel,
        onMovieClickListener: SoundRecyclerViewAdapter.OnSoundClickListener?
    ) {
        soundName.text = soundModel.songName
        item.setOnClickListener {
            onMovieClickListener?.onSoundClickListener(soundModel)
        }
    }
}