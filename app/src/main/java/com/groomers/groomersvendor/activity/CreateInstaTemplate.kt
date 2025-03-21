package com.groomers.groomersvendor.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.adapter.TemplateAdapter
import com.groomers.groomersvendor.databinding.ActivityCreateInstaTemplateBinding
import java.io.File
import java.io.FileOutputStream



class CreateInstaTemplate : AppCompatActivity() {
    private lateinit var binding: ActivityCreateInstaTemplateBinding

    // Template layout resource IDs
    private val templates = listOf(
        R.layout.template1,
        R.layout.template2,
        R.layout.template3
    )

    private var selectedTemplate: Int? = null // Store selected template

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateInstaTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView with templates
        val adapter = TemplateAdapter(templates) { selected ->
            selectedTemplate = selected
            binding.shareButton.visibility = View.VISIBLE // Show share button
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter

        // Share button functionality
        binding.shareButton.setOnClickListener {
            selectedTemplate?.let { templateRes ->
                captureAndShareScreenshot(templateRes)
            } ?: showToast("Please select a template!")
        }
    }

    private fun captureAndShareScreenshot(templateRes: Int) {
        // Inflate the selected template inside a container
        val container = FrameLayout(this)
        val view = LayoutInflater.from(this).inflate(templateRes, container, false)
        container.addView(view)

        // Convert the layout into a Bitmap
        val bitmap = getBitmapFromView(container)

        // Save the bitmap as an image and get its URI
        val imageUri = saveImageToCache(bitmap)

        // Share the image via Instagram
        if (imageUri != null) {
            shareImageToInstagram(imageUri)
        } else {
            showToast("Error saving image!")
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val width = 1080
        val height = 1350
        view.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )
        view.layout(0, 0, width, height)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveImageToCache(bitmap: Bitmap): Uri? {
        val file = File(cacheDir, "shared_template.png")
        return try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

            val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
            grantUriPermission("com.instagram.android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            uri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun shareImageToInstagram(imageUri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Ensure permission is granted
        }

        val chooser = Intent.createChooser(shareIntent, "Share to Instagram")

        try {
            startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            showToast("Instagram is not installed!")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
