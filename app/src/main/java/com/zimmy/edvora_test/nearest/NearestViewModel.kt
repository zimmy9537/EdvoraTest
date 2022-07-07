package com.zimmy.edvora_test.nearest

import androidx.lifecycle.ViewModel
import com.zimmy.edvora_test.network.RidesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NearestViewModel @Inject constructor(
    private val ridesRepository: RidesRepository,
) : ViewModel() {
   val nearestRides = ridesRepository.nearestRides
   val stationCode = ridesRepository.stationCode
}