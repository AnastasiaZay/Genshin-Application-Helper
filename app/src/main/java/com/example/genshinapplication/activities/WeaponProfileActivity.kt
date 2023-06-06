package com.example.genshinapplication

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.genshinapplication.helpers.BASE_URL
import com.example.genshinapplication.models.Weapon
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class WeaponProfileActivity : AppCompatActivity() {
    lateinit var imageView: ImageView

    //    lateinit var cardsMaterials: FlexboxLayout
    lateinit var name: String
    lateinit var baseAttack: TextView
    lateinit var subStat: TextView
    lateinit var passiveName: TextView
    lateinit var location: TextView
    lateinit var type: TextView
    lateinit var passiveDesc: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weapon_profile)
        imageView = findViewById(R.id.imageView)
//        cardsMaterials = findViewById(R.id.cardsMaterials)
        val nameView = findViewById<TextView>(R.id.nameView)
        nameView.text = intent.extras!!.getString("name")
        name = intent.extras!!.getString("name")!!.lowercase().replace(" ", "-").replace("'", "-")
            .replace("'", "-").replace("--", "-")
        val client = OkHttpClient()
        baseAttack = findViewById(R.id.baseAttackView)
        subStat = findViewById(R.id.subStatView)
        passiveName = findViewById(R.id.passiveNameView)
        location = findViewById(R.id.locationView)
        passiveDesc = findViewById(R.id.passiveDescView)
        type = findViewById(R.id.weaponView)

        getWeaponInfo(client, name)
    }

    private fun getWeaponInfo(
        client: OkHttpClient,
        name: String

    ) {
        var nameForPic = name.lowercase().replace(" ", "-").replace("'", "-").replace("'", "-")
        val request = Request
            .Builder()
            .url("$BASE_URL/weapons/${nameForPic}")
            .build()

        //Вносим персонажа
        val weapon = Weapon()

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

                weapon.name = jsonObject.getString("name")
                weapon.passiveDesc = jsonObject.getString("passiveDesc")
                weapon.baseAttack = jsonObject.getInt("baseAttack")
                weapon.type = jsonObject.getString("type")
                weapon.subStat = jsonObject.getString("subStat")
                weapon.passiveName = jsonObject.getString("passiveName")
                weapon.ascensionMaterial = jsonObject.getString("ascensionMaterial")
                weapon.rarity = jsonObject.getInt("rarity")
                weapon.location = jsonObject.getString("location")
//                weapon.ascensionMaterial = jsonObject.getString("location") Надо добавить сам материал!!


                var n = Uri.parse("$BASE_URL/weapons/$nameForPic/icon")
                weapon.imageUrl = n
                println(n)
                println(nameForPic)

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
                        when (weapon.rarity) {
                            1 -> R.drawable.background_rarity_1_star
                            2 -> R.drawable.background_rarity_2_star
                            3 -> R.drawable.background_rarity_3_star
                            4 -> R.drawable.background_rarity_4_star
                            5 -> R.drawable.background_rarity_5_star
                            else -> R.drawable.background_rarity_5a_star
                        }
                    )

                    val rightImage = findViewById<ImageView>(R.id.weaponImageView)
                    rightImage.setImageResource(
                        when (weapon.type) {
                            "Bow" -> R.drawable.bow_icon
                            "Catalyst" -> R.drawable.catalyst_icon
                            "Claymore" -> R.drawable.claymore_icon
                            "Sword" -> R.drawable.sword_icon
                            "Polearm" -> R.drawable.polearm_icon
                            else -> R.drawable.bow_icon
                        }
                    )


                    baseAttack.text = weapon.baseAttack.toString()
                    subStat.text = weapon.subStat
                    type.text = weapon.type
                    passiveName.text = weapon.passiveName
                    location.text = weapon.location
                    passiveDesc.text = weapon.passiveDesc
                    location.text = weapon.location

                }
            }
        })
    }
}