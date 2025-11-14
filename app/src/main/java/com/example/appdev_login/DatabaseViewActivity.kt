package com.example.appdev_login

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Spinner
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.appdev_login.databinding.ActivityDatabaseViewBinding

class DatabaseViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatabaseViewBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var currentUserRole: String = "guest"
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatabaseViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        // Read current user role and id from intent extras (if provided)
        currentUserRole = intent.getStringExtra("currentUserRole") ?: "guest"
        currentUserId = intent.getIntExtra("currentUserId", -1)

        loadUsersIntoTable()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadUsersIntoTable() {
        val users = databaseHelper.getAllUsers()
        val tableLayout = binding.tableLayout

        // Clear existing rows except header
        tableLayout.removeViews(1, maxOf(0, tableLayout.childCount - 1))

        if (users.isEmpty()) {
            val emptyRow = TableRow(this)
            val emptyTextView = TextView(this).apply {
                text = "No users registered yet"
                setPadding(16, 16, 16, 16)
                setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            }
            emptyRow.addView(emptyTextView)
            tableLayout.addView(emptyRow)
        } else {
            users.forEach { user ->
                val tableRow = TableRow(this).apply {
                    setPadding(0, 8, 0, 8)
                }

                // ID Column
                val idTextView = createTableCell(user.id.toString())
                tableRow.addView(idTextView)

                // Username Column
                val usernameTextView = createTableCell(user.username)
                tableRow.addView(usernameTextView)

                // Email Column
                val emailTextView = createTableCell(user.email)
                tableRow.addView(emailTextView)

                // Password Column (masked)
                val passwordTextView = createTableCell("••••••••")
                tableRow.addView(passwordTextView)

                // Role Column
                val roleTextView = createTableCell(user.role)
                tableRow.addView(roleTextView)

                // Action buttons (only add for admins)
                if (currentUserRole == "admin") {
                    val actionsLayout = LinearLayout(this).apply {
                        orientation = LinearLayout.HORIZONTAL
                    }

                    val editButton = Button(this).apply {
                        text = "Edit"
                        textSize = 12f
                        setOnClickListener { showEditDialog(user) }
                    }

                    val deleteButton = Button(this).apply {
                        text = "Delete"
                        textSize = 12f
                        setOnClickListener { confirmAndDeleteUser(user) }
                    }

                    actionsLayout.addView(editButton)
                    actionsLayout.addView(deleteButton)
                    tableRow.addView(actionsLayout)
                } else {
                    // Add an empty placeholder cell so columns align for guests
                    val placeholder = createTableCell("")
                    tableRow.addView(placeholder)
                }

                tableLayout.addView(tableRow)
            }
        }
    }

    private fun showEditDialog(user: User) {
        // Only admins can edit — double-check
        if (currentUserRole != "admin") return

        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_user, null)

        val etUsername = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etUsername)
        val etEmail = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etEmail)
        val etPassword = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etPassword)
        val spinnerRole = dialogView.findViewById<Spinner>(R.id.spinnerRole)

        etUsername.setText(user.username)
        etEmail.setText(user.email)
        etPassword.setText(user.password)

        // Setup spinner options
        val roles = arrayOf("guest", "admin")
        val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter
        val roleIndex = roles.indexOf(user.role).takeIf { it >= 0 } ?: 0
        spinnerRole.setSelection(roleIndex)

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Edit user")
            .setView(dialogView)
            .setPositiveButton("Save") { d, _ ->
                val newUsername = etUsername.text.toString().trim()
                val newEmail = etEmail.text.toString().trim()
                val newPassword = etPassword.text.toString().trim()
                val newRole = spinnerRole.selectedItem.toString()

                val rows = databaseHelper.updateUser(user.id, newUsername, newEmail, newPassword, newRole)
                if (rows > 0) {
                    Toast.makeText(this, "User updated", Toast.LENGTH_SHORT).show()
                    loadUsersIntoTable()
                } else {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                }
                d.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmAndDeleteUser(user: User) {
        if (currentUserRole != "admin") return

        // Prevent admin from deleting themselves
        if (user.id == currentUserId) {
            Toast.makeText(this, "You cannot delete your own admin account", Toast.LENGTH_SHORT).show()
            return
        }

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Delete user")
            .setMessage("Are you sure you want to delete ${user.username}? This cannot be undone.")
            .setPositiveButton("Delete") { d, _ ->
                val rows = databaseHelper.deleteUser(user.id)
                if (rows > 0) {
                    Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()
                    loadUsersIntoTable()
                } else {
                    Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                }
                d.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun createTableCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 16, 16, 16)
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            textSize = 14f
        }
    }
}
