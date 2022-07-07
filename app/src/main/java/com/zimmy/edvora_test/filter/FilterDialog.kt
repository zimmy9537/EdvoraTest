package com.zimmy.edvora_test.filter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zimmy.edvora_test.databinding.FilterDialogBinding
import com.zimmy.edvora_test.local.DEFAULT_CITY
import com.zimmy.edvora_test.local.DEFAULT_STATE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterDialog : DialogFragment() {

   private var _binding: FilterDialogBinding? = null
   private val binding get() = _binding!!
   private val viewModel: FilterViewModel by viewModels()

   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      _binding = FilterDialogBinding.inflate(LayoutInflater.from(context))

      val customDialog = AlertDialog.Builder(requireContext(), 0).create()
      customDialog.apply {
         window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
         val windowsAttributes = window?.attributes
         windowsAttributes?.gravity = Gravity.TOP
         windowsAttributes?.y = 350
         windowsAttributes?.x = -40
         setView(binding.root)
      }

      binding.stateFilterTv.setOnClickListener {
         lifecycleScope.launch {
            viewModel.states.collectLatest { states ->
               showMenu(binding.stateFilter, states, FilterType.STATE)
            }
         }
      }

      binding.cityFilterTv.setOnClickListener {
         lifecycleScope.launch {
            viewModel.cities.collectLatest { cities ->
               showMenu(binding.cityFilter, cities, FilterType.CITY)
            }
         }
      }

      lifecycleScope.launch {
         launch {
            viewModel.currentState.collectLatest { state ->
               binding.stateFilterTv.text = state
            }
         }
         launch {
            viewModel.currentCity.collectLatest { city ->
               binding.cityFilterTv.text = city
            }
         }
      }

      return customDialog
   }

   private fun showMenu(v: View, menu: List<String>, filterType: FilterType) {
      val popup = PopupMenu(requireContext(), v, Gravity.END)

      popup.menu.add(filterType.default)
      menu.forEach { popup.menu.add(it) }

      popup.setOnMenuItemClickListener { menuItem: MenuItem ->
         when (filterType) {
            FilterType.STATE -> viewModel.updateState(menuItem.title.toString())
            FilterType.CITY -> viewModel.updateCity(menuItem.title.toString())
         }
         true
      }
      popup.show()
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }

   enum class FilterType(val default: String) {
      STATE(DEFAULT_STATE),
      CITY(DEFAULT_CITY)
   }
}