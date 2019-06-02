package com.example.customdiceroller

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.customdiceroller.ui.main.PageViewModel
import com.example.customdiceroller.ui.main.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view_pager.adapter = SectionsPagerAdapter(this, supportFragmentManager)
        tabs.setupWithViewPager(view_pager)
        setSupportActionBar(settingsToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.clearHistory -> {
                ViewModelProviders.of(this).get(PageViewModel::class.java).clearHistory()
                val text = "History Cleared"
                val duration = Toast.LENGTH_SHORT
                Toast.makeText(this, text, duration).show()
                true
            }
            R.id.settings_item -> {

                val settingsIntent = Intent(this, Settings::class.java)
                startActivity(settingsIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun isShakeToRoll() : Boolean
    {
        return getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            .getBoolean(getString(R.string.shake_preference_key),false)
    }

    fun shakeSensitivity() : Float
    {
        return getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            .getInt(getString(R.string.shake_sensitivity_key),4).toFloat() + 1.0f
    }


}
