package com.example.smartjacketapp

import java.util.ArrayList

//Data class relating to a general fitness entry in a cardview

class ExampleFitnessEntry(val type: String, val date: String)
{
    var exercises = ArrayList<ExerciseEntryInfo>()
}
