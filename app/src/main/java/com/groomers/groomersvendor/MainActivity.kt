package com.groomers.groomersvendor

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.groomers.groomersvendor.databinding.ActivityMainBinding
import com.groomers.groomersvendor.fragment.AddPostFragment
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
        val openFragment = intent.getStringExtra("openFragment")
        val id = intent.getStringExtra("id") ?: ""

        if (!openFragment.isNullOrEmpty() && id.isNotEmpty()) {
            openFragment(openFragment, id)
        }
    }



    private fun openFragment(fragmentTag: String, postId: String) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment) as? NavHostFragment
        val navController = navHostFragment?.navController

        if (navController == null) {
            // Log an error or handle the case where navController is not available
            return
        }

        val bundle = Bundle().apply {
            putString("postId", postId)
        }

        when (fragmentTag) {
            "HomeFragment" -> navController.navigate(R.id.homeFragment)
            "AddPostFragment" -> navController.navigate(R.id.addPostFragment, bundle)
        }
    }


}
