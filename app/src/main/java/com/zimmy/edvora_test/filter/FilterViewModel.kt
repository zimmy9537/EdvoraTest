package com.zimmy.edvora_test.filter

import androidx.lifecycle.ViewModel
import com.zimmy.edvora_test.local.PreferencesRepository
import com.zimmy.edvora_test.network.RidesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
   private val ridesRepository: RidesRepository,
   private val PreferencesRepository: PreferencesRepository,
) : ViewModel() {

   val states = ridesRepository.stateNames
   val cities = ridesRepository.cityNames

   val currentState = PreferencesRepository.stateFlow
   val currentCity = PreferencesRepository.cityFlow

   fun updateState(state: String) {
      PreferencesRepository.updateState(state)
      updateRides()
   }

   fun updateCity(city: String) {
      PreferencesRepository.updateCity(city)
      updateRides()
   }

   private fun updateRides() {
      ridesRepository.filterRides(currentState.value, currentCity.value)
   }

}