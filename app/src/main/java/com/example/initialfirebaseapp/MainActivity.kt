package com.example.initialfirebaseapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.initialfirebaseapp.adapter.PostAdapter
import com.example.initialfirebaseapp.database.DatabaseManager
import com.example.initialfirebaseapp.databinding.ActivityMainBinding
import com.example.initialfirebaseapp.manager.AuthManager
import com.example.initialfirebaseapp.manager.DatabaseHandler
import com.example.initialfirebaseapp.model.Post
import com.example.initialfirebaseapp.utils.Extensions.toast

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.apply {
            fabCreate.setOnClickListener {
                val intent = Intent(context, CreateActivity::class.java)
                resultLauncher.launch(intent)
            }

            ivLogout.setOnClickListener {
                AuthManager.signOut()
                callSignInActivity(context)
            }
        }

        apiLoadPosts()
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                // Load all posts...
                apiLoadPosts()
            }
        }

    private fun apiLoadPosts() {
        showLoading(this)
        DatabaseManager.apiLoadPosts(object : DatabaseHandler {
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                dismissLoading()
                refreshAdapter(posts)
                Log.d("TAG", "onSuccess: $posts")
            }

            override fun onError() {
                toast("Problem in loading posts")
            }

        })
    }

    private fun refreshAdapter(posts: ArrayList<Post>) {
        val adapter = PostAdapter(posts)
        binding.rvPost.adapter = adapter
    }
}