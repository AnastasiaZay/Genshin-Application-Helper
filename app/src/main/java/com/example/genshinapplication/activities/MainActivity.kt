package com.example.genshinapplication.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.genshinapplication.R
import com.example.genshinapplication.helpers.AlarmReceiver
import com.example.genshinapplication.helpers.MyDatabaseHelper
import com.google.android.material.navigation.NavigationView
import java.io.IOException
import java.util.Calendar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private var mDBHelper: MyDatabaseHelper? = null
    private var mDb: SQLiteDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("СЕТАП НАТИФИКАТИОНС СССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССССС")

        drawerLayout = findViewById(R.id.drawer_layout)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_nav)

        val manager = supportFragmentManager.beginTransaction()
        val homeFragment = HomeFragment()

        manager.replace(R.id.fragment_container, homeFragment).commit()

        toolbar.title = "Личный кабинет"
        navigationView.setCheckedItem(R.id.nav_mainActivity) //nav_home


        mDBHelper = MyDatabaseHelper(this)

        try {
            mDBHelper!!.updateDataBase()
        } catch (mIOException: IOException) {
            throw java.lang.Error("UnableToUpdateDatabase")
        }

        mDb = try {
            mDBHelper!!.writableDatabase
        } catch (mSQLException: SQLException) {
            throw mSQLException
        }



        setUpNotification()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_mainActivity -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
                toolbar.title = getString(R.string.home)// Задать готовый текст - "@string/home"
            }

            R.id.nav_profileFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment()).commit()
                toolbar.title = getString(R.string.title_activity_menu) // задать через строки
            }

            R.id.nav_charactersFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CharactersFragment()).commit()
                toolbar.title = getString(R.string.characters)
            }

            R.id.nav_weaponFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, WeaponFragment()).commit()
                toolbar.title = getString(R.string.weapon)
            }

            R.id.nav_artifactsFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ArtifactsFragment()).commit()
                toolbar.title = getString(R.string.artifacts)
            }

            R.id.nav_informationFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, InformationFragment()).commit()
                toolbar.title = getString(R.string.info)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setUpNotification() {
        val intentAlarm = Intent(this, AlarmReceiver::class.java)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pi = PendingIntent.getBroadcast(
            this,
            123,
            intentAlarm,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val calendar = Calendar.getInstance()

        //Шлем уведы в 9 утра
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 9)
//        calendar.set(Calendar.HOUR_OF_DAY, 21)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis, AlarmManager.INTERVAL_DAY,
            pi
        )
    }
}