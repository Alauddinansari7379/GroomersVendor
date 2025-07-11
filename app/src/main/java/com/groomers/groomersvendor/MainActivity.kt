package com.groomers.groomersvendor


import android.annotation.SuppressLint
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.groomers.groomersvendor.databinding.ActivityMainBinding
import com.groomers.groomersvendor.helper.AppProgressBar
import com.groomers.groomersvendor.helper.NetworkChangeReceiver
import com.groomers.groomersvendor.helper.Toastic
import com.groomers.groomersvendor.model.modeladdbank.ModelAddBank
import com.groomers.groomersvendor.retrofit.ApiClient
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : Common(), NetworkChangeReceiver.ConnectivityListener {
    private lateinit var bottomNav: BottomNavigationView
    internal val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var wasPreviouslyConnected = true
    private val networkChangeReceiver = NetworkChangeReceiver()

    @Inject
    lateinit var sessionManager: SessionManager
    var count2 = 0
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

        Glide.with(this).load(ApiServiceProvider.IMAGE_URL + sessionManager.profilePictureUrl)
            .placeholder(R.drawable.user) // Use a default placeholder
            .into(binding.imgLan)

        val status = sessionManager.onlineOffline?.toString()?.takeIf { it.isNotBlank() }?.toIntOrNull() ?: 0

        if (status == 1) {
            binding.btnOnline.visibility = View.VISIBLE
            binding.btnOffline.visibility = View.GONE
        } else {
            binding.btnOnline.visibility = View.GONE
            binding.btnOffline.visibility = View.VISIBLE
        }

        binding.btnOnline.setOnClickListener {
            SweetAlertDialog(
                this,
                SweetAlertDialog.WARNING_TYPE
            ).setTitleText("Are you sure want to Offline?").setCancelText("No")
                .setConfirmText("Yes").showCancelButton(true).setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    apiCallOnlineOffline("0")

                }.setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }.show()
        }

        binding.btnOffline.setOnClickListener {
            SweetAlertDialog(
                this,
                SweetAlertDialog.WARNING_TYPE
            ).setTitleText("Are you sure want to Online?").setCancelText("No").setConfirmText("Yes")
                .showCancelButton(true).setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    apiCallOnlineOffline("1")

                }.setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }.show()
        }
        binding.imgLan.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, true) // Make sure it's the graph ID
                .build()
            navController?.navigate(R.id.profileFragment, null, navOptions)


        }
    }

    override fun onNetworkStatusChanged(isConnected: Boolean) {
        updateInternetStatus(isConnected)
    }

    private fun networkChangeReceiver() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, filter)
    }

    private fun updateInternetStatus(isConnected: Boolean) {
        if (isConnected && !wasPreviouslyConnected) {
            // Show "Connected" message only if previously disconnected
            showNoInternetLayout("Connected", R.color.green)
        } else if (!isConnected) {
            // Show "No internet connection" if disconnected
            showNoInternetLayout("No internet connection", R.color.black)
        }

        // Update the previous connection status
        wasPreviouslyConnected = isConnected
    }

    private fun showNoInternetLayout(message: String, color: Int) {
        checkNotNull(binding.includeNoInternet)
        binding.includeNoInternet.layoutNoInternetMain.visibility = View.VISIBLE
        binding.includeNoInternet.layoutNoInternetMain.setBackgroundColor(
            ContextCompat.getColor(
                this, color
            )
        )
        binding.includeNoInternet.tvNoInternetMessage.text = message

        // Apply the Slide-in animation from bottom
        val slideIn: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_up)
        binding.includeNoInternet.layoutNoInternetMain.startAnimation(slideIn)

        // Hide the layout after 3 seconds if connected
        if (color == R.color.green) {  // Connected
            // Set the image drawable for the connection icon
            binding.includeNoInternet.imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.connections
                )
            )

            Handler(Looper.getMainLooper()).postDelayed({
                // Slide-out animation when hiding the layout
                val slideOut: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_down)
                binding.includeNoInternet.layoutNoInternetMain.startAnimation(slideOut)
                binding.includeNoInternet.layoutNoInternetMain.visibility = View.GONE
            }, 1000) // Hide after 3 seconds
        } else {
            binding.includeNoInternet.imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.network_signal
                )
            )
        }
    }

    private fun apiCallOnlineOffline(statues: String) {
        AppProgressBar.showLoaderDialog(this)

        ApiClient.apiService.onlineOffline("Bearer ${sessionManager.accessToken}", statues)
            .enqueue(object : Callback<ModelAddBank> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAddBank>, response: Response<ModelAddBank>
                ) {
                    try {
                        if (response.code() == 200) {
                            count2 = 0
                            AppProgressBar.hideLoaderDialog()
                            if (statues == "1") {
                                sessionManager.onlineOffline = "1"
                                binding.btnOnline.visibility = View.VISIBLE
                                binding.btnOffline.visibility = View.GONE
                                Toastic.toastic(
                                    context = this@MainActivity,
                                    message = "Now you are Online !",
                                    duration = Toastic.LENGTH_SHORT,
                                    type = Toastic.SUCCESS,
                                    isIconAnimated = true,
                                    textColor = if (false) Color.BLUE else null,
                                ).show()
                            } else {
                                sessionManager.onlineOffline = "0"
                                binding.btnOnline.visibility = View.GONE
                                binding.btnOffline.visibility = View.VISIBLE
                                Toastic.toastic(
                                    context = this@MainActivity,
                                    message = "Now you are Offline !",
                                    duration = Toastic.LENGTH_SHORT,
                                    type = Toastic.SUCCESS,
                                    isIconAnimated = true,
                                    textColor = if (false) Color.BLUE else null,
                                ).show()

                            }

                        } else if (response.code() == 500) {
                            Toastic.toastic(
                                context = this@MainActivity,
                                message = "Server Error",
                                duration = Toastic.LENGTH_SHORT,
                                type = Toastic.ERROR,
                                isIconAnimated = true,
                                textColor = if (false) Color.BLUE else null,
                            ).show()
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            Toastic.toastic(
                                context = this@MainActivity,
                                message = "Something went wrong",
                                duration = Toastic.LENGTH_SHORT,
                                type = Toastic.ERROR,
                                isIconAnimated = true,
                                textColor = if (false) Color.BLUE else null,
                            ).show()
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toastic.toastic(
                            context = this@MainActivity,
                            message = "Something went wrong",
                            duration = Toastic.LENGTH_SHORT,
                            type = Toastic.ERROR,
                            isIconAnimated = true,
                            textColor = if (false) Color.BLUE else null,
                        ).show()
                    }


                }

                override fun onFailure(call: Call<ModelAddBank>, t: Throwable) {
                    count2++
                    if (count2 <= 3) {
                        apiCallOnlineOffline(statues)
                    } else {
                        Toastic.toastic(
                            context = this@MainActivity,
                            message = "Something went wrong",
                            duration = Toastic.LENGTH_SHORT,
                            type = Toastic.ERROR,
                            isIconAnimated = true,
                            textColor = if (false) Color.BLUE else null,
                        ).show()
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkChangeReceiver);
    }

    override fun onResume() {
        super.onResume()
        networkChangeReceiver();
    }

    private fun openFragment(fragmentTag: String, postId: String) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.hostFragment) as? NavHostFragment
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
