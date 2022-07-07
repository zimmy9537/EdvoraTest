package com.zimmy.edvora_test.past

import androidx.lifecycle.ViewModel
import com.zimmy.edvora_test.network.RidesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PastViewModel @Inject constructor(
    private val ridesRepository: RidesRepository,
) : ViewModel() {
   val pastRides = ridesRepository.pastRides
   val stationCode = ridesRepository.stationCode
}