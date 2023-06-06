package com.example.genshinapplication.models

import android.net.Uri

class DropGems {

    //Самоцвет для возвышения + склянки Путешественника
    //https://api.genshin.dev/materials/character-ascension

    var vision: String? = null
    var pieceOfGem: List<PieceOfGem>? = null

    class PieceOfGem : IDrop {
        var id: String? = null //Название дропа с тире
        var name: String? = null //Название дропа, но без тире
        var sourcees: List<String>? = null //Откуда добыть
        var rarity: Int? = 0
        var imageUrl: Uri? = null
        override fun getDropName(): String? {
            return name
        }

        override fun getDropUri(): Uri? {
            return imageUrl
        }

        override fun getDropRarity(): Int? {
            return rarity
        }
    }

}