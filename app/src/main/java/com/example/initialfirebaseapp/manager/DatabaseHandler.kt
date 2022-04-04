package com.example.initialfirebaseapp.manager

import com.example.initialfirebaseapp.model.Post

interface DatabaseHandler {
    fun onSuccess(post: Post? = null, posts: ArrayList<Post> = ArrayList())
    fun onError()
}