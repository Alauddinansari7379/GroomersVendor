package com.groomers.groomersvendor.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.Login
import com.groomers.groomersvendor.activity.ManageSlots
import com.groomers.groomersvendor.activity.MySlot
import com.groomers.groomersvendor.activity.Settings
import com.groomers.groomersvendor.databinding.ActivityMainBinding
import com.groomers.groomersvendor.databinding.FragmentProfileBinding
import com.groomers.groomersvendor.helper.Toastic
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var sessionManager: SessionManager

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

        profileViewModel.uploadResult.observe(viewLifecycleOwner) { message ->
           if (message == "Uploading...") View.VISIBLE else View.GONE
            message?.let {
                Toastic.toastic(
                    context = requireActivity(),
                    message = it,
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.SUCCESS,
                    isIconAnimated = true,
                    textColor = if (false) Color.BLUE else null,
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
            putExtra(Intent.EXTRA_TEXT, "Hey, check out this amazing app: https://play.google.com/store/apps/details?id=$appPackageName")
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
}
