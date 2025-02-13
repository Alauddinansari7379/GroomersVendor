package com.groomers.groomersvendor

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.groomers.groomersvendor.databinding.ActivityMainBinding
import com.groomers.groomersvendor.fragment.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : Common() {
    private lateinit var bottomNav: BottomNavigationView
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE
        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)
        val openFragment = intent.getStringExtra("openFragment")
        if (openFragment == "HomeFragment") {
            openHomeFragment()
        }
        bottomNav = binding.bottomNavigationView
        // Set up the NavHostFragment and NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        // Attach the BottomNavigationView to the NavController
        bottomNav.setupWithNavController(navController)

        // Update the toolbar title dynamically based on the selected fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.tvTitle.text = when (destination.id) {
                R.id.homeFragment -> getString(R.string.appointment)
                R.id.addPostFragment -> getString(R.string.add_post)
                R.id.financeFragment2 -> getString(R.string.finance)
                R.id.orderListFragment -> getString(R.string.marketing)
                else -> getString(R.string.shop_profile)
            }
        }
    }
    private fun openHomeFragment() {
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.hostFragment, homeFragment)
            .commit()
    }
}
