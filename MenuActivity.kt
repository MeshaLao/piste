package com.android.wrapitup

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class MenuActivity : Activity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var menuNameTextView: TextView
    private lateinit var menuProfileImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val settingsButton = findViewById<ImageButton>(R.id.settings_button)
        val profileButton = findViewById<ImageButton>(R.id.profile_button)
        menuProfileImage = findViewById(R.id.profile_image)
        menuNameTextView = findViewById(R.id.profile_name)

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        loadProfileData()

        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadProfileData()
    }

    private fun loadProfileData() {
        val userName = sharedPreferences.getString("NAME", "User")
        val profileImageUri = sharedPreferences.getString("PROFILE_IMAGE_URI", "")

        menuNameTextView.text = userName

        if (!profileImageUri.isNullOrEmpty()) {
            try {
                menuProfileImage.setImageURI(Uri.parse(profileImageUri))
            } catch (e: Exception) {
                menuProfileImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.profile))
            }
        } else {
            menuProfileImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.profile))
        }
    }
}
