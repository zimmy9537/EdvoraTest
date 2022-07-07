package com.zimmy.edvora_test.network

import com.zimmy.edvora_test.Resource
import com.zimmy.edvora_test.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
   private val rideApiService: RideApiService,
) {

   suspend fun getUserData(): Resource<User> {
      return try {
         val user = rideApiService.getUser()//.await()
         Resource.success(user)
      } catch (e: Exception) {
         Resource.error("there's an Exception in user Api", null)
      }
   }

}