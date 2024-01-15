package com.example.appstory.ui.dashboard

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.appstory.R
import com.example.appstory.data.api.ApiService
import com.example.appstory.ui.viewmodel.AddStoryViewModel
import com.example.appstory.utils.SharedPreferencesHelper
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddStoryActivity : AppCompatActivity() {

    private lateinit var viewImage: ImageView
    private lateinit var btnCamera: Button
    private lateinit var btnGallery: Button
    private lateinit var addDesc: TextInputEditText
    private lateinit var btnUpload: Button
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var viewModel: AddStoryViewModel

    private var selectedImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)
        supportActionBar?.title = "Add New Story"

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        viewModel = ViewModelProvider(this)[AddStoryViewModel::class.java]

        btnCamera = findViewById(R.id.btnCamera)
        btnGallery = findViewById(R.id.btnGallery)
        btnUpload = findViewById(R.id.btnUpload)
        addDesc = findViewById(R.id.addDesc)
        viewImage = findViewById(R.id.viewImage)

        btnCamera.setOnClickListener {
            openCamera()
        }

        btnGallery.setOnClickListener {
            openGallery()
        }

        btnUpload.setOnClickListener {
            val description = addDesc.text.toString().trim()
            if (description.isNotEmpty() && selectedImageUri != null) {
                uploadStory(description, selectedImageUri!!)
                navigateToMainScreen()
            } else {
                Toast.makeText(
                    this,
                    "Please enter a description and select an image",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.uploadSuccess.observe(this) {
            Toast.makeText(
                this@AddStoryActivity,
                "Story uploaded successfully",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(
                this@AddStoryActivity,
                "Failed to upload story: $errorMessage",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraActivityResultLauncher.launch(takePictureIntent)
    }

    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val imageBitmap = data?.extras?.get("data") as Bitmap
                viewImage.setImageBitmap(imageBitmap)
                selectedImageUri = saveImageToInternalStorage(imageBitmap)
            }
        }

    private fun openGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryActivityResultLauncher.launch(pickPhotoIntent)
    }

    private val galleryActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val imageUri = data?.data
                if (imageUri != null) {
                    val imageBitmap = getBitmapFromUri(imageUri)
                    viewImage.setImageBitmap(imageBitmap)
                    selectedImageUri = imageUri
                }
            }
        }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(contentResolver, uri)
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${System.currentTimeMillis()}.jpg")

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return FileProvider.getUriForFile(
            this@AddStoryActivity,
            "$packageName.provider",
            file
        )
    }

    private fun uploadStory(description: String, imageUri: Uri) {
        val token = sharedPreferencesHelper.getString("token", "")
        viewModel.uploadStory(token, description, imageUri)
    }
    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
