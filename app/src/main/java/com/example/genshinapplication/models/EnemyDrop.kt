package com.example.genshinapplication.models

class EnemyDrop {

    //Добыча с маленьких мобов (персонажи)+(оружие)
    //https://api.genshin.dev/materials/common-ascension

    var character: List<String>? = null
    var weapons: List<String>? = null
    var items: List<Drop>? = null
    var sourcees: List<String>? = null //Откуда добыть


}