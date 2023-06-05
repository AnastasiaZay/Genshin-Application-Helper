package com.example.genshinapplication.activities

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.genshinapplication.R
import com.example.genshinapplication.cards.CustomHomeCard
import com.example.genshinapplication.helpers.BASE_URL
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDate

//homeCard
//dropImageView
class HomeFragment : Fragment() {
    lateinit var homeCard: LinearLayout
    val todayDayOfWeek = LocalDate.now().dayOfWeek.name.lowercase()

    //    lateinit var cardsMaterials: FlexboxLayout
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        // Inflate the layout for this fragment
        homeCard = view.findViewById(R.id.homeCard)!!
        getBookInfo(client = OkHttpClient())
        return view
    }

    fun createHomeCard(dropUri: Uri, characters: JSONArray) {
//        val card = CustomHomeCard(requireContext(), dropUri, characters)
//        homeCard.addView(card)

    }

    private fun getBookInfo(client: OkHttpClient) {

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
                while (keys.hasNext()) { //todayDayOfWeek
                    val key = keys.next()
                    val jObject = jsonObject.getJSONObject(key)
                    val arr = jObject.getJSONArray("availability")
                    var i = 0
                    while (i < arr.length()) {

                        if (arr[i].toString().lowercase() == todayDayOfWeek) {
                            val item = jObject.getJSONArray("items").getJSONObject(2)
                            val characters = jObject.getJSONArray("characters")
                            println(item.toString() + "rjrjrjrrjrjrjrjrjrjrjrjrjrrj")

                            activity?.runOnUiThread {
                                val card = CustomHomeCard(
                                    requireContext(), Uri.parse(
                                        "$BASE_URL/materials/talent-book/${
                                            item.getString(
                                                "id"
                                            )
                                        }"
                                    ), characters
                                )
                                homeCard.addView(card)

                            }

                        }

                        i++
                    }
                }
            }


        })


    }
}