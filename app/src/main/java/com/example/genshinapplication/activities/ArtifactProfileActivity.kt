package com.example.genshinapplication.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.genshinapplication.R
import com.example.genshinapplication.models.Artifact
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

//aftifactCardView
class ArtifactProfileActivity : AppCompatActivity() {
    lateinit var name: String
    lateinit var imageViewHat: ImageView
    lateinit var imageSand: ImageView
    lateinit var imageViewGobel: ImageView
    lateinit var imageFlower: ImageView
    lateinit var imageViewPlume: ImageView
    lateinit var text4View: TextView
    lateinit var text2View: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artifact_profile)
        imageViewHat = findViewById(R.id.hatImage)
        imageSand = findViewById(R.id.imageSand)
        imageViewGobel = findViewById(R.id.imageViewGobel)
        imageFlower = findViewById(R.id.imageFlower)
        imageViewPlume = findViewById(R.id.imageViewPlume)
        text4View = findViewById(R.id.PiesArtBonus4)
        text2View = findViewById(R.id.PiesArtBonus2)
//PiesArtBonus2
//        cardsMaterials = findViewById(R.id.cardsMaterials)
        val nameView = findViewById<TextView>(R.id.artifactName)
        nameView.text = intent.extras!!.getString("name")
        name = intent.extras!!.getString("name")!!.lowercase().replace(" ", "-")



        val client = OkHttpClient()
        getArtifactInfo(client, name)
    }

    private fun getArtifactInfo(
        client: OkHttpClient,
        name: String
    ) {  //https://api.genshin.dev/characters/имя-персонажа/icon  - картинки персонажа  (в частности, иконка)
        val request = Request
            .Builder()
            .url("$BASE_URL/artifacts/${name.lowercase()}")
            .build()

        //Вносим персонажа
        val artifact = Artifact()

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

                artifact.name = jsonObject.getString("name")
//                artifact.piece_2_bonus = jsonObject.getString("2-piece_bonus")
//                artifact.piece_4_bonus = jsonObject.getString("4-piece_bonus")
                artifact.max_rarity = jsonObject.getInt("max_rarity")
                artifact.flowerUri = Uri.parse("$BASE_URL/artifacts/${name.lowercase()}/flower-of-life")
                artifact.gobletUri = Uri.parse("$BASE_URL/artifacts/${name.lowercase()}/goblet-of-eonothem")
                artifact.plumeUri = Uri.parse("$BASE_URL/artifacts/${name.lowercase()}/plume-of-death")
                artifact.sandsUri = Uri.parse("$BASE_URL/artifacts/${name.lowercase()}/sands-of-eon")

                var n = Uri.parse("$BASE_URL/artifacts/${name.lowercase()}/circlet-of-logos")//корону загружаем
                artifact.circletUri = n
                println(n)
                runOnUiThread {
                    //Разберись с добавлением текста
                    text4View.text = intent.extras!!.getString("4-piece_bonus")
                    text2View.text = intent.extras!!.getString("2-piece_bonus")
                    Picasso.get().load(n).into(imageViewHat, object :
                        com.squareup.picasso.Callback {
                        override fun onSuccess() {}
                        //Если картинки не загрузились
                        override fun onError(e: Exception?) {
                            Picasso.get()
                                .load("https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest")
                                .into(imageViewHat)
                        }
                    })
                    imageViewHat.background = ContextCompat.getDrawable(
                        applicationContext,
                        when (artifact.max_rarity) {
                            1 -> R.drawable.background_rarity_1_star
                            2 -> R.drawable.background_rarity_2_star
                            3 -> R.drawable.background_rarity_3_star
                            4 -> R.drawable.background_rarity_4_star
                            5 -> R.drawable.background_rarity_5_star
                            else -> R.drawable.background_rarity_5a_star
                        }
                    )
                    Picasso.get().load( artifact.flowerUri).into(imageFlower, object :
                        com.squareup.picasso.Callback {
                        override fun onSuccess() {}
                        //Если картинки не загрузились
                        override fun onError(e: Exception?) {
                            Picasso.get()
                                .load("https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest")
                                .into(imageFlower)
                        }
                    }) //Переделать по-нормальному, чтобы один и тот же код не повторялся по 5 раз
                    imageFlower.background = ContextCompat.getDrawable(
                        applicationContext,
                        when (artifact.max_rarity) {
                            1 -> R.drawable.background_rarity_1_star
                            2 -> R.drawable.background_rarity_2_star
                            3 -> R.drawable.background_rarity_3_star
                            4 -> R.drawable.background_rarity_4_star
                            5 -> R.drawable.background_rarity_5_star
                            else -> R.drawable.background_rarity_5a_star
                        }
                    )
                    Picasso.get().load(artifact.gobletUri).into(imageViewGobel, object :
                        com.squareup.picasso.Callback {
                        override fun onSuccess() {}
                        //Если картинки не загрузились
                        override fun onError(e: Exception?) {
                            Picasso.get()
                                .load("https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest")
                                .into(imageViewGobel)
                        }
                    })
                    imageViewGobel.background = ContextCompat.getDrawable(
                        applicationContext,
                        when (artifact.max_rarity) {
                            1 -> R.drawable.background_rarity_1_star
                            2 -> R.drawable.background_rarity_2_star
                            3 -> R.drawable.background_rarity_3_star
                            4 -> R.drawable.background_rarity_4_star
                            5 -> R.drawable.background_rarity_5_star
                            else -> R.drawable.background_rarity_5a_star
                        }
                    )
                    Picasso.get().load(artifact.plumeUri).into(imageViewPlume, object :
                        com.squareup.picasso.Callback {
                        override fun onSuccess() {}
                        //Если картинки не загрузились
                        override fun onError(e: Exception?) {
                            Picasso.get()
                                .load("https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest")
                                .into(imageViewPlume)
                        }
                    })
                    imageViewPlume.background = ContextCompat.getDrawable(
                        applicationContext,
                        when (artifact.max_rarity) {
                            1 -> R.drawable.background_rarity_1_star
                            2 -> R.drawable.background_rarity_2_star
                            3 -> R.drawable.background_rarity_3_star
                            4 -> R.drawable.background_rarity_4_star
                            5 -> R.drawable.background_rarity_5_star
                            else -> R.drawable.background_rarity_5a_star
                        }
                    )
                    Picasso.get().load(artifact.sandsUri).into(imageSand, object :
                        com.squareup.picasso.Callback {
                        override fun onSuccess() {}
                        //Если картинки не загрузились
                        override fun onError(e: Exception?) {
                            Picasso.get()
                                .load("https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest")
                                .into(imageSand)
                        }
                    })
                    imageSand.background = ContextCompat.getDrawable(
                        applicationContext,
                        when (artifact.max_rarity) {
                            1 -> R.drawable.background_rarity_1_star
                            2 -> R.drawable.background_rarity_2_star
                            3 -> R.drawable.background_rarity_3_star
                            4 -> R.drawable.background_rarity_4_star
                            5 -> R.drawable.background_rarity_5_star
                            else -> R.drawable.background_rarity_5a_star
                        }
                    )
                }


            }
        })

    }


}