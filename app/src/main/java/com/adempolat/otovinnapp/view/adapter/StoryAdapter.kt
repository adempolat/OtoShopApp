package com.adempolat.otovinnapp.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adempolat.otovinnapp.data.response.Story
import com.adempolat.otovinnapp.databinding.ItemStoryBinding
import com.adempolat.otovinnapp.utils.loadCircularImage
import androidx.fragment.app.Fragment
import com.adempolat.otovinnapp.view.StoryDialogFragment

class StoryAdapter(private val stories: List<Story>, private val fragment: Fragment) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position], position)
    }

    override fun getItemCount(): Int = stories.size

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story, position: Int) {
            binding.imageViewStory.loadCircularImage(story.thumb)

            binding.root.setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelableArrayList("stories", ArrayList(stories))
                    putInt("position", position)
                }
                val fragmentManager = fragment.requireActivity().supportFragmentManager
                val storyDialogFragment = StoryDialogFragment()
                storyDialogFragment.arguments = bundle
                storyDialogFragment.show(fragmentManager, "storyDialog")
            }
        }
    }
}
