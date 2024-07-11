package com.adempolat.otovinnapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adempolat.otovinnapp.data.response.Story
import com.bumptech.glide.Glide
import com.adempolat.otovinnapp.databinding.ItemStoryFullBinding
import com.adempolat.otovinnapp.utils.loadStoryImage

class StoryPagerAdapter(private val stories: List<Story>) : RecyclerView.Adapter<StoryPagerAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryFullBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size

    inner class StoryViewHolder(private val binding: ItemStoryFullBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.imageViewStory.loadStoryImage(story.url)
        }
    }
}
