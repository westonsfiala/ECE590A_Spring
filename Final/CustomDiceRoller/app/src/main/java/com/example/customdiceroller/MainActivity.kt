package com.example.customdiceroller

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import com.example.customdiceroller.ui.main.RollFragment
import com.example.customdiceroller.ui.main.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view_pager.adapter = SectionsPagerAdapter(this, supportFragmentManager)
        tabs.setupWithViewPager(view_pager)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.do_something -> {
                val text = "do something clicked"
                val duration = Toast.LENGTH_LONG
                Toast.makeText(this, text, duration).show()
                true
            }
            R.id.settings_item -> {
                val text = "settings clicked"
                val duration = Toast.LENGTH_LONG
                Toast.makeText(this, text, duration).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}