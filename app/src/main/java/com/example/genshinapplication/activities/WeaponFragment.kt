package com.example.genshinapplication.activities

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.genshinapplication.cards.CustomCharacterCard
import com.example.genshinapplication.R
import com.example.genshinapplication.cards.CustomWeaponCard
import com.example.genshinapplication.models.GenshinCharacter
import com.example.genshinapplication.models.Weapon
import com.google.android.flexbox.FlexboxLayout
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class WeaponFragment : Fragment() {
    lateinit var weaponContainer: FlexboxLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weapon, container, false)

        weaponContainer = view.findViewById(R.id.weaponContainer)
        run()

        return view
    }
    fun addCharCard(ch: Weapon) {
        val card = CustomWeaponCard( requireContext(), ch )
        println( card )
        weaponContainer.addView( card )
    }

    fun run(){
        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("$BASE_URL/weapons")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException?) {
                call.cancel()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call?, res: Response) {
                val jsonArr = JSONArray(res.body()!!.string())

                var iterator = 0
                val lst = ArrayList<Weapon>()
                while (iterator < jsonArr.length()) {
                    getCharacterInfo(client,jsonArr.getString(iterator))
                    iterator++
                }

            }
        })
    }
    fun getCharacterInfo(client: OkHttpClient, name: String) {  //https://api.genshin.dev/characters/имя-персонажа/icon  - картинки персонажа  (в частности, иконка)
        val request = Request
            .Builder()
            // Название оружия пиши ЧЕРЕЗ-ТИРЕ
            .url("$BASE_URL/weapons/${name.lowercase()}")
            .build()

        //Вносим оружие
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

            private fun parse(response: String){
                val jsonObject = JSONObject(response)

                weapon.name = jsonObject.getString("name")

//                weapon.type = jsonObject.getString("type")
                weapon.rarity = jsonObject.getInt("rarity")
//                weapon.baseAttack = jsonObject.getInt("baseAttack")
//                weapon.subStat = jsonObject.getString("subStat")
//                weapon.passiveName = jsonObject.getString("passiveName")
//                weapon.passiveDesc = jsonObject.getString("passiveDesc")
//                weapon.location = jsonObject.getString("location")
//                weapon.ascensionMaterial = jsonObject.getString("location") Надо добавить сам материал!!

                weapon.imageUrl = Uri.parse("$BASE_URL/weapons/${name.lowercase()}/icon")
                activity!!.runOnUiThread {
                    val card = CustomWeaponCard(requireContext(), weapon) 
                    println(card)
                    weaponContainer.addView(card)
                }


            }
        })

    }

}