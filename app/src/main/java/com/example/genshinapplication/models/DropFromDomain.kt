package com.example.genshinapplication.models

import android.net.Uri

class DropFromDomain {

    // Материалы из подземелий (для возвышения оружия)
    //https://api.genshin.dev/materials/weapon-ascension

    var name: String? = null
    var weapons: List<String>? = null
    var availability: List<String>? = null //День недели
    var items: List<Drop>? = null //Список добычи по уровням
    var source: String? = null //Название подземелья


}