package com.example.genshinapplication.activities

import com.example.genshinapplication.models.Artifact
import com.example.genshinapplication.models.Drop
import com.example.genshinapplication.models.GenshinCharacter
import com.example.genshinapplication.models.Weapon
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val BASE_URL = "https://api.genshin.dev"

class APIHelper {

    val client = OkHttpClient()

    val today = LocalDate.now().dayOfWeek

    // Получение перса по его имени
    fun getCharacterInfo(name: String): GenshinCharacter {
        val request = Request
            .Builder()
                // Имя персов пиши ЧЕРЕЗ-ТИРЕ
            .url( "$BASE_URL/characters/${ name.lowercase() }" )
            .build()

        with( client.newCall( request ).execute() ) {
            if ( ! this.isSuccessful )
                throw IOException( "Твой код не работает. Ошибка вот = ${code()}, ${message()}" )

            val jsonObject = JSONObject( this.body()!!.string() )

            //Вносим персонажа
            val character = GenshinCharacter()
                character.name = jsonObject.getString("name")
                character.title = jsonObject.getString("title")
                character.vision = jsonObject.getString("vision")
                character.weapon = jsonObject.getString("weapon")
                character.nation = jsonObject.getString("nation")
                character.description = jsonObject.getString("description")
                character.rarity = jsonObject.getInt("rarity")
                character.birthday =
                    LocalDate.parse (
                        jsonObject.getString("birthday"),
                        DateTimeFormatter.ofPattern( "yyyy-MM-dd" )
                    )

            return character
        }
    }

    //Артефакты
    fun getArtifactInfo(name: String): Artifact {
        val request = Request
            .Builder()
            // Имя персов пиши ЧЕРЕЗ-ТИРЕ
            .url( "$BASE_URL/artifacts/${ name.lowercase() }" )
            .build()

        with( client.newCall( request ).execute() ) {
            if ( ! this.isSuccessful )
                throw IOException( "Твой код не работает. Ошибка вот = ${code()}, ${message()}" )

            val jsonObject = JSONObject( this.body()!!.string() )

            //Вносим артифакт
            val artifact = Artifact()
                artifact.name = jsonObject.getString("name")
                artifact.max_rarity = jsonObject.getInt("max_rarity")
                artifact.piece_2_bonus = jsonObject.getString("2-piece_bonus")
                artifact.piece_4_bonus = jsonObject.getString("4-piece_bonus")

            return artifact
        }
    }

    //Оружие
    fun getWeaponInfo(name: String): Weapon {
        TODO({ по примеру
            "name": "Akuoumaru",
            "type": "Claymore",
            "rarity": 4,
            "baseAttack": 42,
            "subStat": "ATK",
            "passiveName": "Watatsumi Wavewalker",
            "passiveDesc": "For every point of the entire party's combined maximum Energy capacity, the Elemental Burst DMG of the character equipping this weapon is increased by 0.12/0.15/0.18/0.21/0.24%. A maximum of 40/50/60/70/80% increased Elemental Burst DMG can be achieved this way.",
            "location": "Gacha",
            "ascensionMaterial": "distantant-sea"
        })
    }

    //Добыча из подземелий (материалы возвышения оружия)
    fun getDailyDropsInfo(name: String): List<Drop> {
        TODO()
    }

    //Книжки
    fun getBooksDropsInfo(name: String): List<Drop> {
        TODO()
    }

    //Добыча с еженедельных боссов
    fun getWeeklyBossDropsInfo(name: String): List<Drop> {
        TODO()
    }

    //Добыча с минибоссов (которые за 40 смолы)
    fun getDailyBossDropsInfo(name: String): List<Drop> {
        TODO()
    }
}