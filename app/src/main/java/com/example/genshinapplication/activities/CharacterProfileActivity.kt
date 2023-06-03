package com.example.genshinapplication.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.genshinapplication.R
import com.example.genshinapplication.cards.CustomMaterialCard
import com.example.genshinapplication.models.GenshinCharacter
import com.google.android.flexbox.FlexboxLayout
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

//cardsMaterials
class CharacterProfileActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var cardsMaterials: FlexboxLayout
    lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_profile)
        imageView = findViewById<ImageView>(R.id.imageView)
        cardsMaterials = findViewById(R.id.cardsMaterials)
        val nameView = findViewById<TextView>(R.id.nameView)
        nameView.text = intent.extras!!.getString("name")
        name = intent.extras!!.getString("name")!!.lowercase().replace(" ", "-")
        val client = OkHttpClient()

        getCharacterInfo(client, name)
        getDropBooks(client)
    }

    fun createDropCard(dropName: String, dropImage: Uri, dropRarity: Int) {
        val card = CustomMaterialCard(applicationContext, dropName, dropImage, dropRarity)
        cardsMaterials.addView(card)
    }

    //Ща пойдем по массиву дропа в поисках имени персонажа
    private fun getDropBooks(client: OkHttpClient) {

        val request = Request
            .Builder()
            .url("$BASE_URL/materials/talent-book")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                throw e
            }

            override fun onResponse(call: Call, res: Response) {
                if (!res.isSuccessful)
                    throw IOException("Твой код не работает. Ошибка вот = ${res.code()}, ${res.message()}")
                parse(res.body()!!.string())

            }

            //Тут надо получить из дропа имя персонажа, тк у персонажа нет инфы о дропе
            private fun parse(response: String) {
                val jsonObject = JSONObject(response)
                val keys = jsonObject.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val jObject = jsonObject.getJSONObject(key)
                    val arr = jObject.getJSONArray("characters")
                    var i = 0
                    while (i < arr.length()) {
                        if (arr[i].toString() == name) {
                            val itemsArr = jObject.getJSONArray("items")
                            var itemI = 0
                            while (itemI < itemsArr.length()) {
                                val thisItem =
                                    itemsArr.getJSONObject(itemI)  // Сам предмет, конкретный, типо Книжки "Учения о свободе"
                                runOnUiThread {
                                    createDropCard(
                                        thisItem.getString("name"),
                                        Uri.parse(
                                            "$BASE_URL/materials/talent-book/${
                                                thisItem.getString(
                                                    "id"
                                                )
                                            }"
                                        ),
                                        thisItem.getInt("rarity")
                                    )
                                    println("${ thisItem.getString("name")}========================================fddddddddddddddddddddddddddddddddddddddddddd")
                                }
                                itemI++
                            }
                        }
                        i++
                    }
                }

            }
        })

    }

    private fun getDropFromNormalBoss(client: OkHttpClient) {
        val request = Request
            .Builder()
            .url("$BASE_URL/materials/boss-material")
            .build()
    }

    private fun getDropFromWeeklyBoss(client: OkHttpClient) {
        val request = Request
            .Builder()
            .url("$BASE_URL/materials/talent-boss")
            .build()
    }

    private fun getDropGems(client: OkHttpClient) {
        val request = Request
            .Builder()
            .url("$BASE_URL/materials/character-ascension")
            .build()
    }

    private fun getDropFromEnemy(client: OkHttpClient) {
        val request = Request
            .Builder()
            .url("$BASE_URL/materials/common-ascension")
            .build()
    }


    private fun getCharacterInfo(
        client: OkHttpClient,
        name: String
    ) {  //https://api.genshin.dev/characters/имя-персонажа/icon  - картинки персонажа  (в частности, иконка)
        val request = Request
            .Builder()
            .url("$BASE_URL/characters/${name.lowercase()}")
            .build()

        //Вносим персонажа
        val character = GenshinCharacter()

        client.newCall(request).enqueue(object : Callback {
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
                //character.title = jsonObject.getString("title")
                character.vision = jsonObject.getString("vision")
                character.weapon = jsonObject.getString("weapon")
                character.nation = jsonObject.getString("nation")
                character.description = jsonObject.getString("description")
                character.rarity = jsonObject.getInt("rarity")
//                val dat = jsonObject.getString("birthday").split("-")
//                character.birthday = LocalDate.of(0, dat[1].toInt(), dat[2].toInt() )


                var n = Uri.parse("$BASE_URL/characters/${name.lowercase()}/icon-big")
                character.characterUri = n
                println(n)

                runOnUiThread {
                    Picasso.get().load(n).into(imageView, object :
                        com.squareup.picasso.Callback {

                        override fun onSuccess() {}

                        //Если картинки не загрузились
                        override fun onError(e: Exception?) {
                            Picasso.get()
                                .load("https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest")
                                .into(imageView)
                        }
                    })
                    imageView.background = ContextCompat.getDrawable(
                        applicationContext,
                        when (character.rarity) {
                            4 -> R.drawable.background_rarity_4_star
                            5 -> R.drawable.background_rarity_5_star
                            else -> R.drawable.background_rarity_3_star
                        }
                    )
                }


            }
        })

    }
}