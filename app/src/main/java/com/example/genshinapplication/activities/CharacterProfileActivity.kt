package com.example.genshinapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class CharacterProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_profile)

        val nameView = findViewById<TextView>(R.id.nameView)
        nameView.text = intent.extras!!.getString("name")
    }
}