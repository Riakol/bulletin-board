package com.riakol.bulletinboard

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.riakol.bulletinboard.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("View binding is not initialized")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        with(binding) {
            val toolBar = mainContent.toolbar
            val toggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                toolBar,
                R.string.open,
                R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            navView.setNavigationItemSelectedListener(this@MainActivity)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
         when(item.itemId) {
            R.id.id_ads -> Toast.makeText(this, "Ads clicked", Toast.LENGTH_SHORT).show()
            R.id.id_car -> "Car clicked"
            R.id.id_pc -> "PC clicked"
            R.id.id_smartphone -> "Smartphone clicked"

        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}