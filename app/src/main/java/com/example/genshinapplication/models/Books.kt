package com.example.genshinapplication.models

class Books {

    var name: String? = null //Название дропа, но без тире
    var source: String? = null //Название Подземелья
    var character: List<String>? = null
    var availability: List<String>? = null //День недели
    var items: List<Drop>? = null //Список добычи по уровням

}