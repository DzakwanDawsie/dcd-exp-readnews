package com.zackone.readnews.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.zackone.readnews.R
import com.zackone.readnews.databinding.ActivityMainBinding
import com.zackone.readnews.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onStart() {
        super.onStart()
        registerBroadCastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setupNavigation()
        setupFragment()

        val defaultItemId = intent.getIntExtra("menu", R.id.navigation_home)
        binding.navigation.selectedItemId = defaultItemId
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment())
            .commit()
    }

    private fun setupNavigation() {
        binding.navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment())
                        .commit()
                    true
                }

                R.id.navigation_favorite -> {
                    loadFavoriteFragment()
                }

                else -> false
            }
        }
    }

    private fun loadFavoriteFragment(): Boolean {
        try {
            val fragment = Class.forName("com.zackone.readnews.favorite.ui.FavoriteFragment")
                .getDeclaredConstructor()
                .newInstance() as Fragment

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()

            return true
        } catch (e: Exception) {
            Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show()
            Log.e("MainActivity", "loadFavoriteFragment: ", e)
            return false
        }
    }

    private fun registerBroadCastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    Intent.ACTION_POWER_CONNECTED -> {
                        binding.tvPowerStatus.text = getString(R.string.power_connected)
                        binding.tvPowerStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.power_connected))
                        binding.tvPowerStatus.visibility = View.VISIBLE
                    }
                    Intent.ACTION_POWER_DISCONNECTED -> {
                        binding.tvPowerStatus.text = getString(R.string.power_disconnected)
                        binding.tvPowerStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.power_disconnected))
                        binding.tvPowerStatus.visibility = View.VISIBLE
                    }
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(broadcastReceiver, intentFilter)
    }
}