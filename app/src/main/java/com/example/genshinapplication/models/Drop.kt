package com.example.genshinapplication.models

import android.net.Uri

class Drop() : IDrop {

    var name: String? = null
    var id: String? = null
    var rarity: Int? = 0
    var imageUrl: Uri? = null
    override fun getDropName(): String? {
        return name
    }

    override fun getDropUri(): Uri? {
        return imageUrl
    }

    override fun getDropRarity(): Int? {
        return rarity
    }

    // "boss-material","character-ascension" одинаковы
}