package com.example.genshinapplication.activities

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.genshinapplication.R
import com.example.genshinapplication.cards.CustomArtifactCard
import com.example.genshinapplication.cards.CustomWeaponCard
import com.example.genshinapplication.models.Artifact
import com.google.android.flexbox.FlexboxLayout
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ArtifactsFragment : Fragment() {
    lateinit var artifactContainer: FlexboxLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_artifacts, container, false)

        artifactContainer = view.findViewById(R.id.artifactsContainer)
//        run()

        return view
    }
    fun addCharCard(art: Artifact) {
        val card = CustomArtifactCard( requireContext(), art )
        println( card )
        artifactContainer.addView( card )
    }

    fun run(){
        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("$BASE_URL/artifacts")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException?) {
                call.cancel()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call?, res: Response) {
                val jsonArr = JSONArray(res.body()!!.string())

                var iterator = 0
                val lst = ArrayList<Artifact>()
                while (iterator < jsonArr.length()) {
                    getArtifactInfo(client,jsonArr.getString(iterator))
                    iterator++
                }

            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        run()
    }
    fun getArtifactInfo(client: OkHttpClient, name: String) {  //https://api.genshin.dev/characters/имя-персонажа/icon  - картинки персонажа  (в частности, иконка)
        val request = Request
            .Builder()
            .url("$BASE_URL/artifacts/${name.lowercase()}")
            .build()

        //Вносим артефакт
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

            private fun parse(response: String){
                val jsonObject = JSONObject(response)

                artifact.name = jsonObject.getString("name")
                artifact.max_rarity = jsonObject.getInt("max_rarity")
//                artifact.piece_2_bonus = jsonObject.getString("2-piece_bonus")
//                artifact.piece_4_bonus = jsonObject.getString("4-piece_bonus")

                artifact.circletUri = Uri.parse("$BASE_URL/artifacts/${name.lowercase()}/circlet-of-logos")



                activity?.runOnUiThread {
                    val card = CustomArtifactCard(requireContext(), artifact)
                    println(card)
                    artifactContainer.addView(card)
                }
//                artifact.flowerUri = Uri.parse("$BASE_URL/artifacts/${name.lowercase()}/flower-of-life")
//                artifact.gobletUri = Uri.parse("$BASE_URL/artifacts/${name.lowercase()}/goblet-of-eonothem")
//                artifact.plumeUri = Uri.parse("$BASE_URL/artifacts/${name.lowercase()}/plume-of-death")
//                artifact.sandsUri = Uri.parse("$BASE_URL/artifacts/${name.lowercase()}/sands-of-eon")

            }
        })

    }
}