package com.example.tiptime

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Creates a binding so the different views can be accessed with ease
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calculateButton.setOnClickListener { calculateTip() }

        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode) }

    }

    private fun calculateTip() {
        //Get the decimal value from the cost service text field
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringInTextField.toDoubleOrNull()

        //Checks to see if nothing or 0 is inputted by the user and returns the tip as 0
        if (cost == null || cost == 0.0) {
            displayTip(0.0)
            return
        }

        //Using the tipOptions Radio Group, the correct percentage is selected to be used for
        // calculating later
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        /**
         * This final part calculates the total tip using the cost and the tip percentage based
         * on what the user entered.
         * Then it checks to see if the roundUpSwitch is checked and if it is, the tip amount is
         * rounded up and if it is not the tip is kept the same.
         */
        var tip = tipPercentage * cost
        if (binding.roundUpSwitch.isChecked) {
            tip = kotlin.math.ceil(tip)
        }

        //calling the displayTip method to convert the tip to a currency
        displayTip(tip)
    }

    /**
     * This method is used to convert the tip amount as a double to any currency based on the users
     * phone settings.
     *
     * @param tip - Tip amount
     */
    private fun displayTip(tip: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

    /**
     * Key listener: listens for when the enter key is pressed and when the event is detected,
     * the keyboard is hidden.
     */
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}