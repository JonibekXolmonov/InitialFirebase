package com.example.initialfirebaseapp.manager

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

class StoreManager {
    companion object {
        private val storage = FirebaseStorage.getInstance()
        var storageRef = storage.reference
        var photoRef = storageRef.child("photos")

        fun uploadPhoto(uri: Uri, handler: StorageHandler) {
            val filename = System.currentTimeMillis().toString() + ".png"
            val uploadTask = photoRef.child(filename).putFile(uri)
            uploadTask.addOnSuccessListener { task ->
                val result = task.metadata!!.reference!!.downloadUrl
                result.addOnSuccessListener { url ->
                    val imgUrl = url.toString()
                    handler.onSuccess(imgUrl)
                }.addOnFailureListener { e ->
                    handler.onError(e)
                }
            }.addOnFailureListener { e ->
                handler.onError(e)
            }
        }
    }
}