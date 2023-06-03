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
import com.example.genshinapplication.WeaponProfileActivity
import com.example.genshinapplication.models.GenshinCharacter
import com.example.genshinapplication.models.Weapon
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class CustomWeaponCard : CardView {

    constructor(context: Context, weapon: Weapon) :
            this(
                context, weapon.name!!, weapon.imageUrl!!,
                weapon.rarity!!
            )

    constructor(
        context: Context, name: String, imageUrl: Uri,
        rarity: Int
    ) : super(context!!) {

        val inflater: LayoutInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.weapon_card, this, true)

        val weaponImageView = findViewById<ImageView>(R.id.weaponImageView)
//        characterImageView.setImageURI(characterIcon)
        //https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest
        Picasso.get().load(imageUrl).into(weaponImageView, object : Callback {

            override fun onSuccess() {}

            //Если картинки не загрузились
            override fun onError(e: Exception?) {
                Picasso.get()
                    .load("https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest")
                    .into(weaponImageView)
            }
        })

        println("$imageUrl")
        weaponImageView.background = ContextCompat.getDrawable(
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



        this.setOnClickListener {
            val i = Intent(context, WeaponProfileActivity::class.java)
            i.putExtra("name", name)
            context.startActivity(i)
        }
    }


}