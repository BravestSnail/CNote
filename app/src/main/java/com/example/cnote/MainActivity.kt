package com.example.cnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cnote.R.*
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_note.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(drawable.ic_menu)
        }

        cnoteNav.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener(){it ->
            when(it.itemId){
                id.iButton -> Snackbar.make(findViewById(id.cnoteCoordinator), "待开发，敬请期待", Snackbar.LENGTH_SHORT).show()
                id.setButton -> Snackbar.make(findViewById(id.cnoteDrawerlayout), "待开发，敬请期待", Snackbar.LENGTH_SHORT).show()
                id.weButton -> Snackbar.make(findViewById(id.cnoteDrawerlayout), "待开发，敬请期待", Snackbar.LENGTH_SHORT).show()

            }
            cnoteDrawerlayout.closeDrawers()
            true
        })
    }

    override fun onResume() {
        super.onResume()
        //在ViewPager里启动Fragment
        cnoteViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2

            override fun createFragment(position: Int): Fragment {
                return when(position){
                    0 -> NoteFragment()
                    else -> PasswordFragment()
                }
            }
        }
//        将ViewPager与TabLayout关联
        TabLayoutMediator(cnoteTabLayout, cnoteViewPager){tab, position ->
            when(position){
                0 -> tab.text = "便签"
                else -> tab.text = "密签"
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> cnoteDrawerlayout.openDrawer(GravityCompat.START)
        }
        return true
    }


}