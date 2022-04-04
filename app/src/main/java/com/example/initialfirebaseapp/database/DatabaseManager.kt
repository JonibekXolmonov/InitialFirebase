package com.example.initialfirebaseapp.database

import android.util.Log
import com.example.initialfirebaseapp.manager.DatabaseHandler
import com.example.initialfirebaseapp.model.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DatabaseManager {
    companion object {
        private var database = FirebaseDatabase.getInstance().reference
        private var reference = database.child("posts")

        fun storePost(post: Post, handler: DatabaseHandler) {
            val key = reference.push().key ?: return
            post.id = key
            reference.child(key).setValue(post)
                .addOnSuccessListener {
                    handler.onSuccess()
                }.addOnFailureListener {
                    handler.onError()
                }
        }
        fun apiLoadPosts(handler: DatabaseHandler) {
            reference.addValueEventListener(object : ValueEventListener {
                var posts = ArrayList<Post>()
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    if (datasnapshot.exists()) {
                        for (snapshot in datasnapshot.children) {
                            val post = snapshot.getValue(Post::class.java)
                            post.let {
                                posts.add(post!!)
                            }
                        }
                        handler.onSuccess(posts = posts)
                    } else {
                        handler.onSuccess(posts = ArrayList())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    handler.onError()
                }

            })
        }
    }
}