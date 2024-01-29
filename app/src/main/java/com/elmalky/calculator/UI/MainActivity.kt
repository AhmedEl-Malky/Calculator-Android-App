package com.elmalky.calculator.UI

import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.elmalky.calculator.Util.evaluateExpression
import com.elmalky.calculator.Util.infixToPostfix
import com.elmalky.calculator.Util.isOperator
import com.elmalky.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var ans = ""
    lateinit var binder: ActivityMainBinding
    override fun onDestroy() {
        super.onDestroy()
        Log.i("Destroy", "destroy")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("create", "cre")
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        binder.switchTheme.isChecked =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        binder.switchTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            changeTheme(isChecked)
        }
        binder.equalBtn.setOnClickListener {
            var expression: String? = binder.inputExpression.text.toString()
            var postfixExpression: String? = infixToPostfix(expression)
            var result = String.format("%.3f", evaluateExpression(postfixExpression))
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                binder.outputResult.setTextColor(ContextCompat.getColor(this, R.color.white))
            else
                binder.outputResult.setTextColor(ContextCompat.getColor(this, R.color.black))
            while (result.last() == '0')
                result = result.dropLast(1)
            if (result.last() == '.')
                result = result.dropLast(1)
            binder.outputResult.text = result
            ans = result

        }
        binder.ansBtn.setOnClickListener {
            binder.inputExpression.text = ans
        }
        binder.apply {
            CBtn.setOnClickListener {
                inputExpression.text = ""
                outputResult.text = "0"
            }
            delBtn.setOnClickListener {
                inputExpression.text = inputExpression.text.dropLast(1)
            }
        }
    }

    private fun changeTheme(checked: Boolean) {
        if (checked)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun operandClick(v: View) {
        binder.inputExpression.append((v as Button).text)
        if ((v as Button).text != ".") {
            var expression: String? = binder.inputExpression.text.toString()
            var postfixExpression: String? = infixToPostfix(expression)
            var result = String.format("%.3f", evaluateExpression(postfixExpression))

            while (result.last() == '0')
                result = result.dropLast(1)
            if (result.last() == '.')
                result = result.dropLast(1)
            binder.outputResult.text = result
        }
    }

    fun operatorClick(v: View) {
        if (binder.inputExpression.text.isNotEmpty()) {
            if (isOperator(binder.inputExpression.text.last()))
                binder.inputExpression.text = binder.inputExpression.text.dropLast(1)
            binder.inputExpression.append((v as Button).text)
        }
    }

    override fun recreate() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in)
    }

}