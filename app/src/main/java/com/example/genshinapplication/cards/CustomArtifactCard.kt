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
import com.example.genshinapplication.activities.ArtifactProfileActivity
import com.example.genshinapplication.models.Artifact
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CustomArtifactCard : CardView {

    constructor(context: Context, artifact: Artifact) :
            this(
                context, artifact.name!!, artifact.circletUri!!,
                artifact.max_rarity!!
            )

    constructor(
        context: Context, name: String, circletUri: Uri,
        max_rarity: Int
    ) : super(context!!) {

        val inflater: LayoutInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.artifact_card, this, true)
        val weaponImageView = findViewById<ImageView>(R.id.artifactImageView)
        //https://static.wikia.nocookie.net/gensin-impact/images/5/59/Traveler_Icon.png/revision/latest

        Picasso.get().load(circletUri).into(weaponImageView, object : Callback {

            override fun onSuccess() {}

            //Если картинки не загрузились
            override fun onError(e: Exception?) {
                weaponImageView.setImageResource(R.drawable.no_pic_no_bg)
            }
        })

        println("$circletUri")
        weaponImageView.background = ContextCompat.getDrawable(
            context,
            when (max_rarity) {
                1 -> R.drawable.background_rarity_1_star
                2 -> R.drawable.background_rarity_2_star
                3 -> R.drawable.background_rarity_3_star
                4 -> R.drawable.background_rarity_4_star
                5 -> R.drawable.background_rarity_5_star
                else -> R.drawable.background_rarity_5a_star
            }
        )

        val nameView = findViewById<TextView>(R.id.nameView)
        nameView.text = if (name.length > 9)
            name.take(8) + "..."
        else
            name

        this.setOnClickListener {
            val i = Intent(context, ArtifactProfileActivity::class.java)
            i.putExtra("name", name)
            context.startActivity(i)
        }
    }


}