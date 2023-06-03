package com.example.genshinapplication.models

import android.net.Uri
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
    var characterUri: Uri? = null //Icon

    //Не из Api
    //Материалы возвышения
    var ascensionMaterialBoss: DropFromNormalBoss? = null //Добыча с босса за 40 смолы
    var ascensionMaterialGem: DropGems? = null //Кристалл стихии
    var ascensionTalantsMaterial: EnemyDrop? = null //Диковинки мира
    var talantsMaterialBoss: DropFromWeeklyBoss? = null //Добыча с еженедельного босса
    var talantsBook: Books? = null //Книжка


}