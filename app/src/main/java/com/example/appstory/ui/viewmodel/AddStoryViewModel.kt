package com.example.appstory.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appstory.data.api.ApiService
import com.example.appstory.data.response.AddStoryResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddStoryViewModel(private val apiService: ApiService) : ViewModel() {

    private val _uploadSuccess = MutableLiveData<Unit>()
    val uploadSuccess: LiveData<Unit> = _uploadSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun uploadStory(token: String, description: String, imageUri: Uri) {
        val file = File(imageUri.path!!)
        val requestImage = createMultipartBody(file, "photo")

        apiService.addNewStory(
            token,
            description,
            requestImage,
            onSuccess = {
                _uploadSuccess.value = Unit
            },
            onError = { errorMessage ->
                _error.value = errorMessage
            }
        )
    }

    private fun createMultipartBody(file: File, paramName: String): MultipartBody.Part {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(paramName, file.name, requestFile)
    }
}
