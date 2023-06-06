package com.example.genshinapplication.helpers

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private var mDataBase: SQLiteDatabase? = null
    private val mContext: Context
    private var mNeedUpdate = false

    init {
        DB_PATH =
            if (Build.VERSION.SDK_INT >= 17) context.applicationInfo.dataDir + "/databases/" else "/data/data/" + context.packageName + "/databases/"
        mContext = context
        copyDataBase()
        this.readableDatabase
    }

    @Throws(IOException::class)
    fun updateDataBase() {
        if (mNeedUpdate) {
            val dbFile = File(DB_PATH + DATABASE_NAME)
            if (dbFile.exists()) dbFile.delete()
            copyDataBase()
            mNeedUpdate = false
        }
    }

    private fun checkDataBase(): Boolean {
        val dbFile = File(DB_PATH + DATABASE_NAME)
        return dbFile.exists()
    }

    private fun copyDataBase() {
        if (!checkDataBase()) {
            this.readableDatabase
            close()
            try {
                copyDBFile()
            } catch (mIOException: IOException) {
                throw Error("ErrorCopyingDataBase")
            }
        }
    }

    @Throws(IOException::class)
    private fun copyDBFile() {
        val mInput = mContext.assets.open(DATABASE_NAME)
        val mOutput: OutputStream = FileOutputStream(DB_PATH + DATABASE_NAME)
        val mBuffer = ByteArray(1024)
        var mLength: Int
        while (mInput.read(mBuffer).also { mLength = it } > 0) mOutput.write(mBuffer, 0, mLength)
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }

    @Throws(SQLException::class)
    fun openDataBase(): Boolean {
        mDataBase =
            SQLiteDatabase.openDatabase(
                DB_PATH + DATABASE_NAME,
                null,
                SQLiteDatabase.CREATE_IF_NECESSARY
            )
        return mDataBase != null
    }

    @Synchronized
    override fun close() {
        if (mDataBase != null) mDataBase!!.close()
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase?) {}
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) mNeedUpdate = true
    }


    companion object {
        private const val DATABASE_NAME = "db.db"
        private const val DATABASE_VERSION = 2
        private var DB_PATH = ""
        private const val CHARACTERS_TABLE_NAME = "CHARACTERS_TABLE_NAME"
        private const val CHARACTERS_COLUMN_ID = "CHARACTERS_COLUMN_ID"
        private const val COLUMN_CHARACTER_NAME = "COLUMN_CHARACTER_NAME"
        private const val CHARACTERS_COLUMN_AM_I_HAVE = "CHARACTERS_COLUMN_AM_I_HAVE"
        private const val CHARACTERS_COLUMN_AM_I_FOLLOW = "CHARACTERS_COLUMN_AM_I_FOLLOW"
        private const val WEAPONS_TABLE_NAME = "WEAPONS_TABLE_NAME"
        private const val WEAPONS_COLUMN_ID = "WEAPONS_COLUMN_ID"
        private const val WEAPONS_COLUMN_NAME = "WEAPONS_COLUMN_NAME"
        private const val WEAPONS_COLUMN_AM_I_HAVE = "WEAPONS_COLUMN_AM_I_HAVE"
        private const val WEAPONS_COLUMN_AM_I_FOLLOW = "WEAPONS_COLUMN_AM_I_FOLLOW"
        private const val BOOKS_TABLE_NAME = "BOOKS_TABLE_NAME"
        private const val BOOKS_COLUMN_ID = "BOOKS_COLUMN_ID"
        private const val BOOKS_COLUMN_NAME = "BOOKS_COLUMN_NAME"
        private const val BOOKS_COLUMN_AM_I_FOLLOW = "BOOKS_COLUMN_AM_I_FOLLOW"

    }


}