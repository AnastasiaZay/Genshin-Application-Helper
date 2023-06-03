package com.example.genshinapplication.cards

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.genshinapplication.R
import com.example.genshinapplication.activities.CharacterProfileActivity
import com.example.genshinapplication.models.GenshinCharacter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class CustomMiniCharacterCard : CardView {

    constructor(context: Context, character: GenshinCharacter) :
            this(
                context, character.name!!, character.characterUri!!,
                character.rarity!!
            )

    constructor(
        context: Context, name: String, characterIcon: Uri,
        rarity: Int
    ) : super(context!!) {

        val inflater: LayoutInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.character_mini_card, this, true)

        val characterImageView = findViewById<ImageView>(R.id.characterImageView)
//        characterImageView.setImageURI(characterIcon)
        //https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest
        Picasso.get().load(characterIcon).into(characterImageView, object : Callback {

            override fun onSuccess() {}

            //Если картинки не загрузились
            override fun onError(e: Exception?) {
                Picasso.get()
                    .load("https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest")
                    .into(characterImageView)
            }
        })

        println("$characterIcon")
        characterImageView.background = ContextCompat.getDrawable(
            context,
            when (rarity) {
                1 -> R.drawable.background_rarity_1_star
                2 -> R.drawable.background_rarity_2_star
                3 -> R.drawable.background_rarity_3_star
                4 -> R.drawable.background_rarity_4_star
                5 -> R.drawable.background_rarity_5_star
                else -> R.drawable.background_rarity_5a_star
            }
        )




        this.setOnClickListener {
            val i = Intent(context, CharacterProfileActivity::class.java)
            i.putExtra("name", name)
            context.startActivity(i)
        }
    }
}