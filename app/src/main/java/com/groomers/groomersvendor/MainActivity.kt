package com.groomers.groomersvendor

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.groomers.groomersvendor.databinding.ActivityMainBinding
import me.ibrahimsn.lib.SmoothBottomBar
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
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Attach the BottomNavigationView to the NavController
        bottomNav.setupWithNavController(navController)

        // Update the toolbar title dynamically based on the selected fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.tvTitle.text = when (destination.id) {
                R.id.homeFragment -> getString(R.string.dashboard)
                R.id.addPostFragment -> getString(R.string.post_title)
                R.id.orderListFragment -> getString(R.string.order_list)
                else -> getString(R.string.my_profile)
            }
        }
    }
}
