package com.example.initialfirebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.initialfirebaseapp.manager.AuthHandler
import com.example.initialfirebaseapp.manager.AuthManager
import com.example.initialfirebaseapp.utils.Extensions.toast
import java.lang.Exception

class SignInActivity : BaseActivity() {

    lateinit var edtEmail: EditText
    lateinit var edtPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initViews()
    }

    private fun initViews() {

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)

        findViewById<TextView>(R.id.tv_sign_up).setOnClickListener {
            openSignUpPage()
        }

        findViewById<Button>(R.id.btn_sign_in).setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            firebaseSignIn(email, password)
        }
    }

    private fun firebaseSignIn(email: String, password: String) {
        showLoading(this)
        AuthManager.signIn(email, password, object : AuthHandler {
            override fun onSuccess() {
                dismissLoading()
                toast("Signed in successfully")
                callMainActivity(context)
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                Toast.makeText(context, "$exception \nSign in failed", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun openSignUpPage() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }
}