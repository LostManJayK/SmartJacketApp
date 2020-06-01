package com.example.smartjacketapp

/*This class will handle the functions of the activity used for adding details of an exercise to a certain exercise entry
Functions include:

Adding new entries
Allowing the user to select a name for the exercise component
Adding a value (String) for the activity
Confirmation and sending of the information to the appropriate exercise entry cardview
Selecting whether or not the detail will be visible in the cardview
 */

import android.os.Bundle
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_exercise_detail.*
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.component_entry_popup.*


class ExerciseDetail: AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_exercise_detail)





        btnAddComponent.setOnClickListener()
        {


            val buildComponentEntryDialog = AlertDialog.Builder(this)

            val exercise_component_entry: View = layoutInflater.inflate(R.layout.component_entry_popup, null)
            // set title for alert dialog box


            val exerciseNameTextField = findViewById<EditText>(R.id.etExerciseName)
            val exerciseComponentTextField = exercise_component_entry.findViewById<EditText>(R.id.etComponentName)
            val exerciseUnitTextField =  exercise_component_entry.findViewById<EditText>(R.id.etComponentUnit)
            val exerciseValueTextField =  exercise_component_entry.findViewById<EditText>(R.id.etComponentValue)



            buildComponentEntryDialog.setMessage(null)

                .setCancelable(true)

                .setPositiveButton("Confirm", DialogInterface.OnClickListener {
                        dialog, id ->
                    val exercise_name = exerciseNameTextField.text.toString()
                    val exercise_component = exerciseComponentTextField.text.toString()
                    val exercise_value = exerciseValueTextField.text.toString()
                    val exercise_units = exerciseUnitTextField.text.toString()

                    sendExercise(exercise_name, exercise_component, exercise_value, exercise_units)
                    finish()
                })

                .setNegativeButton("Cancel", DialogInterface.OnClickListener{
                        dialog, id -> dialog.cancel()
                })

            val alert = buildComponentEntryDialog.create()

            alert.setTitle("New Component")

            alert.setView(exercise_component_entry)
            // show alert dialog
            alert.show()
        }
    }

    fun sendExercise(name: String, component: String, value: String, units: String)
    {
        val exerciseIntent = Intent(this, FitnessDetail::class.java)

        exerciseIntent.putExtra("name", name)
        exerciseIntent.putExtra("component", component)
        exerciseIntent.putExtra("value", value)
        exerciseIntent.putExtra("units", units)

        startActivity(exerciseIntent) //TODO Don't Start unless there is confirmation

    }


}

