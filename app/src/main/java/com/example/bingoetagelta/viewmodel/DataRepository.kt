package com.example.bingoetagelta.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.bingoetagelta.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(@ApplicationContext val context: Context)
{
    private val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    suspend fun getUsername() = pref.getString("username", "")

    val caseValue = context.resources.getInteger(R.integer.caseValue)
    val lineValue = context.resources.getInteger(R.integer.lineValue)
    val columnValue = context.resources.getInteger(R.integer.columnValue)
    val diagValue = context.resources.getInteger(R.integer.diagValue)
    val bonusValue = context.resources.getInteger(R.integer.bonusValue)

    val line2DArray = arrayOf(
        context.resources.getIntArray(R.array.lineArray_0),
        context.resources.getIntArray(R.array.lineArray_1),
        context.resources.getIntArray(R.array.lineArray_2),
    )
    val column2DArray = arrayOf(
        context.resources.getIntArray(R.array.columnArray_0),
        context.resources.getIntArray(R.array.columnArray_1),
        context.resources.getIntArray(R.array.columnArray_2),
    )
    val diag2DArray = arrayOf(
        context.resources.getIntArray(R.array.diagArray_0),
        context.resources.getIntArray(R.array.diagArray_1),
    )
    val bonusArray = context.resources.getIntArray(R.array.bonusArray)

}