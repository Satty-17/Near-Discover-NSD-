package com.example.tvdiscovery

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var nsdManager: MyNsdManager
    private lateinit var deviceListView: ListView
    private lateinit var discoverButton: Button
    private val deviceList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nsdManager = MyNsdManager(this)

        deviceListView = findViewById(R.id.deviceListView)
        discoverButton = findViewById(R.id.discoverButton)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceList)
        deviceListView.adapter = adapter

        // Set a click listener for the "Discover Device" button
        discoverButton.setOnClickListener {
            // Launch a new activity when the button is clicked
            val intent = Intent(this, DeviceDetailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        nsdManager.discover()
        showDiscoveryInProgressUI()
    }

    override fun onDestroy() {
        nsdManager.tearDown()
        super.onDestroy()
    }

    fun updateDeviceList(deviceName: String) {
        runOnUiThread {
            deviceList.add(deviceName)
            (deviceListView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        }
    }

    fun showDiscoveryInProgressUI() {
        runOnUiThread {
            // Show the progress bar and "Discover Device" button
            findViewById<ProgressBar>(R.id.progressBar).visibility = VISIBLE
            findViewById<Button>(R.id.discoverButton).visibility = GONE
            findViewById<ListView>(R.id.deviceListView).visibility = GONE
        }
    }

    // Call this method when a device is discovered to show the "Discover Device" button
    fun showDeviceDiscoveredUI() {
        runOnUiThread {
            findViewById<ProgressBar>(R.id.progressBar).visibility = GONE
            findViewById<Button>(R.id.discoverButton).visibility = VISIBLE
            findViewById<ListView>(R.id.deviceListView).visibility = VISIBLE
        }
    }
}
