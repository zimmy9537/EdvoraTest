package com.zimmy.edvora_test.network

import com.zimmy.edvora_test.model.Ride
import com.zimmy.edvora_test.model.User
import retrofit2.http.GET

interface RideApiService {
   @GET("/user")
   suspend fun getUser(): User

   @GET("/rides")
   suspend fun getRides(): List<Ride>
}