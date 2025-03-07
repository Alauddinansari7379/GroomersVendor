package com.groomers.groomersvendor.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.Login
import com.groomers.groomersvendor.activity.ManageSlots
import com.groomers.groomersvendor.activity.MySlot
import com.groomers.groomersvendor.activity.Settings
import com.groomers.groomersvendor.databinding.FragmentProfileBinding
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

        binding.llSlotlist.setOnClickListener {
            startActivity(Intent(requireContext(), MySlot::class.java))
        }

        binding.cameraBtn.setOnClickListener {
            openImageChooser()
        }
        Glide.with(requireContext())
            .load(ApiServiceProvider.IMAGE_URL+sessionManager.profilePictureUrl)
            .placeholder(R.drawable.user) // Use a default placeholder
            .into(binding.userProfile)
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

        profileViewModel.uploadResult.observe(viewLifecycleOwner) { message ->
            binding.progressBar.visibility = if (message == "Uploading...") View.VISIBLE else View.GONE
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
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
            binding.userProfile.setImageURI(selectedImageUri)
            selectedImageUri?.let { uri ->
                showConfirmationDialog(uri)
            }
        }
    }
    private fun showConfirmationDialog(uri: Uri) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Update Profile Picture?")
            .setContentText("Do you want to set this image as your profile picture?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
                binding.userProfile.setImageURI(uri) // Set the selected image
                val file = uriToFile(uri)
                file?.let { profileViewModel.uploadProfilePicture(it) }
            }
            .setCancelClickListener { sDialog -> sDialog.dismissWithAnimation() }
            .show()
    }

    private fun uriToFile(uri: Uri): File? {
        val resolver = requireContext().contentResolver
        val fileDescriptor = resolver.openFileDescriptor(uri, "r")?.fileDescriptor ?: return null
        val inputStream = FileInputStream(fileDescriptor)
        val file = File(requireContext().cacheDir, "profile_image.jpg")
        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return file
    }

    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }

    override fun onDestroy() {
        super.onDestroy()
        profileViewModel.clearRegisterData()
    }
}
