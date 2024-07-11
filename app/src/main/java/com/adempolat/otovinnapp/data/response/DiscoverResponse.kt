package com.adempolat.otovinnapp.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class DiscoverResponse(
    val code: Int,
    val data: DiscoverData,
    val message: String
)

data class DiscoverData(
    val stories: List<Story>,
    val menu: List<MenuItem>,
    val slider: List<SliderItem>,
    val stations: List<StationCategory>
)
@Parcelize
data class Story(
    val id: String,
    val thumb: String,
    val type: String,
    val url: String
) : Parcelable

data class MenuItem(
    val id: String,
    val title: String
)

data class SliderItem(
    val id: String,
    val url: String
)

data class StationCategory(
    val tittle: String,
    val order: Int,
    val station: List<StationItem>
)

data class StationItem(
    val name: String,
    val image: String,
    val point: Int,
    var isFavorite: Boolean = false
)

