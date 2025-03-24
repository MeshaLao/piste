package com.android.wrapitup

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class EditProfileActivity : Activity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editTextName: EditText
    private lateinit var editTextRole: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize the views
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val saveButton = findViewById<Button>(R.id.save_button)
        editTextName = findViewById(R.id.editTextName)
        editTextRole = findViewById(R.id.editTextRole)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextEmail = findViewById(R.id.editTextEmail)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)

        // Load the current profile data
        editTextName.setText(sharedPreferences.getString("NAME", ""))
        editTextRole.setText(sharedPreferences.getString("ROLE", ""))
        editTextPhone.setText(sharedPreferences.getString("PHONE", ""))
        editTextEmail.setText(sharedPreferences.getString("EMAIL", ""))

        // Back button functionality
        backButton.setOnClickListener {
            finish() // Close the activity
        }

        // Save button functionality
        saveButton.setOnClickListener {
            saveButton.setOnClickListener {
                val name = editTextName.text.toString().trim()
                val role = editTextRole.text.toString().trim()
                val phone = editTextPhone.text.toString().trim()
                val email = editTextEmail.text.toString().trim()

                if (name.isEmpty() || role.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val editor = sharedPreferences.edit()
                    editor.putString("NAME", name)
                    editor.putString("ROLE", role)
                    editor.putString("PHONE", phone)
                    editor.putString("EMAIL", email)
                    editor.apply()

                    val resultIntent = Intent(this, ProfileActivity::class.java)
                    resultIntent.putExtra("UPDATED_NAME", name)
                    resultIntent.putExtra("UPDATED_ROLE", role)
                    resultIntent.putExtra("UPDATED_PHONE", phone)
                    resultIntent.putExtra("UPDATED_EMAIL", email)
                    setResult(RESULT_OK, resultIntent)

                    Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show()

                    startActivity(resultIntent)
                    finish()
                }
            }


        }
    }
}
