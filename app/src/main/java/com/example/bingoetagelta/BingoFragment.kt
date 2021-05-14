package com.example.bingoetagelta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.preference.PreferenceManager
import java.util.*
import kotlin.random.Random

// the fragment initialization parameters keys
private const val NUMBER_ARRAY_SHUFFLED = "NUMBER_ARRAY_SHUFFLED"
private const val CHECKED_ARRAY = "CHECKED_ARRAY"
private const val EDITING_BOOL = "EDITING_BOOL"

/**
 * A simple [Fragment] subclass.
 * Use the [BingoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BingoFragment : Fragment(), View.OnClickListener {
    private val numberOfButton=10

    private var numberArrayShuffled: IntArray = IntArray(numberOfButton)
    private var checkedArray: BooleanArray = BooleanArray(numberOfButton)
    private var editingBool: Boolean = false

    private lateinit var buttonArray : Array<ToggleButton>
    private lateinit var textVBingoCount : TextView
    private lateinit var okButton : Button
    private lateinit var editButton: ImageButton

    private val caseValue = 1
    private val lineValue = 2
    private val columnValue = 2
    private val diagValue = 2
    private val bonusValue = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            numberArrayShuffled = it.getIntArray(NUMBER_ARRAY_SHUFFLED) ?: IntArray(numberOfButton)
            checkedArray = it.getBooleanArray(CHECKED_ARRAY) ?: BooleanArray(numberOfButton)
            editingBool = it.getBoolean(EDITING_BOOL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragView = inflater.inflate(R.layout.fragment_bingo, container, false)
        // Input initialization
        buttonArray = Array(numberOfButton){
                i -> fragView.findViewById(
            resources.getIdentifier(
                "button${i + 1}",
                "id",
                context?.packageName
            ))
        }
        for (button in buttonArray) button.setOnClickListener(this)

        okButton = fragView.findViewById(R.id.okButton)
        okButton.setOnClickListener(this)

        editButton = fragView.findViewById(R.id.editButton)
        editButton.setOnClickListener(this)

        textVBingoCount = fragView.findViewById(R.id.textViewBingoCount)

        // Initialize TextView and Buttons
        updateBingoGrid()
        setEditing(editingBool)

        // return the view
        return fragView
    }

    fun setBingoGrid(numberArrayShuffledInput: IntArray?,
                     checkedArrayInput: BooleanArray?,
                     editingBoolInput: Boolean) {
        numberArrayShuffled = numberArrayShuffledInput ?: IntArray(numberOfButton)
        checkedArray = checkedArrayInput ?: BooleanArray(numberOfButton)
        editingBool = editingBoolInput

        arguments = Bundle().apply {
            putIntArray(NUMBER_ARRAY_SHUFFLED, numberArrayShuffled)
            putBooleanArray(CHECKED_ARRAY, checkedArray)
            putBoolean(EDITING_BOOL, editingBool)
        }
    }

    fun updateBingoGrid(){
        fun updateText(button: ToggleButton, newText: String){
            button.text = newText
            button.textOff = newText
            button.textOn = newText
        }

        for ((index, button) in buttonArray.withIndex()) {
            updateText(button, numberArrayShuffled[index].toString())
            button.isChecked = checkedArray[index]
        }
        calculateBingoCount()
    }

    override fun onClick(v: View?) {
        if (v==null) return
        when(v.id){
            R.id.okButton -> setEditing(false)
            R.id.editButton -> setEditing(true)
            else -> calculateBingoCount()
        }
    }

    private fun calculateBingoCount(){

        val buttonStateArray = Array(numberOfButton) { i -> buttonArray[i].isChecked }

        var result = 0

        for (buttonState in buttonStateArray){
            if (buttonState) result+=caseValue
        }

        // line check
        for (i in 1..3){
            var lineChecked = true
            for (j in 1..3){
                if (!buttonStateArray[(i - 1) * 3 + j - 1]) lineChecked = false
            }
            if (lineChecked) result+=lineValue
        }

        // column check
        for (j in 1..3){
            var lineChecked = true
            for (i in 1..3){
                if (!buttonStateArray[(i - 1) * 3 + j - 1]) lineChecked = false
            }
            if (lineChecked) result+=columnValue
        }

        // diag check
        if (buttonStateArray[0] && buttonStateArray[4] && buttonStateArray[8]) result +=diagValue
        if (buttonStateArray[2] && buttonStateArray[4] && buttonStateArray[6]) result +=diagValue

        // bonus check
        if (buttonStateArray[9]) result += bonusValue

        textVBingoCount.text = resources.getString(R.string.text_bingo_count, result.toString())
    }

    private fun setEditing(edit: Boolean){
        editingBool = edit
        okButton.visibility = if (editingBool) Button.VISIBLE else Button.INVISIBLE
        editButton.visibility = if (!editingBool) Button.VISIBLE else Button.INVISIBLE
        for (button in buttonArray){
            button.isEnabled = editingBool
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param numberArrayShuffled Parameter 1 : the bingo grid for the selected day
         * @param checkedArray Parameter 2 : the checked (or not) values
         * @param editingBool Parameter 3 : the editing mode
         * @return A new instance of fragment BingoFragment.
         */
        @JvmStatic
        fun newInstance(numberArrayShuffled: IntArray?,
                        checkedArray: BooleanArray?,
                        editingBool: Boolean) =
            BingoFragment().apply {
                arguments = Bundle().apply {
                    putIntArray(NUMBER_ARRAY_SHUFFLED, numberArrayShuffled)
                    putBooleanArray(CHECKED_ARRAY, checkedArray)
                    putBoolean(EDITING_BOOL, editingBool)
                }
            }
    }
}