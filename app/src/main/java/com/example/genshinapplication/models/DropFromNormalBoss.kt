package com.example.genshinapplication.models

import android.net.Uri

class DropFromNormalBoss : IDrop {

    //Дроп с обычных боссов, стоимостью 40 смолы
    //Гемы тоже сюда надо добавить
    //https://api.genshin.dev/materials/boss-material

    var id: String? = null //Название дропа с тире
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
        return 4
    }
}