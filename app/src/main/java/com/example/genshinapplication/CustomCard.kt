package com.example.genshinapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.genshinapplication.models.GenshinCharacter
import com.squareup.picasso.Picasso
import java.net.URI

class CustomCard : CardView {

    constructor(context: Context, character: GenshinCharacter) :
            this(
                context, character.name!!, character.characterUri!!,
                character.rarity!!, character.weapon!!, character.vision!!
            )

    constructor(
        context: Context, name: String, characterIcon: Uri,
        rarity: Int, vision: String, weapon: String
    ) : super(context!!) {


        println("$name $rarity -------------------------------------------------")
        val inflater: LayoutInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.character_card, this, true)

        val characterImageView = findViewById<ImageView>(R.id.characterImageView)
        characterImageView.setImageURI(characterIcon)
        characterImageView.background = ContextCompat.getDrawable(
            context,
            when (rarity) {
                4 -> R.drawable.background_rarity_4_star
                5 -> R.drawable.background_rarity_5_star
                else -> R.drawable.background_rarity_3_star
            }
        )

        val nameView = findViewById<TextView>(R.id.nameView)
        nameView.text = name

        val leftImage = findViewById<ImageView>(R.id.leftImageView)
        leftImage.setImageResource(
            when (vision) {
                "anemo" -> R.drawable.anemo
                "hydro" -> R.drawable.hydro
                "electtro" -> R.drawable.electro
                "pyro" -> R.drawable.pyro
                "geo" -> R.drawable.geo
                "cryo" -> R.drawable.cryo
                "dendro" -> R.drawable.dendro
                else -> R.drawable.anemo
            }
        )

        val rightImage = findViewById<ImageView>(R.id.rightImageView)
        rightImage.setImageResource(
            when (vision) {
                "bow" -> R.drawable.bow_icon
                "catalyst" -> R.drawable.catalyst_icon
                "claymore" -> R.drawable.claymore_icon
                "sword" -> R.drawable.sword_icon
                "polearm" -> R.drawable.polearm_icon
                else -> R.drawable.bow_icon
            }
        )

        this.setOnClickListener {
            val i = Intent(context, CharacterProfileActivity::class.java)
            i.putExtra("name", name)
            context.startActivity( i )
        }
    }
}