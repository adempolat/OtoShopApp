package com.adempolat.otovinnapp.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.adempolat.otovinnapp.R
import com.adempolat.otovinnapp.data.response.Story
import com.adempolat.otovinnapp.view.adapter.StoryPagerAdapter
import com.adempolat.otovinnapp.databinding.FragmentStoryDialogBinding

class StoryDialogFragment : DialogFragment() {

    private var _binding: FragmentStoryDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var stories: List<Story>
    private var initialPosition: Int = 0

    private val handler = Handler(Looper.getMainLooper())
    private var progressHandler: Handler? = null
    private var currentProgress = 0
    private var currentProgressBar: ProgressBar? = null

    private val updateRunnable = object : Runnable {
        override fun run() {
            if (binding.viewPagerStories.currentItem < stories.size - 1) {
                binding.viewPagerStories.currentItem += 1
            } else {
                dismiss() // Tüm hikayeler gösterildikten sonra dialogu kapat
            }
            handler.postDelayed(this, 5000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoryDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            stories = it.getParcelableArrayList("stories")!!
            initialPosition = it.getInt("position", 0)
        }

        val storyPagerAdapter = StoryPagerAdapter(stories)
        binding.viewPagerStories.adapter = storyPagerAdapter
        binding.viewPagerStories.setCurrentItem(initialPosition, false)

        setupProgressBars()
        startAutoSlide()
    }

    private fun setupProgressBars() {
        binding.progressIndicatorLayout.weightSum = stories.size.toFloat()
        for (i in stories.indices) {
            val progressBar = LayoutInflater.from(context).inflate(R.layout.item_progress_bar, binding.progressIndicatorLayout, false) as ProgressBar
            progressBar.max = 5000 // 5 saniye
            binding.progressIndicatorLayout.addView(progressBar)
        }

        binding.viewPagerStories.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateProgressBars(position)
                startProgress() // Progress animasyonunu her sayfa değişiminde başlat
            }
        })
    }

    private fun updateProgressBars(position: Int) {
        for (i in 0 until binding.progressIndicatorLayout.childCount) {
            val progressBar = binding.progressIndicatorLayout.getChildAt(i) as ProgressBar
            progressBar.progress = if (i < position) progressBar.max else 0
        }
        currentProgressBar = binding.progressIndicatorLayout.getChildAt(position) as ProgressBar
    }

    private fun startAutoSlide() {
        handler.postDelayed(updateRunnable, 5000)
        startProgress()
    }

    private fun startProgress() {
        progressHandler?.removeCallbacksAndMessages(null) // Var olan handler işlemlerini temizle
        currentProgress = 0
        progressHandler = Handler(Looper.getMainLooper())
        progressHandler?.postDelayed(object : Runnable {
            override fun run() {
                if (currentProgress < 5000) {
                    currentProgress += 50
                    currentProgressBar?.progress = currentProgress
                    progressHandler?.postDelayed(this, 50)
                }
            }
        }, 50)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateRunnable)
        progressHandler?.removeCallbacksAndMessages(null)
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
