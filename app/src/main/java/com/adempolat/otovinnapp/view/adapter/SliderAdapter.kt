package com.adempolat.otovinnapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adempolat.otovinnapp.data.response.SliderItem
import com.adempolat.otovinnapp.databinding.ItemSliderBinding
import com.adempolat.otovinnapp.utils.loadImage

class SliderAdapter(private val sliderItems: List<SliderItem>) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(sliderItems[position])
    }

    override fun getItemCount(): Int = sliderItems.size

    inner class SliderViewHolder(private val binding: ItemSliderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sliderItem: SliderItem) {
            binding.imageViewSlider.loadImage(sliderItem.url)
        }
    }
}
