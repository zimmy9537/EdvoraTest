package com.zimmy.edvora_test.network

import com.zimmy.edvora_test.local.DEFAULT_CITY
import com.zimmy.edvora_test.local.DEFAULT_STATE
import com.zimmy.edvora_test.model.Ride
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import javax.inject.Inject
import kotlin.math.abs

class RidesRepository @Inject constructor(
   private val rideApiService: RideApiService,
) {

   private fun stringToTimeStamp(stringDate: String): Long {
      val format = SimpleDateFormat("MM/dd/yyyy hh:mm a")
      val date = format.parse(stringDate)
      return date?.time ?: 0
   }

   private val _stateNames = MutableStateFlow<List<String>>(emptyList())
   val stateNames = _stateNames.asStateFlow()

   private val _cityNames = MutableStateFlow<List<String>>(emptyList())
   val cityNames = _cityNames.asStateFlow()

   private val _stationCode = MutableStateFlow<Int>(0)
   val stationCode = _stationCode.asStateFlow()

   private val _tapsCount = MutableStateFlow<Pair<Int, Int>>(Pair(0, 0))
   val tapsCount = _tapsCount.asStateFlow()

   private val _upcomingRides = MutableStateFlow<List<Ride>>(emptyList<Ride>())
   val upcomingRides = _upcomingRides.asStateFlow()

   private val _pastRides = MutableStateFlow<List<Ride>>(emptyList<Ride>())
   val pastRides = _pastRides.asStateFlow()

   private val _nearestRides = MutableStateFlow<List<Ride>>(emptyList<Ride>())
   val nearestRides = _nearestRides.asStateFlow()

   private val allRides = MutableStateFlow<List<Ride>>(emptyList<Ride>())

   suspend fun updateRides(
      currentStationCode: Int = stationCode.value,
      state: String,
      city: String,
   ) {
      try {
         val rides = rideApiService.getRides()
         _stationCode.emit(currentStationCode)
         allRides.value = rides
         updateStateNames(rides)
         updateCityNames(rides)
         filterRides(state, city)
      } catch (e: Exception) {
      }
   }

   private fun updateAllRides(rides: List<Ride>) {
      updateNearestRides(rides)
      updateUpcomingRides(rides)
      updatePastRides(rides)
   }

   private fun updateUpcomingRides(rides: List<Ride>) {
      val currentTime = System.currentTimeMillis()
      val upcoming = rides.filter { stringToTimeStamp(it.date) >= currentTime }
         .sortedBy { ride -> ride.station_path.minOf { abs(it - stationCode.value) } }
      _upcomingRides.value = upcoming
      _tapsCount.value = Pair(upcoming.size, pastRides.value.size)
   }

   private fun updatePastRides(rides: List<Ride>) {
      val currentTime = System.currentTimeMillis()
      val past = rides.filter { stringToTimeStamp(it.date) < currentTime }
         .sortedBy { ride -> ride.station_path.minOf { abs(it - stationCode.value) } }
      _pastRides.value = past
      _tapsCount.value = Pair(upcomingRides.value.size, past.size)
   }

   private fun updateNearestRides(rides: List<Ride>) {
      val nearest =
         rides.sortedBy { ride -> ride.station_path.minOf { abs(it - stationCode.value) } }
      _nearestRides.value = nearest
   }

   private fun updateStateNames(rides: List<Ride>) {
      val states = rides.map { it.state }.toSet().toList()
      _stateNames.value = states
   }

   private fun updateCityNames(rides: List<Ride>) {
      val cities = rides.map { it.city }.toSet().toList()
      _cityNames.value = cities
   }

   private fun List<Ride>.filterState(state: String): List<Ride> {
      return if (state != DEFAULT_STATE)
         this.filter { it.state == state }
      else this
   }

   private fun List<Ride>.filterCity(city: String): List<Ride> {
      return if (city != DEFAULT_CITY)
         this.filter { it.city == city }
      else this
   }

   fun filterRides(state: String, city: String) {
      val filteredRides = allRides.value.filterState(state).filterCity(city)
      updateAllRides(filteredRides)
   }


}