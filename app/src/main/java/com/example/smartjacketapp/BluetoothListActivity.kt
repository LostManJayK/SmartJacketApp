package com.example.smartjacketapp

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.toast

class BluetoothListActivity: AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bt_list)
        val headerBar: Toolbar = findViewById(R.id.headerBar)
        setSupportActionBar(headerBar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)/* Remove toolbar default title */


        val btList: java.util.ArrayList<BluetoothDevice> = intent.getParcelableArrayListExtra("BTArray")

        val btRecyclerView = findViewById<RecyclerView>(R.id.rvPairedDevices)

        btRecyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = BluetoothListAdapter(btList, this)

        btRecyclerView?.adapter = adapter

    }

    open fun returnToMain(activityIntent: Intent)
    {
        startActivity(intent)
    }
}