package com.example.smartjacketapp

import java.util.*
import kotlin.collections.ArrayList


//TODO This class specifies and handles the required info for a fitness entry

data class FitnessEntryInfo(val title: String, val date: Date, val exercises: ArrayList<ExerciseEntryInfo>)
