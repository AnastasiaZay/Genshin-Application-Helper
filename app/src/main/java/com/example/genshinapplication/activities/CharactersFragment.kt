package com.example.genshinapplication.activities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.genshinapplication.CustomCard
import com.example.genshinapplication.R
import com.example.genshinapplication.models.GenshinCharacter
import com.google.android.flexbox.FlexboxLayout

class CharactersFragment : Fragment() {

    lateinit var characterContainer: FlexboxLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_characters, container, false)

        characterContainer = view.findViewById(R.id.characterContainer)

        val apiHelper = APIHelper()
        val characterList = apiHelper.getAllCharacters()

        return view
    }
    open fun addCharCard(ch: GenshinCharacter) {
        val card = CustomCard( requireContext(), ch )
        println( card )
        characterContainer.addView( card )
    }
}
