package com.example.genshinapplication.activities

import android.app.ActionBar.LayoutParams
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.genshinapplication.R
import com.example.genshinapplication.cards.CustomMaterialCard
import com.example.genshinapplication.helpers.BASE_URL
import com.example.genshinapplication.helpers.MyDatabaseHelper
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


class CharacterProfileActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var cardsMaterials: FlexboxLayout
    lateinit var cardsMaterialsBooks: FlexboxLayout
    lateinit var skrollBgView: ScrollView
    private var mDBHelper: MyDatabaseHelper? = null
    private var mDb: SQLiteDatabase? = null
    lateinit var switchFavorite: Switch
    lateinit var switchMy: Switch
    lateinit var switchFollow: Switch
    lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_profile)

        switchMy = findViewById(R.id.switchHaveCharacter)
//        val prefs = PreferenceManager.getDefaultSharedPreferences(this.applicationContext)
        switchFollow = findViewById(R.id.followBookSwitch)
        switchFavorite = findViewById(R.id.favoriteCharacterSwitch)
        imageView = findViewById(R.id.imageView)
        //Для всего, кроме книг
        cardsMaterials = findViewById(R.id.cardsMaterials)
        //Для книг
        cardsMaterialsBooks = findViewById(R.id.cardsMaterialsBooks)
        val nameView = findViewById<TextView>(R.id.nameView)
        nameView.text = intent.extras!!.getString("name")
        name = intent.extras!!.getString("name")!!.lowercase().replace(" ", "-")
        val client = OkHttpClient()
        skrollBgView = findViewById(R.id.skrollBgView)

        getCharacterInfo(client, name)
        getDropFromNormalBoss(client)
        getDropFromWeeklyBoss(client)
//        getDropGems(client)
        getDropFromEnemy(client)
        getDropBooks(client)
//        getDropFromWorld(client)



        mDBHelper = MyDatabaseHelper(applicationContext)
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

        //Задаем запросы SQL для каждого свича
        val myString = "SELECT CHARACTERS_COLUMN_ID, CHARACTERS_COLUMN_AM_I_HAVE FROM CHARACTERS_TABLE_NAME WHERE COLUMN_CHARACTER_NAME = "
        val favoriteString = "SELECT CHARACTERS_COLUMN_ID, CHARACTERS_COLUMN_IS_FAVORITE FROM CHARACTERS_TABLE_NAME WHERE COLUMN_CHARACTER_NAME = "
        val followString = "SELECT CHARACTERS_COLUMN_ID, CHARACTERS_COLUMN_AM_I_FOLLOW FROM CHARACTERS_TABLE_NAME WHERE COLUMN_CHARACTER_NAME = "
        //Ставим свичи в нужное положение
        if (mDBHelper!!.getSwitchState(name,myString) == 1) switchMy.setChecked(true)
        if (mDBHelper!!.getSwitchState(name,followString) == 1) switchFollow.setChecked(true)
        if (mDBHelper!!.getSwitchState(name,favoriteString) == 1) switchFavorite.setChecked(true)

        var s = mDBHelper!!.getSwitchState(name,myString)
//        runOnUiThread { }
        switchMy.setOnCheckedChangeListener{ _, s ->
            var i: Int = if(s) 1
            else 0
            mDBHelper!!.updateSwitchState(i, name, "CHARACTERS_COLUMN_AM_I_HAVE")
        }

//        s = mDBHelper!!.getSwitchState(name,myString)
        switchFavorite.setOnCheckedChangeListener{ _, s ->
            var i: Int = if(s) 1
            else 0
            mDBHelper!!.updateSwitchState(i, name, "CHARACTERS_COLUMN_IS_FAVORITE")
        }
        switchFollow.setOnCheckedChangeListener{ _, s ->
            var i: Int = if(s) 1
            else 0
            mDBHelper!!.updateSwitchState(i, name, "CHARACTERS_COLUMN_AM_I_FOLLOW")
        }
//       println( mDBHelper!!.getSwitchState(name,myString).toString() + "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV")
//        checkSwitchState(myString,name, switchMy)
//        checkSwitchState(favoriteString)
//        checkSwitchState(followString)

    }

    fun createDropCard(dropName: String, dropImage: Uri, dropRarity: Int) {
        val card = CustomMaterialCard(applicationContext, dropName, dropImage, dropRarity)
        cardsMaterials.addView(card)
    }

    fun createDropCardBooks(dropName: String, dropImage: Uri, dropRarity: Int) {
        val card = CustomMaterialCard(applicationContext, dropName, dropImage, dropRarity)
        cardsMaterialsBooks.addView(card)
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
                        if (arr[i].toString() == checkName(name)) {
                            val itemsArr = jObject.getJSONArray("items")
                            var itemI = 0
                            while (itemI < itemsArr.length()) {
                                val thisItem =
                                    itemsArr.getJSONObject(itemI)  // Сам предмет, конкретный, типо Книжки "Учения о свободе"
                                runOnUiThread {
                                    createDropCardBooks(
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

                                }
                                itemI++
                            }
                            runOnUiThread {
                                //Дни недели вставляем
                                val weekDay = TextView(applicationContext)
                                weekDay.layoutParams = LayoutParams(
                                    LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT
                                )
                                weekDay.text =
                                    jObject.getJSONArray("availability").toString().drop(1)
                                        .dropLast(1)
                                cardsMaterialsBooks.addView(weekDay)
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
                    val dropName = jObject.getString("name")
                    val arr = jObject.getJSONArray("characters")
                    var i = 0

                    while (i < arr.length()) { //Идем по массиву персонажей
                        if (name == "xinyan") {
                            name = "Xinyan"
                        }
                        if (arr[i].toString() == checkName(name)) {

                            runOnUiThread {

                                createDropCard(
                                    dropName,
                                    Uri.parse(
                                        "$BASE_URL/materials/boss-material/${
                                            key.replace("'", "-") //Надо получить заголовок

                                        }"
                                    ),
                                    4 //У такого дропа редкость всегда 4
                                )


                            }
                        }
                        i++
                        if (name == "Xinyan") {
                            name = "xinyan" //У Синь Янь проблемы с именем :(
                        }
                    }
                }

            }
        })
    }

    private fun getDropFromWeeklyBoss(client: OkHttpClient) {
        val request = Request
            .Builder()
            .url("$BASE_URL/materials/talent-boss")
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
                    val dropName = jObject.getString("name")
                    val arr = jObject.getJSONArray("characters")
                    var i = 0
                    while (i < arr.length()) { //Идем по массиву персонажей
                        if (arr[i].toString() == checkName(name)) {

                            runOnUiThread {
                                createDropCard(
                                    dropName,
                                    Uri.parse(
                                        "$BASE_URL/materials/talent-boss/${
                                            key.replace("'", "-") //Надо получить заголовок
                                        }"
                                    ),
                                    5 //У такого дропа редкость всегда 5
                                )


                            }
                        }
                        i++
                    }
                }

            }
        })
    }

    private fun getDropGems(client: OkHttpClient) {
        val request = Request
            .Builder()
            .url("$BASE_URL/materials/character-ascension")
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
                        if (arr[i].toString() == checkName(name)) {
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

                                }
                                itemI++
                            }
                            runOnUiThread {
                                //Дни недели вставляем
                                val weekDay = TextView(applicationContext)
                                weekDay.layoutParams = LayoutParams(
                                    LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT
                                )
                                weekDay.text =
                                    jObject.getJSONArray("availability").toString().drop(1)
                                        .dropLast(1)
                                cardsMaterials.addView(weekDay)
                            }
                        }
                        i++
                    }
                }

            }
        })
    }

    private fun getDropFromEnemy(client: OkHttpClient) {
        val request = Request
            .Builder()
            .url("$BASE_URL/materials/common-ascension")
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
                    if (jObject.has("characters")) {
                        val arr = jObject.getJSONArray("characters")
                        var i = 0
                        while (i < arr.length()) {
                            if (arr[i].toString() == checkName(name)) {
                                val itemsArr = jObject.getJSONArray("items")
                                var itemI = 0
                                while (itemI < itemsArr.length()) {
                                    val thisItem =
                                        itemsArr.getJSONObject(itemI)  // Сам предмет, конкретный, типо Книжки "Учения о свободе"
                                    runOnUiThread {
                                        createDropCard(
                                            thisItem.getString("name"),
                                            Uri.parse(
                                                "$BASE_URL/materials/common-ascension/${
                                                    thisItem.getString(
                                                        "id"
                                                    ).replace("'", "-")
                                                }"
                                            ),
                                            thisItem.getInt("rarity")
                                        )

                                    }
                                    itemI++
                                }
                            }
                            i++
                        }
                    }
                }

            }
        })
    }

    //Надо с этой функцией поработать
    private fun getDropFromWorld(client: OkHttpClient){
        val request = Request
            .Builder()
            .url("$BASE_URL/materials/local-specialties")
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
                    val dropName = jObject.getString("name")
                    val arr = jObject.getJSONArray("characters")
                    var i = 0

                    while (i < arr.length()) { //Идем по массиву персонажей
                        if (name == "xinyan") {
                            name = "Xinyan"
                        }
                        if (arr[i].toString() == checkName(name)) {

                            runOnUiThread {

                                createDropCard(
                                    dropName,
                                    Uri.parse(
                                        "$BASE_URL/materials/local-specialties/${
                                            jObject.getString("id") //Надо получить заголовок

                                        }"
                                    ),
                                    1 //У такого дропа редкость всегда 1
                                )


                            }

                        }
                        i++
                        if (name == "Xinyan") {
                            name = "xinyan" //У Синь Янь проблемы с именем :(
                        }
                    }
                }

            }
        })
    }


    private fun getCharacterInfo(
        client: OkHttpClient,
        name: String

    ) {  //https://api.genshin.dev/characters/имя-персонажа/icon  - картинки персонажа  (в частности, иконка)
        var nameForPic = checkName(name.lowercase().replace(" ", "-"))
        val request = Request
            .Builder()
            .url("$BASE_URL/characters/${nameForPic}")
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
                character.nation = jsonObject.getString("nation")
                character.description = jsonObject.getString("description")
                character.rarity = jsonObject.getInt("rarity")
//                val dat = jsonObject.getString("birthday").split("-")
//                character.birthday = LocalDate.of(0, dat[1].toInt(), dat[2].toInt() )
                var n = Uri.parse("$BASE_URL/characters/$nameForPic/icon-big")
                character.characterUri = n
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
                        when (character.rarity) {
                            4 -> R.drawable.background_rarity_4_star
                            5 -> R.drawable.background_rarity_5_star
                            else -> R.drawable.background_rarity_5a_star
                        }
                    )

                    skrollBgView.setBackgroundResource(
                        when(character.vision) {
                            "Anemo" -> R.drawable.anemo_bg
                            "Hydro" -> R.drawable.hydro_bg
                            "Electro" -> R.drawable.electro_bg
                            "Pyro" -> R.drawable.pyro_bg
                            "Geo" -> R.drawable.geo_bg
                            "Cryo" -> R.drawable.cryo_bg
                            "Dendro" -> R.drawable.dendro_bg
                            else -> R.drawable.anemo_bg
                        }
                    )

                    val leftImage = findViewById<ImageView>(R.id.visionImageWiew)
                    leftImage.setImageResource(
                        when (character.vision) {
                            "Anemo" -> R.drawable.anemo
                            "Hydro" -> R.drawable.hydro
                            "Electro" -> R.drawable.electro
                            "Pyro" -> R.drawable.pyro
                            "Geo" -> R.drawable.geo
                            "Cryo" -> R.drawable.cryo
                            "Dendro" -> R.drawable.dendro
                            else -> R.drawable.pyro
                        }
                    )

                    val rightImage = findViewById<ImageView>(R.id.weaponImageView)
                    rightImage.setImageResource(
                        when (character.weapon) {
                            "Bow" -> R.drawable.bow_icon
                            "Catalyst" -> R.drawable.catalyst_icon
                            "Claymore" -> R.drawable.claymore_icon
                            "Sword" -> R.drawable.sword_icon
                            "Polearm" -> R.drawable.polearm_icon
                            else -> R.drawable.bow_icon
                        }
                    )

                    val nationText = findViewById<TextView>(R.id.nationView)
                    val descriptionText = findViewById<TextView>(R.id.descriptionView)
                    val visionText = findViewById<TextView>(R.id.visionView)
                    val weaponText = findViewById<TextView>(R.id.weaponView)
                    nationText.text = character.nation
                    descriptionText.text = character.description
                    visionText.text = character.vision
                    weaponText.text = character.weapon
                }


            }
        })

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
//Проверка для Switch
    fun checkSwitchState(string: String, name: String, switchName:Switch){



    }


}