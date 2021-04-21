package com.bignerdranch.android.kotlin_point.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.kotlin_point.R
import com.bignerdranch.android.kotlin_point.data.PlacesItem

class RecyclerViewAdapter(private val list_: List<PlacesItem>) : RecyclerView.Adapter<RecyclerViewAdapter.ExampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.place_name, parent, false)
        return ExampleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
       holder.itemPlace.text = list_[position].name

    }

    override fun getItemCount() = list_.size

    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemPlace: TextView = itemView.findViewById(R.id.itemPlace)                     //здесь надо находить через findViewById..не работает dataBinding ?
    }
}