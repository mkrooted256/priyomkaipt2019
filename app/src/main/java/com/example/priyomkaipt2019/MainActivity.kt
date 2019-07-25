package com.example.priyomkaipt2019

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val crScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    lateinit var fab: FloatingActionButton
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        fab = findViewById(R.id.fab)
        fab.setOnClickListener { replaceFragment(NewRecordFragment()) }
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        navView.itemIconTintList = null;

        val prefs = getSharedPreferences(LoginFragment.TAG, 0)
        val opname = prefs.getString(LoginFragment.PREF_OPNAME, null)
        if (opname == null){
            toast("Необхідний вхід")
            fab.hide()
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            replaceFragment(LoginFragment.newInstance(""))
        } else {
            toast("Ви увійшли як $opname")
            navView.getHeaderView(0).findViewById<TextView>(R.id.nav_header_op_name).text = opname
            replaceFragment(RecordsFragment())
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun toast(str: String, long: Boolean = false) {
        Toast.makeText(this, str, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }

    fun login(name: String, token: String) {
        fab.show()
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        getSharedPreferences(LoginFragment.TAG, 0)
            ?.edit()
            ?.putString(LoginFragment.PREF_OPNAME, name)
            ?.putString(LoginFragment.PREF_TOKEN, token)
            ?.apply()
        supportFragmentManager.popBackStackImmediate()
        replaceFragment(RecordsFragment())
        navView.getHeaderView(0).findViewById<TextView>(R.id.nav_header_op_name).text = name
    }

    private fun logout() {
        fab.hide()
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        val prefs = getSharedPreferences(LoginFragment.TAG, 0)
        val opname = prefs.getString(LoginFragment.PREF_OPNAME, "") ?: ""

        supportFragmentManager.popBackStackImmediate()
        replaceFragment(LoginFragment.newInstance(opname))

        prefs.edit().clear().apply()
    }

    fun replaceFragment(newFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment, newFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun dial(numberRes: Int) {
        startActivity(
            Intent(Intent.ACTION_DIAL, Uri.parse(getString(numberRes)))
        )
    }

    private fun chat(usernameRes: Int) {
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("http://t.me/${getString(usernameRes)}"))
        )
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_all_records -> {
                replaceFragment(RecordsFragment())
            }
            R.id.nav_search -> {

            }
            R.id.nav_new_record -> {
                replaceFragment(NewRecordFragment())
            }
            R.id.nav_contact_iren -> {
                chat(R.string.iren_tg)
            }
            R.id.nav_call_iren -> {
                toast("Не треба.")
                // dial(R.string.iren_phone)
            }
            R.id.nav_contact_mkrooted -> {
                chat(R.string.mkrooted_tg)
            }
            R.id.nav_call_mkrooted -> {
                dial(R.string.mkrooted_phone)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
