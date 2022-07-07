package com.zimmy.edvora_test.upcoming

import androidx.lifecycle.ViewModel
import com.zimmy.edvora_test.network.RidesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    private val ridesRepository: RidesRepository,
) : ViewModel() {
   val upcomingRides = ridesRepository.upcomingRides
   val stationCode = ridesRepository.stationCode
}