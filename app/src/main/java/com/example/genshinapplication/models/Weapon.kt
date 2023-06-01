package com.example.genshinapplication.models

import java.time.LocalDate

class Weapon {

    // Получаем из джосн
    var name: String? = null
    var type: String? = null
    var rarity: Int? = null
    var baseAttac: Int? = null
    var subStat: String? = null
    var passiveName: String? = null
    var passiveDesc: String? = null
    var imageUrl: String? = null

    //Получение
    var location: String? = null
    //Материал из мира
    var ascensionMaterial: String? = null
    //Материал из подземелья
    var ascensionDomainMaterial: String? = null

}