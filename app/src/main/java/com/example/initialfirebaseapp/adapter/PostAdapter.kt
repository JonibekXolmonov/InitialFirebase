package com.example.initialfirebaseapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.initialfirebaseapp.R
import com.example.initialfirebaseapp.databinding.ItemPostBinding
import com.example.initialfirebaseapp.model.Post

class PostAdapter(var items: ArrayList<Post>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post: Post = items[position]
        if (holder is PostViewHolder) {
            Log.d("TAG", "onBindViewHolder: $post")
            holder.tvTitle.text = post.title!!.uppercase()
            holder.tvBody.text = post.body
            if (post.img != "") {
                Glide.with(holder.ivPhoto.context)
                    .load(post.img)
                    .into(holder.ivPhoto)
            }
        }
    }

    inner class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvTitle: TextView = binding.tvTitle
        var tvBody: TextView = binding.tvBody
        var ivPhoto: ImageView = binding.ivPhoto
    }
}