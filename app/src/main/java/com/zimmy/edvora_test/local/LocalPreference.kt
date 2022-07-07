package com.zimmy.edvora_test.local

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val STATE_FILTER_KEY = "state_filter"
private const val CITY_FILTER_KEY = "city_filter"

const val DEFAULT_STATE = "State Name"
const val DEFAULT_CITY = "City Name"

class PreferencesRepository private constructor(context: Context) {

   private val sharedPreferences =
      context.applicationContext.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)

   private val _stateFlow = MutableStateFlow(state)
   val stateFlow: StateFlow<String> = _stateFlow

   private val _cityFlow = MutableStateFlow(city)
   val cityFlow: StateFlow<String> = _cityFlow

   private val state: String
      get() = sharedPreferences.getString(STATE_FILTER_KEY, DEFAULT_STATE).toString()

   private val city: String
      get() = sharedPreferences.getString(CITY_FILTER_KEY, DEFAULT_CITY).toString()

   fun updateState(newState: String) {
      sharedPreferences.edit {
         putString(STATE_FILTER_KEY, newState)
      }
      _stateFlow.value = newState
   }

   fun updateCity(newCity: String) {
      sharedPreferences.edit {
         putString(CITY_FILTER_KEY, newCity)
      }
      _cityFlow.value = newCity
   }

   companion object {
      @Volatile
      private var INSTANCE: PreferencesRepository? = null

      fun getInstance(context: Context): PreferencesRepository {
         return INSTANCE ?: synchronized(this) {
            INSTANCE?.let {
               return it
            }

            val instance = PreferencesRepository(context)
            INSTANCE = instance
            instance
         }
      }
   }
}