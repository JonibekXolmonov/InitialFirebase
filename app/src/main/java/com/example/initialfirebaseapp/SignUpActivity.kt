package com.example.initialfirebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.initialfirebaseapp.manager.AuthHandler
import com.example.initialfirebaseapp.manager.AuthManager
import com.example.initialfirebaseapp.utils.Extensions.toast
import java.lang.Exception

class SignUpActivity : BaseActivity() {

    lateinit var edtEmail: EditText
    lateinit var edtPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
    }

    private fun initViews() {

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)

        findViewById<TextView>(R.id.tv_sign_in).setOnClickListener {
            openSignIn()
        }

        findViewById<Button>(R.id.btn_sign_up).setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            firebaseSignUp(email, password)
        }
    }

    private fun firebaseSignUp(email: String, password: String) {
        showLoading(this)
        AuthManager.signUp(email, password, object : AuthHandler {
            override fun onSuccess() {
                toast("Signed up successfully")
                dismissLoading()
                callMainActivity(context)
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast("Sign up failed")
            }

        })
    }

    private fun openSignIn() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}