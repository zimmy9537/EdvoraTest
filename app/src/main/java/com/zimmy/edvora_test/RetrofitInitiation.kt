package com.zimmy.edvora_test

import android.content.Context
import com.zimmy.edvora_test.local.PreferencesRepository
import com.zimmy.edvora_test.network.RideApiService
import com.zimmy.edvora_test.network.RidesRepository
import com.zimmy.edvora_test.network.UserRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInitiation {
   private const val BASE_URL = "https://assessment.api.vweb.app"

   @Singleton
   @Provides
   fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
      .readTimeout(100, TimeUnit.SECONDS)
      .connectTimeout(100, TimeUnit.SECONDS)
      .build()

   @Singleton
   @Provides
   fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(GsonConverterFactory.create(
         GsonBuilder().setLenient().create())
      ).build()

   @Singleton
   @Provides
   fun provideApiService(retrofit: Retrofit): RideApiService =
      retrofit.create(RideApiService::class.java)

   @Singleton
   @Provides
   fun providesUserRepository(rideApiService: RideApiService) = UserRepository(rideApiService)

   @Singleton
   @Provides
   fun providesRidesRepository(rideApiService: RideApiService) = RidesRepository(rideApiService)

   @Singleton
   @Provides
   fun providesUserPreferencesRepository(@ApplicationContext appContext: Context) =
      PreferencesRepository.getInstance(appContext)
}