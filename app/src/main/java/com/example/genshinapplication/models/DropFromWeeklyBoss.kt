package com.example.genshinapplication.models

import android.net.Uri

class DropFromWeeklyBoss :IDrop{

    //Дроп с еженедельных боссов (таланты персонажей)
    //https://api.genshin.dev/materials/talent-boss

    var id: String? = null //Название дропа
    var name: String? = null //Название дропа, но без тире
    var source: String? = null //Название босса
    var character: List<String>? = null
    var imageUrl: Uri? = null

    override fun getDropName(): String? {
        return name
    }

    override fun getDropUri(): Uri? {
        return imageUrl
    }
    override fun getDropRarity(): Int? {
        return 5
    }
}