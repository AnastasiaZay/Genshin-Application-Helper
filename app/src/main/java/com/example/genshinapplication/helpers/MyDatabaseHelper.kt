package com.example.genshinapplication.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(private val context: Context?) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = "CREATE TABLE " + CHARACTERS_TABLE_NAME +
                " (" + CHARACTERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CHARACTER_NAME + " TEXT, " +
                CHARACTERS_COLUMN_AM_I_HAVE + " INTEGER, " +
                CHARACTERS_COLUMN_AM_I_FOLLOW + " INTEGER);"
        db.execSQL(query)
        val query2 = "CREATE TABLE " + WEAPONS_TABLE_NAME +
                " (" + WEAPONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WEAPONS_COLUMN_NAME + " TEXT, " +
                WEAPONS_COLUMN_AM_I_HAVE + " INTEGER, " +
                WEAPONS_COLUMN_AM_I_FOLLOW + " INTEGER);"
        db.execSQL(query2)
        val query3 = "CREATE TABLE " + BOOKS_TABLE_NAME +
                " (" + BOOKS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BOOKS_COLUMN_NAME + " TEXT, " +
                BOOKS_COLUMN_AM_I_FOLLOW + " INTEGER);"
        db.execSQL(query3)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE IF EXISTS $CHARACTERS_TABLE_NAME")
        onCreate(db)
    }

    fun addCharacters(){}

    fun updateHavingCharacterInCharacterProfile(){}

    companion object {
        private const val DATABASE_NAME = "HeavingCharactersInfo.db"
        private const val DATABASE_VERSION = 1
        private const val CHARACTERS_TABLE_NAME = "characters"
        private const val CHARACTERS_COLUMN_ID = "_id"
        private const val COLUMN_CHARACTER_NAME = "characters_names"
        private const val CHARACTERS_COLUMN_AM_I_HAVE = false
        private const val CHARACTERS_COLUMN_AM_I_FOLLOW = false
        private const val WEAPONS_TABLE_NAME = "weapons"
        private const val WEAPONS_COLUMN_ID = "_id"
        private const val WEAPONS_COLUMN_NAME = "weapons_names"
        private const val WEAPONS_COLUMN_AM_I_HAVE = false
        private const val WEAPONS_COLUMN_AM_I_FOLLOW = false
        private const val BOOKS_TABLE_NAME = "weapons"
        private const val BOOKS_COLUMN_ID = "_id"
        private const val BOOKS_COLUMN_NAME = "weapons_names"
        private const val BOOKS_COLUMN_AM_I_FOLLOW = false
    }


}