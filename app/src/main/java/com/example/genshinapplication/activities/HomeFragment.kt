package com.example.genshinapplication.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.genshinapplication.R
import com.google.android.flexbox.FlexboxLayout

//homeCard
//dropImageView
class HomeFragment : Fragment() {
    lateinit var homeCard: CardView

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        // Inflate the layout for this fragment
        homeCard = view.findViewById(R.id.homeCard)!!

        return view
    }


}