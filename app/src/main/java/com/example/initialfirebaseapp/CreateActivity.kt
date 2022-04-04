package com.example.initialfirebaseapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.initialfirebaseapp.database.DatabaseManager
import com.example.initialfirebaseapp.databinding.ActivityCreateBinding
import com.example.initialfirebaseapp.manager.DatabaseHandler
import com.example.initialfirebaseapp.manager.StorageHandler
import com.example.initialfirebaseapp.manager.StoreManager
import com.example.initialfirebaseapp.model.Post
import com.example.initialfirebaseapp.utils.Extensions.toast
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import java.lang.Exception

class CreateActivity : BaseActivity() {

    lateinit var binding: ActivityCreateBinding
    var pickedImage: Uri? = null
    var allImages = ArrayList<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.apply {
            btnCreate.setOnClickListener {
                val title = edtTitle.text.toString().trim()
                val body = edtBody.text.toString().trim()
                val post = Post(title, body)
                storePost(post)
            }

            ivPhoto.setOnClickListener {
                pickUserImage()
            }

            ivClose.setOnClickListener {
                finish()
            }

            ivCamera.setOnClickListener {
                startCamera()
            }
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                toast("${it.data!!.data}")
            }
        }

    private fun storePost(post: Post) {
        if (pickedImage != null) {
            storeStorage(post = post)
        } else {
            storeToDatabase(post)
        }
    }

    private fun storeStorage(post: Post) {
        showLoading(this)
        StoreManager.uploadPhoto(pickedImage!!, object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                post.img = imgUrl
                storeToDatabase(post)
            }

            override fun onError(exception: Exception?) {
                storeToDatabase(post)
            }
        })
    }

    private fun storeToDatabase(post: Post) {
        DatabaseManager.storePost(post, object : DatabaseHandler {
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                toast("Post is saved")
                dismissLoading()
                finishIntent()
            }

            override fun onError() {
                dismissLoading()
                toast("Post saving failed")
            }
        })
    }

    private fun finishIntent() {
        val returnIntent = intent
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    private fun pickUserImage() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setSelectedImages(allImages)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                allImages =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                pickedImage = allImages[0]
                binding.ivPhoto.setImageURI(pickedImage)
            }
        }

}