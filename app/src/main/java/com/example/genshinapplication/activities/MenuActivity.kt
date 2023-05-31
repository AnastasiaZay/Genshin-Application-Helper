package com.example.practice5

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.genshinapplication.CharactersActivity
import com.example.genshinapplication.ProfileActivity
import com.example.genshinapplication.activities.MainActivity
import com.example.genshinapplication.R
import com.example.genshinapplication.activities.CharactersFragment
import com.google.android.material.navigation.NavigationView
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.StorageReference

class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

//    private lateinit var fireStorage: StorageReference

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var user: HashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

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

        user = intent.getSerializableExtra("user") as HashMap<String, String>

        val manager = supportFragmentManager.beginTransaction()
        val homeFragment = MainActivity()

//        fireStorage = FirebaseStorage.getInstance().reference

//        val bundle = Bundle()
//            bundle.putString("id", user["id"])
//            bundle.putString("name", user["name"])
//            bundle.putString("surname", user["surname"])
//            bundle.putString("login", user["login"])
//            bundle.putString("birth", user["birth"])
//            bundle.putString("photoURI", user["photoURI"])

//        homeFragment.arguments = bundle

//        manager.replace(R.id.fragment_container, homeFragment.commit())

        toolbar.title = "@string/home"
        navigationView.setCheckedItem( R.id.mainActivity)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profileActivity -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileActivity()).commit()
                toolbar.title = "@string/profile"
            }
            R.id.charactersActivity -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CharactersFragment()).commit()
                toolbar.title = "@string/characters"
            }
            R.id.mainActivity -> {
                val homeFragment = MainActivity()
                val manager = supportFragmentManager.beginTransaction()
//
//                val bundle = Bundle()
//                    bundle.putString("id", user["id"])
//                    bundle.putString("name", user["name"])
//                    bundle.putString("surname", user["surname"])
//                    bundle.putString("login", user["login"])
//                    bundle.putString("birth", user["birth"])
//                    bundle.putString("photoURI", user["photoURI"])

//                homeFragment.arguments = bundle

//                manager.replace(R.id.fragment_container, homeFragment.commit())
                toolbar.title = "@string/profile"
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}