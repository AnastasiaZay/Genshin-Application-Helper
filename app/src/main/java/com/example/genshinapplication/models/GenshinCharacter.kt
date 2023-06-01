package com.example.genshinapplication.models

import java.time.LocalDate

class GenshinCharacter() {

    // Получаем из джосн
    var name: String? = null
    var title: String? = null
    var vision: String? = null
    var weapon: String? = null
    var nation: String? = null //Регион
    var description: String? = null
    var rarity: Int? = null //4 или 5 звезд
    var birthday: LocalDate? = null


    //Не из Api
    //Материалы возвышения
    var ascensionMaterialBoss: String? = null //Добыча с босса за 40 смолы
    var ascensionMaterialGem: String? = null //Кристалл стихии
    var ascensionTalantsMaterialWorld: String? = null //Диковинки мира
    var ascensionTalantsMaterialMob: String? = null //Добыча с мобов (Возвышение и таланты)
    var talantsMaterialBoss: String? = null //Добыча с еженедельного босса
    var talantsBook: String? = null //Книжка


}