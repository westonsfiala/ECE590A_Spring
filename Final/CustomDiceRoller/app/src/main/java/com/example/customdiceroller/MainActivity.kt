package com.example.customdiceroller

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.customdiceroller.ui.main.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view_pager.adapter = SectionsPagerAdapter(this, supportFragmentManager)
        tabs.setupWithViewPager(view_pager)

        toolbar.title = getString(R.string.app_name)

        setSupportActionBar(toolbar)
    }
}