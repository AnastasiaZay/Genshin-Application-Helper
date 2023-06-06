package com.example.genshinapplication.models

import android.net.Uri

class WorldDrop : IDrop {

    //Диковинки мира
    //https://api.genshin.dev/materials/local-specialties
    var name: String? = null
    var character: List<String>? = null
    var imageUrl: Uri? = null
    var id: String? = null
    override fun getDropName(): String? {
        return name
    }

    override fun getDropUri(): Uri? {
        return imageUrl
    }

    override fun getDropRarity(): Int? {
        return 1
    }

}
