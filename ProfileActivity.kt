package com.android.wrapitup

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class ProfileActivity : Activity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var textViewName: TextView
    private lateinit var textViewRole: TextView
    private lateinit var textViewPhone: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var buttonEditProfile: Button
    private lateinit var buttonSettings: Button
    private lateinit var buttonMenu: ImageButton
    private lateinit var buttonProfile: ImageView

    private val PICK_IMAGE_REQUEST = 1
    private val STORAGE_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Views
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        textViewName = findViewById(R.id.profile_name)
        textViewRole = findViewById(R.id.profile_role)
        textViewPhone = findViewById(R.id.profile_phone)
        textViewEmail = findViewById(R.id.profile_email)
        buttonEditProfile = findViewById(R.id.editProfile_button)
        buttonSettings = findViewById(R.id.settings_button)
        buttonMenu = findViewById(R.id.menu_button)
        buttonProfile = findViewById(R.id.profile_image)

        // Load Profile Data
        loadProfileData()

        // Edit Profile Button → Open EditProfileActivity
        buttonEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivityForResult(intent, 1)
        }

        // Settings Button → Open SettingsActivity
        buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Menu Button → Open MenuActivity with User Name & Profile Image
        buttonMenu.setOnClickListener {
            val name = textViewName.text.toString()
            val profileImageUri = sharedPreferences.getString("PROFILE_IMAGE_URI", "")
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("USER_NAME", name)
            intent.putExtra("PROFILE_IMAGE_URI", profileImageUri)
            startActivity(intent)
        }

        // Profile Image Button → Select & Change Profile Picture
        buttonProfile.setOnClickListener {
            if (checkStoragePermission()) {
                openImagePicker()
            } else {
                requestStoragePermission()
            }
        }
    }

    // Load profile data from SharedPreferences
    private fun loadProfileData() {
        val name = sharedPreferences.getString("NAME", "N/A")
        val role = sharedPreferences.getString("ROLE", "N/A")
        val phone = sharedPreferences.getString("PHONE", "N/A")
        val email = sharedPreferences.getString("EMAIL", "N/A")
        val profileImageUri = sharedPreferences.getString("PROFILE_IMAGE_URI", "")

        textViewName.text = name
        textViewRole.text = role
        textViewPhone.text = phone
        textViewEmail.text = email

        if (!profileImageUri.isNullOrEmpty()) {
            try {
                val uri = Uri.parse(profileImageUri)
                buttonProfile.setImageURI(uri)
            } catch (e: Exception) {
                buttonProfile.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.profile))
            }
        } else {
            buttonProfile.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.profile))
        }
    }

    private fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                buttonProfile.setImageBitmap(bitmap)

                val editor = sharedPreferences.edit()
                editor.putString("PROFILE_IMAGE_URI", imageUri.toString())
                editor.apply()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val updatedName = data.getStringExtra("UPDATED_NAME")
            val updatedRole = data.getStringExtra("UPDATED_ROLE")
            val updatedPhone = data.getStringExtra("UPDATED_PHONE")
            val updatedEmail = data.getStringExtra("UPDATED_EMAIL")

            updatedName?.let { textViewName.text = it }
            updatedRole?.let { textViewRole.text = it }
            updatedPhone?.let { textViewPhone.text = it }
            updatedEmail?.let { textViewEmail.text = it }
        }
    }
}
