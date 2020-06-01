package com.example.smartjacketapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartjacketapp.LogEntries.Companion.workouts
//import com.example.smartjacketapp.LogEntries.Companion.workouts
import kotlinx.android.synthetic.main.activity_fitness_log.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast
import java.lang.IllegalStateException
import java.nio.channels.UnresolvedAddressException

class FitnessLog : AppCompatActivity()
{

    val entries = LogEntries()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitness_log)

        //Creating recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.rvFitnessEntries)

        recyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        //Retrieve new workout information
        try
        {
            val new_entry_title = intent.getStringExtra("title")
            val new_entry_date = intent.getStringExtra("date")
            workouts.add(ExampleFitnessEntry(new_entry_title, new_entry_date))
        }
        catch (e: IllegalStateException)
        {
            //Nothing found
        }



        val adapter = CustomAdapter(workouts)

        recyclerView?.adapter = adapter

        btnMenu.setOnClickListener()
        {
            val intent = Intent(this, MapActivity::class.java)

            startActivity(intent)
        }

        btnAddFitnessEntry.setOnClickListener ()
        {
            val intent = Intent(this, FitnessDetail::class.java) //CHANGE TO OPEN FITNESS ADDITION, ONLY FOR TESTING EXERCISE EDITING

            startActivity(intent)
        }
    }
}


//This class stores the entered workout information
class LogEntries()
{
    companion object
    {
        var workouts = ArrayList<ExampleFitnessEntry>()

    }
}
