/*
    -When importing the Google Maps dependency, the console threw an error saying the allocated memory was exceeded. This was fixed by adding MultiDex support
     Originally the app was designed on Android API 18, making the app available on 95% of devices, however, implementing MultiDex support on this API was difficult
     To resolve this, the API was upgraded to Android API 21. This reduced the usability to 85% of devices, however for this number will increase steadily as more people change over their devices

    -In order to use the Google maps API, the project needed to be registered with the Google API developer console, so an API key could be generated and included with the project

    -
*/



package com.example.smartjacketapp

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartjacketapp.R.drawable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.Exception
import org.jetbrains.anko.toast
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.IllegalStateException
import java.lang.NullPointerException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity()
{

    //lateinit  var bt_available_devices: ArrayList<BluetoothDevice> //Create a list of all the connected bluetooth devices

    lateinit var bt_paired_devices: Set<BluetoothDevice>

    val REQUEST_BLUETOOTH_ENABLE = 1

    val bt_filter: IntentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND) //Filter for receiving bluetooth broadcasts

    lateinit var bt_paired_device_list: ArrayList<BluetoothDevice>

    //Companion object for variables to be accessed elsewhere
    companion object
    {
        //val EXTRA_ADDRESS: String = "Device_address"
        var lastProgress: Int = 0 //Variable for temp value power off
        var temp: Int = 25 //Temperature control variable
        var power_on: Boolean = false //Replacement variable for power toggle
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") //TODO: Find HC05 UUID
        var m_bluetooth_socket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        var m_isConnected: Boolean = false
        var m_address: String? = null
        var bt_adapt: BluetoothAdapter? = null//Use bluetooth adapter
        var bluetooth_input: InputStream? = null
        var bluetooth_output: OutputStream? = null
        val communicator = SendReceive() //Initialize the communication handler

    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        m_address = intent.getStringExtra("device_address")//Retrieve the selected devices address

        if (m_address != null)
        {
            Log.i("address", m_address)
            ConnectToDevice(this).execute()
        }





        bt_adapt = BluetoothAdapter.getDefaultAdapter()

        //Allow the user to enable  bluetooth and search for an appropriate device
        btnBluetoothConnect.setOnClickListener()
        {
            //TODO If button is clicked, ask to search for a new connection or disconnect bluetooth, or remind that there is no bluetooth support
            btSetup() //Run Bluetooth Setup

            if (bt_adapt != null)
            {
                btPrompt()
            }


        }

        sbTempControl.thumb = resources.getDrawable((drawable.grey_sun_thumb))//Set default temp control thumb
        sbTempControl.progress = 0 //Set initial SeekBar progress to 0

        val headerBar: Toolbar = findViewById(R.id.headerBar)
        setSupportActionBar(headerBar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)/* Remove toolbar default title */

        //Handle clicking the menu button
        btnMenu.setOnClickListener()
        {
            val intent = Intent(this, FitnessLog::class.java)

            startActivity(intent)
        }




        //If bluetooth is enabled, show the bluetooth enabled icon, otherwise show the default bluetooth icon
        try
        {
            if(bt_adapt!!.isEnabled)
            {
                btnBluetoothConnect.setBackgroundResource(R.drawable.bluetooth_enabled_ic)
            }
            else
            {
                btnBluetoothConnect.setBackgroundResource((R.drawable.bluetooth_ic))
            }
        }
        catch(e: Exception)
        {
            Toast.makeText(this, "Bluetooth not supported.", Toast.LENGTH_SHORT).show()
        }

        btnPower.setOnClickListener()
        {

           /* while(btnPower.isSelected)
            {
                btnPower.setBackgroundDrawable(getResources().getDrawable((R.drawable.power_grey)))
            }*/
            power_on = !(power_on)
            if (m_isConnected && bluetooth_output != null)
            {
                communicator.sendCommand()//Toggle power on button press
            }

            if (power_on)
            {
                btnPower.setBackgroundDrawable(resources.getDrawable((drawable.power_green)))
                sbTempControl.thumb = resources.getDrawable((drawable.sun_thumb))
            }
            else
            {
                btnPower.setBackgroundDrawable(resources.getDrawable((drawable.power_red)))
                lastProgress = sbTempControl.progress
                sbTempControl.thumb = resources.getDrawable((drawable.grey_sun_thumb))
            }
        }

        sbTempControl!!.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener
        {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) =
                if (power_on)
                {
                    when
                    {
                        rbSelectCelsius.isChecked -> {
                            temp = (25 + (sbTempControl.progress.toDouble() / 100 * 15)).toInt()
                            tvTemperatureDisplay.text = temp.toString() + " °C"
                        }
                        rbSelectFahrenheit.isChecked -> {
                            temp = (77 + (sbTempControl.progress.toDouble() / 100 * (104 - 77))).toInt()
                            tvTemperatureDisplay.text = temp.toString() + " °F"
                        }
                        else -> sbTempControl.progress = 0
                    }

                }
                else
                {
                    sbTempControl.progress = lastProgress
                    sbTempControl.thumb = resources.getDrawable((drawable.grey_sun_thumb))
                }
        })


    }


    //Ask user if they wish to view devices or disconnect bluetooth
    private fun btPrompt()
    {
        //Create intent for switching to bluetooth device list
        val bta_intent = Intent(this, BluetoothListActivity::class.java)

        bta_intent.putParcelableArrayListExtra("BTArray", bt_paired_device_list)


        //Ask the user if the wish to view devices
        val buildBtEntryDialog = AlertDialog.Builder(this)



        buildBtEntryDialog.setMessage(null)

            .setCancelable(true)

            .setPositiveButton("View Devices", DialogInterface.OnClickListener
            {
                    dialog, id -> startActivity(bta_intent) //TODO Don't Start unless there is confirmation
            })

            .setNegativeButton("Disconnect", DialogInterface.OnClickListener{
                    dialog, id -> disconnect()
            })

        val alert = buildBtEntryDialog.create()


        // set title for alert dialog box
        alert.setMessage("View paired devices or disconnect from jacket? (Tap outside this dialog to cancel.)")
        // show alert dialog
        alert.show()
    }
    private fun btSetup()
    {
        if(bt_adapt == null)
        {
            toast("Bluetooth not supported...")
            return
        }
        if (!bt_adapt!!.isEnabled)
        {
            val btIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(btIntent, REQUEST_BLUETOOTH_ENABLE)

        }
        pairedDeviceList()


    }


    //Retrieve a list of all paired devices
    private fun pairedDeviceList()
    {
        bt_paired_devices = bt_adapt!!.bondedDevices
        val list: ArrayList<BluetoothDevice> = ArrayList()

        if (!bt_paired_devices.isEmpty())
        {
            for (device in bt_paired_devices)
            {
                if("HC" in device.name)
                list.add(device)
                Log.i("device",""+device)
            }
        }
        else
        {
            toast("No paired devices found")
        }

        this.bt_paired_device_list = list

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_BLUETOOTH_ENABLE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if (bt_adapt!!.isEnabled)
                {
                    toast("Bluetooth Enabled.")
                    btnBluetoothConnect.setBackgroundResource(R.drawable.bluetooth_enabled_ic)
                }
                else
                {
                    toast("Bluetooth Disabled")
                    btnBluetoothConnect.setBackgroundResource(R.drawable.bluetooth_ic)
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED)
            {
                toast("Bluetooth cancelled")
            }
        }
    }



    //This function disconnects the bluetooth socket
    private fun disconnect()
    {
        if(m_bluetooth_socket != null)
        {
            try
            {
                m_bluetooth_socket!!.close()
                m_bluetooth_socket = null
                m_isConnected = false
            }
            catch(e: IOException)
            {
                e.printStackTrace()
            }
        }
    }


    //Class for handling device connection
    private class ConnectToDevice(c: Context): AsyncTask<Void, Void, String>()
    {

        private var connect_success: Boolean = true
        private val context: Context

        init
        {
            this.context = c
        }

        override fun onPreExecute()
        {
            super.onPreExecute()
            m_progress = ProgressDialog.show(this.context, "Connecting...", "We're connecting to your jacket, hang tight!")
        }

        override fun doInBackground(vararg params: Void?): String ?
        {

            try
            {
                if (m_bluetooth_socket == null || !m_isConnected)
                {
                    bt_adapt = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = bt_adapt!!.getRemoteDevice(m_address)

                    m_bluetooth_socket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    bt_adapt!!.cancelDiscovery()
                    m_bluetooth_socket!!.connect()
                }
            } catch (e: IOException)
            {
                connect_success = false
                e.printStackTrace()
            }
            return null
        }
        override fun onPostExecute(result: String?)
        {
            super.onPostExecute(result)
            if (!connect_success)
            {
                Log.i("data", "couldn't connect")
            }
            else
            {
                m_isConnected= true
                Log.i("success", "success")

            }
            m_progress.dismiss()
            if(m_isConnected)
            {
                Log.i("Connection", m_isConnected.toString())
                communicator.start()
            }

        }
    }

    class SendReceive: Thread()
    {

        var o_stream: OutputStream? = null
        var i_stream: InputStream? = null

        override public fun run()
        {
            comSetup()
            while(true)
            {
                handleBTData()
            }
        }

        fun comSetup()
        {
            if (m_bluetooth_socket != null) {
                try {
                    o_stream = m_bluetooth_socket!!.outputStream
                    i_stream = m_bluetooth_socket!!.inputStream
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                bluetooth_input = i_stream
                bluetooth_output = o_stream
            }
        }

        //This function sends commands to the arduino
        fun sendCommand()
        {
            //Create a byte array for data transfer and popullate it with the control data
            var command_byte_array: ByteArray = byteArrayOf()
            if (power_on)
            {
                command_byte_array = "1".toByteArray()
                Log.i("data_send", command_byte_array.toString())
            }
            else
            {
                command_byte_array = "0".toByteArray()
                Log.i("data_send", command_byte_array.toString())
            }

            bluetooth_output!!.write(command_byte_array)
        }

        fun handleBTData()
        {

            var buffer: ByteArray = ByteArray(1024)
            var bytes: Int



            try
            {
                bytes = bluetooth_input!!.read(buffer)
                Log.i("INPUT", bytes.toString())
            }
            catch(e: IOException)
            {
                e.printStackTrace()
            }


        }
    }

    override fun onDestroy()
    {
        super.onDestroy()

        //unregisterReceiver(bt_receiver)
    }


}

