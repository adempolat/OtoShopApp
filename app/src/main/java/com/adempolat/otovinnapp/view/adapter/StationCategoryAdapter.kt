package com.adempolat.otovinnapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adempolat.otovinnapp.data.response.StationCategory
import com.adempolat.otovinnapp.databinding.ItemStationCategoryBinding

class StationCategoryAdapter(private val stationCategories: List<StationCategory>) : RecyclerView.Adapter<StationCategoryAdapter.StationCategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationCategoryViewHolder {
        val binding = ItemStationCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StationCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StationCategoryViewHolder, position: Int) {
        holder.bind(stationCategories[position])
    }

    override fun getItemCount(): Int = stationCategories.size

    inner class StationCategoryViewHolder(private val binding: ItemStationCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stationCategory: StationCategory) {
            binding.textViewStationTitle.text = stationCategory.tittle

            binding.recyclerViewStationItems.layoutManager = LinearLayoutManager(binding.recyclerViewStationItems.context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewStationItems.adapter = StationItemAdapter(stationCategory.station)
        }
    }
}
