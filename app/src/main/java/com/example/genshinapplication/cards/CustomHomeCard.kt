package com.example.genshinapplication.cards

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.genshinapplication.R
import com.example.genshinapplication.helpers.BASE_URL
import com.example.genshinapplication.models.GenshinCharacter
import com.google.android.flexbox.FlexboxLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class CustomHomeCard : CardView {

    constructor(
        context: Context, dropUri: Uri, characters: JSONArray
    ) : super(context!!) {

        val inflater: LayoutInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.home_row, this, true)

        val dropImageView = findViewById<ImageView>(R.id.dropImageView)
        //https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest
        Picasso.get().load(dropUri).into(dropImageView, object : Callback {

            override fun onSuccess() {}

            //Если картинки не загрузились
            override fun onError(e: Exception?) {
                Picasso.get()
                    .load("https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest")
                    .into(dropImageView)
            }
        })


        val background = ContextCompat.getDrawable(context, R.drawable.background_rarity_4_star)
        dropImageView.background = background //Сменить фон
        val charactersContainer = findViewById<FlexboxLayout>(R.id.charactersContainer)
        println("$dropUri")
        var c = 0
        while (c < characters.length()) {
            val ss = checkName(characters[c].toString().lowercase().replace(" ", "-"))

            val request = Request
                .Builder()
                .url("$BASE_URL/characters/${ss}")
                .build()

            val character = GenshinCharacter()

            OkHttpClient().newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    throw e
                }

                override fun onResponse(call: Call, res: Response) {
                    if (!res.isSuccessful)
                        throw IOException("Твой код не работает. Ошибка вот = ${res.code()}, ${res.message()}")
                    parse(res.body()!!.string())
                }

                private fun parse(response: String) {
                    val jsonObject = JSONObject(response)

                    character.name = jsonObject.getString("name")
                    character.rarity = jsonObject.getInt("rarity")


                    var n = Uri.parse(
                        "$BASE_URL/characters/${
                            checkName(
                                character.name.toString().lowercase().replace(" ", "-")
                            )
                        }/icon-big"
                    )
                    character.characterUri = n
                    println(n)
                    val miniCard =
                        CustomMiniCharacterCard(context, character)
                    charactersContainer.post { charactersContainer.addView(miniCard) }
                }
            })
            c++
        }
    }

    //Проверяем, чтобы имя соответствовало
    fun checkName(name: String): String {
        var nameNew: String = when (name) {
            "kamisato-ayaka" -> "ayaka"
            "kaedehara-kazuha" -> "kazuha"
            "sangonomiya-kokomi" -> "kokomi"
            "kujou-sara" -> "sara"
            "raiden-shogun" -> "raiden"
            else -> name
        }

        return nameNew
    }
}