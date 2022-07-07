package com.zimmy.edvora_test.nearest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zimmy.edvora_test.RideAdapter
import com.zimmy.edvora_test.databinding.FragmentNearestBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NearestFragment : Fragment() {

   private var _binding: FragmentNearestBinding? = null
   private val binding get() = _binding!!
   private val adapter by lazy { RideAdapter() }
   private val viewModel: NearestViewModel by viewModels()

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?,
   ): View? {
      _binding = FragmentNearestBinding.inflate(inflater, container, false)

      binding.nearestList.adapter = adapter

      lifecycleScope.launch {
         launch {
            viewModel.stationCode.collectLatest {
               Log.d("StationCode Nearest", it.toString())
               adapter.updateStationCode(it)
            }
         }
         launch {
            viewModel.nearestRides.collectLatest {
               adapter.submitList(it)
            }
         }
      }

      return binding.root
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}