package com.example.tvdiscovery

import android.content.Context
import android.content.Intent
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo

class MyNsdManager(private val context: Context) {

    private val SERVICE_TYPE = "_androidtv._tcp"
    private val nsdManager: NsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    private lateinit var discoveryListener: NsdManager.DiscoveryListener

    init {
        initializeDiscoveryListener()
    }

    fun discover() {
        nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }

    fun tearDown() {
        nsdManager.stopServiceDiscovery(discoveryListener)
    }

    private fun initializeDiscoveryListener() {
        discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(regType: String) {
                // Service discovery started, implement actions here if needed
                (context as MainActivity).showDiscoveryInProgressUI()
            }

            override fun onServiceFound(service: NsdServiceInfo) {
                // A service was found, implement actions here
                if (service.serviceType == SERVICE_TYPE) {
                    // Handle the discovered service info
                    val serviceName = service.serviceName
                    (context as MainActivity).updateDeviceList(serviceName)
                    // Show the "Discover Device" button
                    (context as MainActivity).showDeviceDiscoveredUI()

                    // Launch a new activity when a device is discovered
                    val intent = Intent(context, DeviceDetailActivity::class.java)
                    context.startActivity(intent)
                }
            }

            override fun onServiceLost(service: NsdServiceInfo) {
                // A service was lost, implement actions here if needed
            }

            override fun onDiscoveryStopped(serviceType: String) {
                // Service discovery stopped, implement actions here if needed
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                // Start discovery failed, handle error if needed
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                // Stop discovery failed, handle error if needed
            }
        }
    }
}
