package com.example.genshinapplication.cards

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.genshinapplication.R
import com.example.genshinapplication.models.GenshinCharacter
import com.example.genshinapplication.models.IDrop
import com.google.android.flexbox.FlexboxLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

//homeCard
//dropImageView
class CustomHomeCard : CardView {

    constructor(
        context: Context, dropUri: Uri, characters: List<GenshinCharacter>
    ) : super(context!!) {

        val inflater: LayoutInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.home_row, this, true)

        val dropImageView = findViewById<ImageView>(R.id.dropImageView)
//        characterImageView.setImageURI(characterIcon)
        //https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest
        Picasso.get().load(dropUri).into(dropImageView, object : Callback {

            override fun onSuccess() {}

            //Если картинки не загрузились
            override fun onError(e: Exception?) {
                Picasso.get()
                    .load("https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest")
                    .into(dropImageView)
            }
        })

        val background = ContextCompat.getDrawable(context, R.drawable.background_rarity_1_star)
        dropImageView.background = background //Сменить фон
        val charactersContainer = findViewById<FlexboxLayout>(R.id.characterContainer)
        println("$dropUri")
        for(c in characters){
            val card = CustomMiniCharacterCard(context, c)
            charactersContainer.addView(card)
        }

//        this.setOnClickListener {
//            val i = Intent(context, WeaponProfileActivity::class.java)
//            i.putExtra("name", name)
//            context.startActivity(i)
//        }
    }

}