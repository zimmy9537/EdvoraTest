package com.zimmy.edvora_test

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zimmy.edvora_test.databinding.RideItemBinding

import com.zimmy.edvora_test.model.Ride
import kotlin.math.abs

class RideAdapter : ListAdapter<Ride, RideAdapter.ViewHolder>(
   ProductDiffCallback()
) {
   private var currentStationCode = 0
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(
         RideItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bind(getItem(position), currentStationCode)
   }

   fun updateStationCode(stationCode: Int) {
      currentStationCode = stationCode
   }

   class ViewHolder(
      private val binding: RideItemBinding,
   ) : RecyclerView.ViewHolder(binding.root) {

      @SuppressLint("SetTextI18n")
      fun bind(currentRide: Ride, currentStationCode: Int) {
         with(binding) {
            cityName.text = currentRide.city
            stateName.text = currentRide.state
            rideId.text = "Ride Id : ${currentRide.id}"
            originStation.text = "Origin Station : ${currentRide.origin_station_code}"
            stationPath.text = "Station Path : [${currentRide.station_path.joinToString()}]"
            date.text = "Date : ${currentRide.date}"
            distance.text =
               "Distance : ${currentRide.station_path.minOf { abs(it - currentStationCode) }}"
            Glide.with(itemView.context).load(currentRide.map_url).into(mapImg)
         }
      }

   }
}

private class ProductDiffCallback : DiffUtil.ItemCallback<Ride>() {
   override fun areItemsTheSame(oldItem: Ride, newItem: Ride): Boolean {
      return oldItem.id == newItem.id
   }
   override fun areContentsTheSame(oldItem: Ride, newItem: Ride): Boolean {
      return oldItem == newItem
   }
}
