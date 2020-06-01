package com.example.smartjacketapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_fitness_detail.*
import org.jetbrains.anko.toast
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList

class FitnessDetail : AppCompatActivity()
{


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitness_detail)

        val detailRecycler = findViewById<RecyclerView>(R.id.rvExerciseEntries)

        detailRecycler?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        val workoutTitleField = findViewById<EditText>(R.id.etWorkoutTitle)
        val workoutDateField = findViewById<EditText>(R.id.etWorkoutDate)

        try
        {
            val new_entry_name = intent.getStringExtra("name")
            val new_entry_unit = intent.getStringExtra("units")
            val  new_entry_value = intent.getStringExtra("value")
            val new_entry_component = intent.getStringExtra("component")
            ExerciseEntries.exercises.add(ExerciseEntryInfo(new_entry_name, new_entry_component, new_entry_unit, new_entry_value))
        }
        catch (e: IllegalStateException)
        {
            //Nothing found
        }


        val adapter  = ExerciseAdapter(ExerciseEntries.exercises)

        detailRecycler?.adapter = adapter

        //Add a new exercise to the workout
        btnAddNewExercise.setOnClickListener()
        {
            val intent  = Intent(this, ExerciseDetail::class.java)
            startActivity(intent)
        }

        //Confirm the entered information for the workout
        btnConfirmFitnessEntry.setOnClickListener()
        {
            val workout_title: String = workoutTitleField.text.toString()
            val workout_date: String = workoutDateField.text.toString()

            //Ask the user if the wish to confirm the workout entry
            val buildWorkoutConfirmDialog = AlertDialog.Builder(this)



            buildWorkoutConfirmDialog.setMessage(null)

                .setCancelable(true)

                .setPositiveButton("Confirm", DialogInterface.OnClickListener
                {

                        dialog, id ->
                            if(workout_title == "" && workout_date == "")
                            {
                                toast("No information entered")
                            }
                            else
                            {
                                sendWorkout(workout_title, workout_date)
                            }
                            finish()

                })

                .setNegativeButton("Cancel", DialogInterface.OnClickListener{
                        dialog, id -> dialog.cancel()
                })

            val alert = buildWorkoutConfirmDialog.create()


            // set title for alert dialog box
            alert.setMessage("Confirm entered workout?")
            // show alert dialog
            alert.show()

        }


    }

    fun sendWorkout(title: String, date: String)
    {
        val fitnessIntent = Intent(this, FitnessLog::class.java)
        fitnessIntent.putExtra("title", title)
        fitnessIntent.putExtra("date", date)
        startActivity(fitnessIntent) //TODO Don't Start unless there is confirmation
    }


}

class ExerciseEntries()
{
    companion object
    {
        var exercises = ArrayList<ExerciseEntryInfo>()
    }
}