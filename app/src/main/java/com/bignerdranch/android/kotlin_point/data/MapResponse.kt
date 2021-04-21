package com.bignerdranch.android.kotlin_point.data

data class MapResponse(
	val places: List<PlacesItem?>? = null
)

data class PlacesItem(
	val lng: Double? = null,
	val name: String? = null,
	val id: Int? = null,
	val lat: Double? = null
)

