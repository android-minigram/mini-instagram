package com.motawfik.minigram.viewModels

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.motawfik.minigram.data.FirebaseStorage
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

class TimelineViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val firebaseStorage = FirebaseStorage()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // to cancel all coroutines when the view model is terminated
    }

    fun uploadNewImage(bitmap: Bitmap) {
        val bytesStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytesStream)
        val data = bytesStream.toByteArray()
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val path = firebaseStorage.uploadNewImage(data)
            }
        }
    }

    fun uploadSavedImage(uri: Uri) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val path = firebaseStorage.uploadSavedImage(uri)
            }
        }
    }
}