package com.example.genshinapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.genshinapplication.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    private fun makeToast(text: String){
        val toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast.show()
    }

}