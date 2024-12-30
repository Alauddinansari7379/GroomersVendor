package com.groomers.groomersvendor.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.groomers.groomersvendor.R



class AddPostFragment : Fragment(R.layout.fragment_add_post) {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var btnAddImage: TextView
    private lateinit var imageViewPreview: ImageView

    private var selectedImageUri: Uri? = null

    // Define image picker activity result launcher
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            imageViewPreview.setImageURI(it)
            imageViewPreview.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        editTextTitle = view.findViewById(R.id.editTextTitle)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        btnAddImage = view.findViewById(R.id.btnAddImage)
        imageViewPreview = view.findViewById(R.id.imageViewPreview)

        // Button click to add image
        btnAddImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }


}
