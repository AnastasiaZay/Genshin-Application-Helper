package com.example.genshinapplication.cards

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.genshinapplication.CharacterProfileActivity
import com.example.genshinapplication.R
import com.example.genshinapplication.models.GenshinCharacter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class CustomCharacterCard : CardView {

    constructor(context: Context, character: GenshinCharacter) :
            this(
                context, character.name!!, character.characterUri!!,
                character.rarity!!, character.vision!!, character.weapon!!
            )

    constructor(
        context: Context, name: String, characterIcon: Uri,
        rarity: Int, vision: String, weapon: String
    ) : super(context!!) {

        val inflater: LayoutInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.character_card, this, true)

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
                4 -> R.drawable.background_rarity_4_star
                5 -> R.drawable.background_rarity_5_star
                else -> R.drawable.background_rarity_3_star
            }
        )

        val nameView = findViewById<TextView>(R.id.nameView)
        nameView.text = if (name.length > 9)
            name.take(9) + "..."
        else
            name

        val leftImage = findViewById<ImageView>(R.id.leftImageView)
        leftImage.setImageResource(
            when (vision) {
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

        val rightImage = findViewById<ImageView>(R.id.rightImageView)
        rightImage.setImageResource(
            when (weapon) {
                "Bow" -> R.drawable.bow_icon
                "Catalyst" -> R.drawable.catalyst_icon
                "Claymore" -> R.drawable.claymore_icon
                "Sword" -> R.drawable.sword_icon
                "Polearm" -> R.drawable.polearm_icon
                else -> R.drawable.bow_icon
            }
        )

        this.setOnClickListener {
            val i = Intent(context, CharacterProfileActivity::class.java)
            i.putExtra("name", name)
            context.startActivity(i)
        }
    }
}