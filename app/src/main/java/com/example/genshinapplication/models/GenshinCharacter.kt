package com.example.genshinapplication.models

import java.time.LocalDate

class GenshinCharacter() {

    // Получаем из джосн
    var name: String? = null
    var title: String? = null
    var vision: String? = null
    var weapon: String? = null
    var nation: String? = null
    var description: String? = null
    var rarity: Int? = null
    var birthday: LocalDate? = null
}