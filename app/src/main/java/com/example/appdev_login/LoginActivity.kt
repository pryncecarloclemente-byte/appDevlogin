package com.example.appdev_login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appdev_login.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        // Optional debug: show how many users exist in DB and list emails (helps verify DB connection)
        if (AppConfig.ENABLE_DB_DEBUG) {
            val currentUsers = databaseHelper.getAllUsers()
            val emails = currentUsers.joinToString { it.email }
            Toast.makeText(this, "DB users: ${currentUsers.size} -> $emails", Toast.LENGTH_LONG).show()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val user = databaseHelper.getUserByEmailAndPassword(email, password)
                if (user != null) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, DatabaseViewActivity::class.java).apply {
                        putExtra("currentUserId", user.id)
                        putExtra("currentUserRole", user.role)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.tvViewDatabase.setOnClickListener {
            val intent = Intent(this, DatabaseViewActivity::class.java)
            startActivity(intent)
        }
    }
}
