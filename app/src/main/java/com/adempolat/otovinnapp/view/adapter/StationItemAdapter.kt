package com.adempolat.otovinnapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adempolat.otovinnapp.R
import com.adempolat.otovinnapp.data.response.StationItem
import com.adempolat.otovinnapp.databinding.ItemStationBinding
import com.adempolat.otovinnapp.utils.loadImage

class StationItemAdapter(private val stationItems: List<StationItem>) : RecyclerView.Adapter<StationItemAdapter.StationItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationItemViewHolder {
        val binding = ItemStationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StationItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StationItemViewHolder, position: Int) {
        holder.bind(stationItems[position])
    }

    override fun getItemCount(): Int = stationItems.size

    inner class StationItemViewHolder(private val binding: ItemStationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stationItem: StationItem) {
            binding.imageViewStation.loadImage(stationItem.image)
            binding.textViewStationName.text = stationItem.name

            // Yıldızlar ile point gösterimi
            val starViews = listOf(
                binding.imageViewStar1,
                binding.imageViewStar2,
                binding.imageViewStar3,
                binding.imageViewStar4,
                binding.imageViewStar5
            )

            for (i in starViews.indices) {
                if (i < stationItem.point) {
                    starViews[i].setImageResource(R.drawable.baseline_star_24)
                } else {
                    starViews[i].setImageResource(R.drawable.star_24px)
                }
            }

            binding.lyFavorite.setOnClickListener {
                stationItem.isFavorite = !stationItem.isFavorite
                updateFavoriteUI(stationItem)
            }
            updateFavoriteUI(stationItem)
        }

        private fun updateFavoriteUI(stationItem: StationItem) {
            if (stationItem.isFavorite) {
                binding.favoriteIcon.setImageResource(R.drawable.ic_favorite)
                binding.favoriteText.text = binding.root.context.getString(R.string.remove_from_favorites)
            } else {
                binding.favoriteIcon.setImageResource(R.drawable.favorite_24px)
                binding.favoriteText.text = binding.root.context.getString(R.string.add_favorites)
            }
        }
    }
}
