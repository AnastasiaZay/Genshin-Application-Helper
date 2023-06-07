package com.example.genshinapplication.activities

import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.genshinapplication.R
import com.example.genshinapplication.cards.CustomCharacterCard
import com.example.genshinapplication.helpers.BASE_URL
import com.example.genshinapplication.helpers.MyDatabaseHelper
import com.example.genshinapplication.models.GenshinCharacter
import com.google.android.flexbox.FlexboxLayout
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class ProfileFragment : Fragment() {
    private lateinit var textView: TextView
    private var mDBHelper: MyDatabaseHelper? = null
    private var mDb: SQLiteDatabase? = null
    lateinit var favoriteCharactersContainer: FlexboxLayout
    lateinit var followCharactersContainer: FlexboxLayout
    lateinit var myCharactersContainer: FlexboxLayout



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_profile,
            container,
            false
        )

        favoriteCharactersContainer = view.findViewById(R.id.favoriteCharactersContainer)
        followCharactersContainer = view.findViewById(R.id.followCharactersContainer)
        myCharactersContainer = view.findViewById(R.id.myCharactersContainer)

        textView = view.findViewById(R.id.textView)


        run()


//        // Пропишем обработчик клика кнопки
//        button!!.setOnClickListener {
//            var product = ""
//            val cursor = mDb!!.rawQuery("SELECT * FROM clients", null)
//            cursor.moveToFirst()
//            while (!cursor.isAfterLast) {
//                product += cursor.getString(1) + " | "
//                cursor.moveToNext()
//            }
//            cursor.close()
//            textView!!.text = product
//        }


        return view
    }

    private fun run() {
        val client = OkHttpClient()

        //Нужен запрос в базу, а не в апи
        val request: Request = Request.Builder()
            .url("$BASE_URL/characters")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException?) {
                call.cancel()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call?, res: Response) {
//                val jsonArr = JSONArray(res.body()!!.string())
                val arrFav = getArrayCharacters("SELECT CHARACTERS_COLUMN_ID, COLUMN_CHARACTER_NAME FROM CHARACTERS_TABLE_NAME WHERE CHARACTERS_COLUMN_IS_FAVORITE is 1")
                var iterator =0
                while (iterator < arrFav!!.size) {
                    getFavoriteCharacterInfo(
                        client,
                        arrFav[iterator]
                    ) //Будет принимать массив имен нужных персонажей
                    iterator++
                }

                val arrFoll = getArrayCharacters("SELECT CHARACTERS_COLUMN_ID, COLUMN_CHARACTER_NAME FROM CHARACTERS_TABLE_NAME WHERE CHARACTERS_COLUMN_AM_I_FOLLOW is 1")
                iterator =0
                while (iterator < arrFoll!!.size) {
                    getFollowteCharacterInfo(
                        client,
                        arrFoll[iterator]
                    ) //Будет принимать массив имен нужных персонажей
                    iterator++
                }

                val arrMy = getArrayCharacters("SELECT CHARACTERS_COLUMN_ID, COLUMN_CHARACTER_NAME FROM CHARACTERS_TABLE_NAME WHERE CHARACTERS_COLUMN_AM_I_HAVE is 1")
                iterator =
                    0 //Надо, чтобы он искал только по именам тех, у кого в базе данных стоит нужный флажок
                while (iterator < arrMy!!.size) {
                    getMyCharacterInfo(
                        client,
                        arrMy[iterator]
                    ) //Будет принимать массив имен нужных персонажей
                    iterator++
                }


//            }
            }
        })
    }


//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        run()
//    }

    fun getFavoriteCharacterInfo(
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
                    val card = CustomCharacterCard(requireContext(), character)
                    println(card)
                    favoriteCharactersContainer.addView(card)
                }

            }
        })
    }
    fun getFollowteCharacterInfo(
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
                    val card = CustomCharacterCard(requireContext(), character)
                    println(card)
                    followCharactersContainer.addView(card)
                }

            }
        })
    }

    fun getMyCharacterInfo(
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
                    val card = CustomCharacterCard(requireContext(), character)
                    println(card)
                    myCharactersContainer.addView(card)
                }

            }
        })
    }
//    private fun dbDoing() {
//        mDBHelper = MyDatabaseHelper(this.requireContext())
//        try {
//            mDBHelper!!.updateDataBase()
//        } catch (mIOException: IOException) {
//            throw Error("UnableToUpdateDatabase")
//        }
//        mDb = try {
//            mDBHelper!!.writableDatabase
//        } catch (mSQLException: SQLException) {
//            throw mSQLException
//        }
//        var product = ""
//        val cursor = mDb!!.rawQuery("SELECT * FROM CHARACTERS_TABLE_NAME", null)
//        cursor.moveToFirst()
//        while (!cursor.isAfterLast) {
//            product += cursor.getString(1) + " | "
//            cursor.moveToNext()
//        }
//        cursor.close()
//        textView!!.text = product
//    }


    fun getArrayCharacters(s: String): ArrayList<String>? {
        // В s заносим sql запрос, пример -  "SELECT CHARACTERS_COLUMN_ID, COLUMN_CHARACTER_NAME FROM CHARACTERS_TABLE_NAME WHERE CHARACTERS_COLUMN_AM_I_HAVE is 1"
        mDBHelper = MyDatabaseHelper(this.requireContext())
        try {
            mDBHelper!!.updateDataBase()
        } catch (mIOException: IOException) {
            throw Error("UnableToUpdateDatabase")
        }
        mDb = try {
            mDBHelper!!.writableDatabase
        } catch (mSQLException: SQLException) {
            throw mSQLException
        }
        var charactersArray: ArrayList<String>? = ArrayList<String>() //= listOf<String>()
        val cursor = mDb!!.rawQuery(
            s,
            null
        )
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            println(cursor.getString(1))

            charactersArray?.add(cursor.getString(1).toString())
            cursor.moveToNext()
            println(charactersArray)
        }
        cursor.close()
        return charactersArray
    }



}


