package com.example.genshinapplication.activities


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.genshinapplication.ProfileActivity
import com.example.genshinapplication.R
import com.example.genshinapplication.WeaponFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar( toolbar )

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator( R.drawable.ic_nav )

        val manager = supportFragmentManager.beginTransaction()
        val homeFragment = HomeFragment()

        manager.replace(R.id.fragment_container, homeFragment).commit()

        toolbar.title = "Личный кабинет"
        navigationView.setCheckedItem( R.id.nav_mainActivity) //nav_home
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_mainActivity -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
                toolbar.title = "@string/home"
            }
            R.id.nav_profileFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileActivity()).commit()
                toolbar.title = "@string/profile"
            }
            R.id.nav_charactersFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CharactersFragment()).commit()
                toolbar.title = "@string/characters"
            }
            R.id.nav_weaponFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, WeaponFragment()).commit()
                toolbar.title = "@string/weapon"
            }
            R.id.nav_artifactsFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ArtifactsFragment()).commit()
                toolbar.title = "@string/artifacts"
            }
            R.id.nav_informationFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, InformationFragment()).commit()
                toolbar.title = "@string/info"
            }


        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}