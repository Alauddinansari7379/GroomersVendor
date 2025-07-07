package com.groomers.groomersvendor.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.ehcf.Helper.myToast
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.Login
import com.groomers.groomersvendor.activity.ManageSlots
import com.groomers.groomersvendor.activity.Settings
import com.groomers.groomersvendor.activity.ViewOnGroomers
import com.groomers.groomersvendor.adapter.AdapterFinance
import com.groomers.groomersvendor.databinding.ActivityMainBinding
import com.groomers.groomersvendor.databinding.FragmentProfileBinding
import com.groomers.groomersvendor.helper.AppProgressBar
import com.groomers.groomersvendor.helper.Toastic
import com.groomers.groomersvendor.model.modelEarning.Earning
import com.groomers.groomersvendor.model.modelEarning.ModelEarning
import com.groomers.groomersvendor.model.modelEarning.Result
import com.groomers.groomersvendor.retrofit.ApiClient
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var sessionManager: SessionManager
    val REQUEST_CODE = 100
    var count = 0
    private val profileViewModel: ProfileViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private var isUpdatingProfilePicture = true // To track whether updating profile or cover
    private lateinit var binding1: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivityBinding = (requireActivity() as MainActivity).binding

        val navController =
            (requireActivity().supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment).navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.profileFragment) {
                mainActivityBinding.linearLayoutCompat.visibility = View.GONE
            } else {
                mainActivityBinding.linearLayoutCompat.visibility = View.VISIBLE
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFloating.setOnClickListener {
            startActivity(Intent(requireContext(), Settings::class.java))
        }
        binding.cardCreateSlot.setOnClickListener {
            startActivity(Intent(requireContext(), ManageSlots::class.java))
        }
        binding.cardHelpList.setOnClickListener {
            openDialog()
        }

        binding.llSlotlist.setOnClickListener {
            //  startActivity(Intent(requireContext(), MySlot::class.java))
        }

        binding.cameraBtn.setOnClickListener {
            isUpdatingProfilePicture = true
            openImageChooser()
        }

        binding.coverPhotoBtn.setOnClickListener {
            isUpdatingProfilePicture = false
            openImageChooser()
        }
        binding.addCoverButton.setOnClickListener {
            startActivity(Intent(requireContext(),ViewOnGroomers::class.java))
        }


        // Load profile and cover images
        Glide.with(requireContext())
            .load(ApiServiceProvider.IMAGE_URL + sessionManager.profilePictureUrl)
            .placeholder(R.drawable.user) // Default placeholder
            .into(binding.profileImage)

        Glide.with(requireContext())
            .load(ApiServiceProvider.IMAGE_URL + sessionManager.coverPictureUrl)
            .placeholder(Color.GRAY.toDrawable())
            .into(binding.coverPhoto)

        binding.userName.text = sessionManager.userName

        binding.cardLogout.setOnClickListener {
            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to logout?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    sessionManager.clearSession()
                    val intent = Intent(requireContext(), Login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    requireActivity().finish()
                    startActivity(intent)
                }
                .setCancelClickListener { sDialog -> sDialog.cancel() }
                .show()
        }

        binding.shareProfileButton.setOnClickListener {
            shareApp()
        }

        binding.cardFinanceCenter.setOnClickListener {
            apiCallFinanceData()
        }

        profileViewModel.uploadResult.observe(viewLifecycleOwner) { message ->
            if (message == "Uploading...") View.VISIBLE else View.GONE
            message?.let {
                Toastic.toastic(
                    context = requireActivity(),
                    message = it,
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.SUCCESS,
                    isIconAnimated = true,
                    textColor = Color.BLUE ,
                ).show()
            }
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri?.let { uri ->
                if (isUpdatingProfilePicture) {
                    showConfirmationDialog(uri, true) // Update profile picture
                } else {
                    showConfirmationDialog(uri, false) // Update cover photo
                }
            }
        }
    }

    private fun showConfirmationDialog(uri: Uri, isProfile: Boolean) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText(if (isProfile) "Update Profile Picture?" else "Update Cover Picture?")
            .setContentText("Do you want to set this image as your ${if (isProfile) "profile" else "cover"} picture?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
                val file = uriToFile(uri)

                if (isProfile) {
                    binding.profileImage.setImageURI(uri)
                    file?.let { profileViewModel.uploadProfilePicture(it) }
                } else {
                    binding.coverPhoto.setImageURI(uri)
                    file?.let { profileViewModel.uploadCoverPicture(it) }
                }
            }
            .setCancelClickListener { sDialog -> sDialog.dismissWithAnimation() }
            .show()
    }

    private fun uriToFile(uri: Uri): File? {
        val resolver = requireContext().contentResolver
        val fileDescriptor = resolver.openFileDescriptor(uri, "r")?.fileDescriptor ?: return null
        val inputStream = FileInputStream(fileDescriptor)
        val file = File(requireContext().cacheDir, "selected_image.jpg")
        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return file
    }

    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }

    private fun shareApp() {
        val appPackageName = getAppPackageName(requireContext())
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out this app!")
            putExtra(
                Intent.EXTRA_TEXT,
                "Hey, check out this amazing app: https://play.google.com/store/apps/details?id=$appPackageName"
            )
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    fun getAppPackageName(context: Context): String {
        return context.packageName
    }

    override fun onDestroy() {
        super.onDestroy()
        profileViewModel.clearRegisterData()
    }

    private fun openDialog() {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)

            val emailLayout = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }
            val mobileLayout = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }

            val emailText = TextView(context).apply { text = "Email: example@mail.com" }
            val emailCopyIcon = ImageView(context).apply {
                setImageResource(R.drawable.baseline_content_copy_24)
                layoutParams = LinearLayout.LayoutParams(50, 50).apply { // Making the icon smaller
                    setMargins(20, 0, 0, 0) // Adding margin between text and icon
                }
                setOnClickListener {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(
                        ClipData.newPlainText(
                            "Email",
                            emailText.text.toString().removePrefix("Email: ")
                        )
                    )
                    Toast.makeText(context, "Email copied!", Toast.LENGTH_SHORT).show()
                }
            }

            val mobileText = TextView(context).apply { text = "Mobile: +91-1234567890" }
            val mobileCopyIcon = ImageView(context).apply {
                setImageResource(R.drawable.baseline_content_copy_24)
                layoutParams = LinearLayout.LayoutParams(50, 50).apply { // Making the icon smaller
                    setMargins(20, 0, 0, 0) // Adding margin between text and icon
                }
                setOnClickListener {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(
                        ClipData.newPlainText(
                            "Mobile",
                            mobileText.text.toString().removePrefix("Mobile: ")
                        )
                    )
                    Toast.makeText(context, "Mobile number copied!", Toast.LENGTH_SHORT).show()
                }
            }

            val addressText = TextView(context).apply { text = "Address: Hyderabad, India" }

            emailLayout.addView(emailText)
            emailLayout.addView(emailCopyIcon)
            mobileLayout.addView(mobileText)
            mobileLayout.addView(mobileCopyIcon)

            addView(emailLayout)
            addView(mobileLayout)
            addView(addressText)
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Connect Information")
            .setView(layout)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }
    private fun apiCallFinanceData() {
        ApiClient.apiService.vendorEarning("Bearer ${sessionManager.accessToken}")
            .enqueue(object : Callback<ModelEarning> {
                override fun onResponse(
                    call: Call<ModelEarning>, response: Response<ModelEarning>
                ) {
                    try {
                        AppProgressBar.hideLoaderDialog()
                        when (response.code()) {
                            404 -> myToast(requireContext(), "Something went wrong", false)
                            500 -> myToast(requireContext(), "Server Error", false)
                            else -> {
                                val result = response.body()?.result
                                if (result != null) {
                                    showFinanceDialog(result)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(requireContext(), "Something went wrong", false)
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelEarning>, t: Throwable) {
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallFinanceData()
                    } else {
                        myToast(requireContext(), t.message.toString(), false)
                        AppProgressBar.hideLoaderDialog()
                    }
                }
            })
    }

    private fun captureLayoutAndShare(fragment: Context, view: View) {
        val context = fragment ?: return

        try {
            // Capture layout as bitmap
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)

            // Save the bitmap
            val file = File(context.externalCacheDir, "layout_capture_${System.currentTimeMillis()}.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // Get URI using FileProvider
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

            // Open share dialog with image preview
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*" // Ensures image preview in share menu
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "Check this out!") // Optional text
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            fragment.startActivity(Intent.createChooser(intent, "Share Layout Image"))

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to capture layout!", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showFinanceDialog(earningsList: Result) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_finance_data, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.dialogRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = AdapterFinance(requireContext(), earningsList)

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Close", null)
            .show()
    }


}
