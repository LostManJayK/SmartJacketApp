package com.example.smartjacketapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.find

//This class contains the properties for the adapter needed to adapt the Exercises within a workout, into a recycleview

class ExerciseAdapter(val exerciseList: ArrayList<ExerciseEntryInfo>): RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.exercise_entry_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    override fun onBindViewHolder(holder: ExerciseAdapter.ViewHolder, position: Int)
    {
        val exercise: ExerciseEntryInfo = exerciseList[position]

        holder?.textViewName.text = exercise.name
        holder?.textViewComponent.text = exercise.components
        holder?.textViewUnits.text = exercise.units
        holder?.textViewValue.text = exercise.value

    }

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)
    {
        val textViewName = itemView.findViewById(R.id.tvExerciseName) as TextView
        val textViewComponent = itemView.findViewById(R.id.tvExerciseComponent) as TextView
        val textViewValue = itemView.findViewById(R.id.tvComponentValue) as TextView
        val textViewUnits = itemView.findViewById(R.id.tvComponentUnits) as TextView

    }
}