package com.example.smartjacketapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.toolbar.*

class MapActivity: AppCompatActivity()
{

    private lateinit var mMap: SupportMapFragment //Declare map fragment
    lateinit var googleMap: GoogleMap //Declare Google map

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        btnMenu.setOnClickListener()
        {
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }

        mMap = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment //Initialize map fragment variable
        //Retrieve map, set location and zoom
        mMap.getMapAsync(OnMapReadyCallback {

            googleMap = it

            val location1 = LatLng(13.03, 77.60) //Lat long variable
            googleMap.addMarker(MarkerOptions().position(location1).title("Test Location"))//Add marker at set lat long
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location1, 10f))
        })
    }





}