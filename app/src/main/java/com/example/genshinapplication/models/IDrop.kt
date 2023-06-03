package com.example.genshinapplication.models
import android.net.Uri

interface IDrop{
    fun getDropName():String?
    fun getDropUri():Uri?
    fun getDropRarity():Int?

}