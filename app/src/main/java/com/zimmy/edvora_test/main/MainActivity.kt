package com.zimmy.edvora_test.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.zimmy.edvora_test.filter.FilterDialog
import com.zimmy.edvora_test.nearest.NearestFragment
import com.zimmy.edvora_test.past.PastFragment
import com.zimmy.edvora_test.upcoming.UpcomingFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.zimmy.edvora_test.R
import com.zimmy.edvora_test.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMainBinding
   private val PAGES_NUMBER = 3
   private val tabTitles = arrayOf(R.string.nearest, R.string.upcoming, R.string.past)

   private val viewModel: MainViewModel by viewModels()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityMainBinding.inflate(layoutInflater)
      setContentView(binding.root)

      binding.filters.setOnClickListener {
         FilterDialog().show(supportFragmentManager, "FilterDialog")
      }

      setupTapLayoutAndViewPager()
      readUserDate()
   }

   private fun setupTapLayoutAndViewPager() {
      val pagerAdapter = ScreenSlidePagerAdapter(this)
      binding.pager.adapter = pagerAdapter

      TabLayoutMediator(binding.tabLayout, binding.pager)
      { tab, position ->
         tab.text = getTabTitle(position)//this.getString(tabTitles[position])
      }.attach()
   }

   private fun readUserDate() {
      lifecycleScope.launch {
         viewModel.user.collect { it ->
            it?.let { user ->
               Glide.with(this@MainActivity).load(user.url).into(binding.profileImage)
               binding.userName.text = user.name
               viewModel.updateRides(it.station_code)

               viewModel.tapsCount.collectLatest { pair ->
                  binding.tabLayout.getTabAt(1)?.text = getTabTitle(1, pair.first)
                  binding.tabLayout.getTabAt(2)?.text = getTabTitle(2, pair.second)
               }
            }
         }
      }
   }

   private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
      override fun getItemCount(): Int = PAGES_NUMBER

      override fun createFragment(position: Int): Fragment = when (position) {
         0 -> NearestFragment()
         1 -> UpcomingFragment()
         else -> PastFragment()
      }
   }

   private fun getTabTitle(position: Int, ridesNumber: Int = 0): String {
      return this.getString(tabTitles[position]) +
              if ( position != 0) " ($ridesNumber)" else ""
   }

}