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

/*
class MyDatabaseHelper(private val context: Context?) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    private var mNeedUpdate = false
    private var mDataBase: SQLiteDatabase? = null
    private var mContext: Context? = null
        fun DatabaseHelper(context: Context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION)
        DB_PATH =
            if (Build.VERSION.SDK_INT >= 17) context.applicationInfo.dataDir + "/databases/" else "/data/data/" + context.packageName + "/databases/"
        this.mContext = context
        copyDataBase()
        this.readableDatabase
    }
    override fun onCreate(db: SQLiteDatabase) {
//        val query = "CREATE TABLE " + CHARACTERS_TABLE_NAME +
//                " (" + CHARACTERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COLUMN_CHARACTER_NAME + " TEXT, " +
//                CHARACTERS_COLUMN_AM_I_HAVE + " INTEGER, " +
//                CHARACTERS_COLUMN_AM_I_FOLLOW + " INTEGER);"
//        db.execSQL(query)
//        val query2 = "CREATE TABLE " + WEAPONS_TABLE_NAME +
//                " (" + WEAPONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                WEAPONS_COLUMN_NAME + " TEXT, " +
//                WEAPONS_COLUMN_AM_I_HAVE + " INTEGER, " +
//                WEAPONS_COLUMN_AM_I_FOLLOW + " INTEGER);"
//        db.execSQL(query2)
//        val query3 = "CREATE TABLE " + BOOKS_TABLE_NAME +
//                " (" + BOOKS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                BOOKS_COLUMN_NAME + " TEXT, " +
//                BOOKS_COLUMN_AM_I_FOLLOW + " INTEGER);"
//        db.execSQL(query3)
        /*
                db.execSQL("""
                    create table $WEAPONS_TABLE_NAME (
                    $WEAPONS_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $WEAPONS_COLUMN_NAME TEXT,
                    $WEAPONS_COLUMN_AM_I_HAVE INTEGER DEFAULT 0,
                    $WEAPONS_COLUMN_AM_I_FOLLOW INTEGER DEFAULT 0
                )
                    """.trimIndent()
                )
                db.execSQL("""
                    create table $CHARACTERS_TABLE_NAME (
                    $CHARACTERS_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_CHARACTER_NAME TEXT,
                    $CHARACTERS_COLUMN_AM_I_HAVE INTEGER DEFAULT 0,
                    $CHARACTERS_COLUMN_AM_I_FOLLOW INTEGER DEFAULT 0
                )
                    """.trimIndent()
                )
                db.execSQL("""
                    create table $BOOKS_TABLE_NAME (
                    $BOOKS_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $BOOKS_COLUMN_NAME TEXT,
                    $BOOKS_COLUMN_AM_I_FOLLOW INTEGER DEFAULT 0
                )
                    """.trimIndent()
                )*/
    }

    //    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
////        db.execSQL("DROP TABLE IF EXISTS $CHARACTERS_TABLE_NAME")
////        db.execSQL("DROP TABLE IF EXISTS $BOOKS_TABLE_NAME")
////        db.execSQL("DROP TABLE IF EXISTS $WEAPONS_TABLE_NAME")
////        onCreate(db)
//    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) mNeedUpdate = true
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
        val mInput: InputStream? = mContext?.assets?.open(DATABASE_NAME)
        val mOutput: OutputStream = FileOutputStream(DB_PATH + DATABASE_NAME)
        val mBuffer = ByteArray(1024)
        var mLength: Int
        if (mInput != null) {
            while (mInput.read(mBuffer).also { mLength = it } > 0) mOutput.write(mBuffer, 0, mLength)
        }
        mOutput.flush()
        mOutput.close()
        mInput?.close()
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
        mDataBase?.close()
        super.close()
    }

    fun updateHavingCharacterInCharacterProfile() {
        var db: SQLiteDatabase = this.writableDatabase
        lateinit var cv: ContentValues
        cv.put("CHARACTERS_COLUMN_AM_I_HAVE", true) //Первое - название столбца, второе - инфа
        var result = db.insert(CHARACTERS_TABLE_NAME, null, cv)
//        if (result.equals(-1)){
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
//        }
//        else Toast.makeText(context, "Sucsess", Toast.LENGTH_SHORT).show()
        db.insert(CHARACTERS_TABLE_NAME, null, cv)
    } */
class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private var mDataBase: SQLiteDatabase? = null
    private val mContext: Context
    private var mNeedUpdate = false

    init {
        if (Build.VERSION.SDK_INT >= 17) DB_PATH =
            context.applicationInfo.dataDir + "/databases/" else DB_PATH =
            "/data/data/" + context.packageName + "/databases/"
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
            SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY)
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
        private const val CHARACTERS_COLUMN_ID = "c_id"
        private const val COLUMN_CHARACTER_NAME = "characters_names"
        private const val CHARACTERS_COLUMN_AM_I_HAVE = false
        private const val CHARACTERS_COLUMN_AM_I_FOLLOW = false
        private const val WEAPONS_TABLE_NAME = "weapons"
        private const val WEAPONS_COLUMN_ID = "w_id"
        private const val WEAPONS_COLUMN_NAME = "weapons_names"
        private const val WEAPONS_COLUMN_AM_I_HAVE = false
        private const val WEAPONS_COLUMN_AM_I_FOLLOW = false
        private const val BOOKS_TABLE_NAME = "books"
        private const val BOOKS_COLUMN_ID = "b_id"
        private const val BOOKS_COLUMN_NAME = "books_names"
        private const val BOOKS_COLUMN_AM_I_FOLLOW = false

    }


}