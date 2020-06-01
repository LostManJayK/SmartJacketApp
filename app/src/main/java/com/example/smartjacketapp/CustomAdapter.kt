package com.example.smartjacketapp

//This class contains the properties for the adapter needed to adapt the Workouts into recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(val workoutList: ArrayList<ExampleFitnessEntry>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    lateinit var workout: ExampleFitnessEntry

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.fitness_entry_item, parent, false)

        //Access the workout detail when clicked
        v.setOnClickListener()
        {
            val type: String  = workout.type
            val date: String = workout.date
        }
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        workout = workoutList[position]

        holder?.textViewName.text = workout.type
        holder?.textViewAddress.text = workout.date
    }

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val textViewName = itemView.findViewById(R.id.tvEntry1) as TextView
        val textViewAddress = itemView.findViewById(R.id.tvEntry2) as TextView
    }


}