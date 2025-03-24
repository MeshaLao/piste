package com.android.wrapitup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        
        val backButton = findViewById<ImageButton>(R.id.backBtn)
        val developerButton = findViewById<Button>(R.id.developers_button)


        backButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        developerButton.setOnClickListener {

            val intent = Intent(this, DeveloperActivity::class.java)
            startActivity(intent)

        }
    }
}
