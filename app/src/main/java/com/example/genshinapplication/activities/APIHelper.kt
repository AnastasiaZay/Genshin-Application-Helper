package com.example.genshinapplication.activities

import com.example.genshinapplication.models.GenshinCharacter
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.IOException

const val BASE_URL = "https://api.genshin.dev"

class APIHelper {

    val client = OkHttpClient()

    // Получение перса по его имени
    fun getCharacterInfo(name: String): GenshinCharacter {
        val request = Request
            .Builder()
                // Имя персов пиши ЧЕРЕЗ-ТИРЕ
            .url( "$BASE_URL/characters/${ name.lowercase() }" )
            .build()

        with( client.newCall( request ).execute() ) {
            if ( ! this.isSuccessful )
                throw IOException( "Твой код неработает. Ошибка вот = " + this.code().toString() + ", " + this.message() )

            val jsonObject = JSONObject( this.body()!!.string() )
            println("Вот твой перс = $jsonObject")
            TODO()
        }
    }


    fun getArtifactInfo(name: String): Artifact {

    }

    fun getWeaponInfo(name: String): Weapon {

    }

    //Добыча из подземелий
    fun getDailyDropsInfo(name: String): List<Drop> {

    }

    //Книжки
    fun getBooksDropsInfo(name: String): List<Drop> {

    }

    //Добыча с еженедельных боссов
    fun getWeeklyBossDropsInfo(name: String): List<Drop> {

    }

    //Добыча с минибоссов
    fun getDailyBossDropsInfo(name: String): List<Drop> {

    }
}