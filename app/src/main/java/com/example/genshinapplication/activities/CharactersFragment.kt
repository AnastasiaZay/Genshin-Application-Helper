package com.example.genshinapplication.activities

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.genshinapplication.R
import com.example.genshinapplication.cards.CustomCharacterCard
import com.example.genshinapplication.helpers.BASE_URL
import com.example.genshinapplication.models.GenshinCharacter
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayout
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class CharactersFragment : Fragment() {

    lateinit var characterContainer: FlexboxLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_characters, container, false)

        characterContainer = view.findViewById(R.id.characterContainer)
//        run()

        return view
    }


    private fun run() {
        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("$BASE_URL/characters")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, res: Response) {
                val jsonArr = JSONArray(res.body()!!.string())

                var iterator = 0
                while (iterator < jsonArr.length()) {
                    getCharacterInfo(client, jsonArr.getString(iterator))
                    iterator++
                }

            }
        })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        run()
    }

    fun getCharacterInfo(
        client: OkHttpClient,
        name: String
    ) {  //https://api.genshin.dev/characters/имя-персонажа/icon  - картинки персонажа  (в частности, иконка)
        val request = Request
            .Builder()
            // Имя персов пиши ЧЕРЕЗ-ТИРЕ
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
                character.vision = jsonObject.getString("vision")
                character.weapon = jsonObject.getString("weapon")
                character.rarity = jsonObject.getInt("rarity")
                character.characterUri =
                    Uri.parse("$BASE_URL/characters/${name.lowercase()}/icon-big")



                activity?.runOnUiThread {
//                    characterContainer.
                    val card = CustomCharacterCard(requireContext(), character)
                    println(card)
                    characterContainer.addView(card)
                }


            }
        })

    }
}
