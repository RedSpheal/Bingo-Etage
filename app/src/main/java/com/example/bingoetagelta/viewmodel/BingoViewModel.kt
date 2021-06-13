package com.example.bingoetagelta.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class BingoViewModel @Inject constructor(
    //savedStateHandle: SavedStateHandle,
    private var repository: DataRepository
    ): ViewModel()
{
    val currentDate: MutableLiveData<Calendar> = MutableLiveData<Calendar>(setCalendarTime(Calendar.getInstance()))

    var checkedArrayInput: BooleanArray? = null
    var editingBoolInput: Boolean = false

    fun changeCurrentDate(year: Int, month: Int, dayOfMonth: Int)
    {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        setCalendarTime(cal)
        currentDate.value = cal
    }

    private fun setCalendarTime(cal: Calendar): Calendar
    {
        cal.set(Calendar.HOUR_OF_DAY, 12)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal
    }

    fun getBingoGridFromCurrentDate(): IntArray
    {
        return generateBingoGridFromCurrentDate()
    }

    private fun generateBingoGridFromCurrentDate(): IntArray
    {
        fun getSeed(): Int
        {
            val nonNullDay = currentDate.value ?: Calendar.getInstance()
            // Set to 12:0:0.000
            setCalendarTime(nonNullDay)
            // Return hashcode
            var nameHashCode: Int = 0
            viewModelScope.launch { nameHashCode = repository.getUsername().hashCode() }
            return nonNullDay.hashCode() xor nameHashCode
        }

        checkedArrayInput = null
        editingBoolInput = true

        val arrayShuffled = intArrayOf(11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
        arrayShuffled.shuffle(Random(getSeed()))

        return arrayShuffled
    }

    fun calculateBingoCount(checkedStateArray: Array<Boolean>): Int
    {
        fun loop2DArrayAndSum(array: Array<IntArray>, value: Int): Int
        {
            var result = 0
            for (line in array)
            {
                var lineChecked = true
                for (caseNum in line)
                {
                    if (!checkedStateArray[caseNum]) lineChecked = false
                }
                if (lineChecked) result+=value
            }
            return result
        }

        val caseValue = repository.caseValue
        val lineValue = repository.lineValue
        val columnValue = repository.columnValue
        val diagValue = repository.diagValue
        val bonusValue = repository.bonusValue

        val line2DArray = repository.line2DArray
        val column2DArray = repository.column2DArray
        val diag2DArray = repository.diag2DArray
        val bonusArray = repository.bonusArray

        var result = 0

        for (buttonState in checkedStateArray)
        {
            if (buttonState) result+=caseValue
        }

        // line check
        result += loop2DArrayAndSum(line2DArray,lineValue)

        // column check
        result += loop2DArrayAndSum(column2DArray,columnValue)

        // diag check
        result += loop2DArrayAndSum(diag2DArray,diagValue)

        // bonus check
        for (caseNum in bonusArray)
        {
            if (checkedStateArray[caseNum]) result += bonusValue
        }

        return result
    }
}