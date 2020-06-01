package com.example.smartjacketapp

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class BluetoothListAdapter(val pairedDeviceList: ArrayList<BluetoothDevice>,activityContext: Context): RecyclerView.Adapter<BluetoothListAdapter.ViewHolder>()
{

    lateinit var device: BluetoothDevice

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothListAdapter.ViewHolder
    {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.bt_item, parent, false)
        v.setOnClickListener()
        {
            Log.i("NAME: ", device.name)
            val intent = Intent(v.context, MainActivity::class.java)
            intent.putExtra("device_address", device.address)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            v.context.startActivity(intent)

        }
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return pairedDeviceList.size
    }

    override fun onBindViewHolder(holder: BluetoothListAdapter.ViewHolder, position: Int)
    {
        device = pairedDeviceList[position]

        holder?.textViewDeviceName.text = device.name
        holder?.textViewDeviceAddress.text = device.address




    }


    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)
    {
        val textViewDeviceName:TextView = itemView.findViewById(R.id.tvBtEntry)
        val textViewDeviceAddress: TextView = itemView.findViewById(R.id.tvDeviceAddress)
        val linLayoutBtItem: LinearLayout = itemView.findViewById(R.id.linLayoutBtItem)
    }
}