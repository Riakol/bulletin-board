package com.riakol.bulletinboard

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.riakol.bulletinboard.databinding.ActivityMainBinding
import com.riakol.bulletinboard.dialogHelper.DialogConst
import com.riakol.bulletinboard.dialogHelper.DialogHelper


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("View binding is not initialized")
    private val dialogHelper = DialogHelper(this)
    val mAuth = FirebaseAuth.getInstance()
    private lateinit var tvAccount : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
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
            tvAccount = navView.getHeaderView(0).findViewById(R.id.tvAccountEmail)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
         when(item.itemId) {
            R.id.id_ads -> Toast.makeText(this, "Ads clicked", Toast.LENGTH_SHORT).show()
            R.id.id_car -> "Car clicked"
            R.id.id_pc -> "PC clicked"
            R.id.id_smartphone -> "Smartphone clicked"
            R.id.id_sing_up -> {
                dialogHelper.createSignDialog(DialogConst.SIGN_UP_STATE)
            }
             R.id.id_sing_in -> {
                 dialogHelper.createSignDialog(DialogConst.SIGN_IN_STATE)
             }
             R.id.id_sing_out -> {
                 uiUpdate(null)
                 mAuth.signOut()
             }

        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun uiUpdate(user: FirebaseUser?) {
        tvAccount.text = if (user == null) resources.getString(R.string.not_reg) else user.email
    }
}