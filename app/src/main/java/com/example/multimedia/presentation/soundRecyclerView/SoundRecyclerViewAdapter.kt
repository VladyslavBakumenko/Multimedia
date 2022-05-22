package com.example.multimedia.presentation.soundRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.multimedia.R
import com.example.multimedia.data.SoundModel

class SoundRecyclerViewAdapter: RecyclerView.Adapter<SoundItemViewHolder>() {

    var soundList: List<SoundModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onSoundClickListener: OnSoundClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.sound_item,
            parent,
            false
        )
        return SoundItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoundItemViewHolder, position: Int) {
        holder.bind(soundList[position], onSoundClickListener)
    }

    override fun getItemCount(): Int = soundList.size

    interface OnSoundClickListener {
        fun onSoundClickListener(soundModel: SoundModel)
    }
}