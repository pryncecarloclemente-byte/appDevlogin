package com.example.appdev_login

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.appdev_login.databinding.ActivityDatabaseViewBinding

class DatabaseViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatabaseViewBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatabaseViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

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

                tableLayout.addView(tableRow)
            }
        }
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
