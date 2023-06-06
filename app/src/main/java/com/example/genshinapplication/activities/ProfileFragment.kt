package com.example.genshinapplication.activities

import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.genshinapplication.R
import com.example.genshinapplication.helpers.MyDatabaseHelper
import java.io.IOException


class ProfileFragment : Fragment() {
    private lateinit var textView: TextView
    private var mDBHelper: MyDatabaseHelper? = null
    private var mDb: SQLiteDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_profile,
            container,
            false
        )

        textView = view.findViewById(R.id.textView)

        dbDoing()


//        // Пропишем обработчик клика кнопки
//        button!!.setOnClickListener {
//            var product = ""
//            val cursor = mDb!!.rawQuery("SELECT * FROM clients", null)
//            cursor.moveToFirst()
//            while (!cursor.isAfterLast) {
//                product += cursor.getString(1) + " | "
//                cursor.moveToNext()
//            }
//            cursor.close()
//            textView!!.text = product
//        }


        return view
    }

    private fun dbDoing() {
        mDBHelper = MyDatabaseHelper(this.requireContext())
        try {
            mDBHelper!!.updateDataBase()
        } catch (mIOException: IOException) {
            throw Error("UnableToUpdateDatabase")
        }
        mDb = try {
            mDBHelper!!.writableDatabase
        } catch (mSQLException: SQLException) {
            throw mSQLException
        }
        var product = ""
        val cursor = mDb!!.rawQuery("SELECT * FROM CHARACTERS_TABLE_NAME", null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            product += cursor.getString(1) + " | "
            cursor.moveToNext()
        }
        cursor.close()
        textView!!.text = product
    }
}