package com.example.genshinapplication.cards

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.genshinapplication.R
import com.example.genshinapplication.models.IDrop
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CustomMaterialCard : CardView {

    constructor(context: Context, drop: IDrop) :
            this(
                context, drop.getDropName()!!, drop.getDropUri()!!,
                drop.getDropRarity()!!
            )

    constructor(
        context: Context, name: String, imageUrl: Uri,
        rarity: Int
    ) : super(context!!) {

        val inflater: LayoutInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.drop_card, this, true)

        val materialImageView = findViewById<ImageView>(R.id.materialImageView)
        //https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest

        Picasso.get().load(imageUrl).into(materialImageView, object : Callback {

            override fun onSuccess() {}

            //Если картинки не загрузились
            override fun onError(e: Exception?) {
                Picasso.get()
                    .load("https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest")
                    .into(materialImageView)
            }
        })

        println("$imageUrl")
        materialImageView.background = ContextCompat.getDrawable(
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

        val nameView = findViewById<TextView>(R.id.nameView)
        nameView.text = if (name.length > 23)
            name.take(23) + "..."
        else
            name

        //На будущее
//        this.setOnClickListener {
//            val i = Intent(context, WeaponProfileActivity::class.java)
//            i.putExtra("name", name)
//            context.startActivity(i)
//        }
    }

}