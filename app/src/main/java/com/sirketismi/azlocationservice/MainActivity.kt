package com.sirketismi.azlocationservice

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.sirketismi.azlocationservice.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    val locationClient : GoogleLocationClient by lazy {
        GoogleLocationClient(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(binding.root, "Hassas konum bilgine iznine ihtiyacım var", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver") {
                requestPermission()
            }.show()
        } else if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Snackbar.make(binding.root, "Genel bi konum bilgine iznine ihtiyacım var", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver") {
                requestPermission()
            }.show()
        }  else {
            requestPermission()
        }

        Intent(applicationContext, LocationService::class.java).apply {
            startService(this)
        }

        /* locationClient.getLocationUpdates(100)
            .catch {  }
            .onEach {
                println("lat ${it.latitude} lat ${it.longitude}")
            }.launchIn(lifecycleScope)*/
    }

    fun requestPermission() {
        locationPermissionReceiver.launch(arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS))
    }
    private val locationPermissionReceiver = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if(it.values.any { isGranted-> isGranted }) {
           // requestUserLocation()
        }
    }
}