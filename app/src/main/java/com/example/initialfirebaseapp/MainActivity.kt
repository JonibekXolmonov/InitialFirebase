package com.example.initialfirebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.initialfirebaseapp.manager.AuthManager

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnSignOut).setOnClickListener {
            AuthManager.signOut()
            startActivity(Intent(context, SignInActivity::class.java))
        }
    }
}