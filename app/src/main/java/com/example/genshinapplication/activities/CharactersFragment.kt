package com.example.genshinapplication.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.genshinapplication.CustomCard
import com.example.genshinapplication.R
import com.google.android.flexbox.FlexboxLayout

class CharactersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_characters, container, false)

        val characterContainer = view.findViewById<FlexboxLayout>(R.id.characterContainer)

        val apiHelper = APIHelper()
        val characterList = apiHelper.getAllCharacters()

        for ( ch in characterList ) {
            val card = CustomCard( view.context, ch )
            println( card )
            characterContainer.addView( card )
        }

        return view
    }

}