package com.elmalky.calculator.UI

import android.R
import android.os.Bundle
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)
        binder.switchTheme.isChecked =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        binder.apply {
            switchTheme.setOnCheckedChangeListener { buttonView, isChecked ->
                changeTheme(isChecked)
            }
            equalBtn.setOnClickListener {
                equalBtnClick()
            }
            CBtn.setOnClickListener {
                CBtnClick()
            }
            delBtn.setOnClickListener {
                delBtnClick()
            }
            ansBtn.setOnClickListener {
                ansBtnClick()
            }
        }
    }

    private fun delBtnClick() {
        binder.inputExpression.text = binder.inputExpression.text.dropLast(1)
        if (binder.inputExpression.text.isEmpty())
            binder.outputResult.text = "0"
        else {
            try {
                var expression: String? = binder.inputExpression.text.toString()
                var postfixExpression: String? = infixToPostfix(expression)
                var result = String.format("%.3f", evaluateExpression(postfixExpression))

                while (result.last() == '0')
                    result = result.dropLast(1)
                if (result.last() == '.')
                    result = result.dropLast(1)
                binder.outputResult.text = result
            } catch (e: Exception) {
            }
        }
    }

    private fun CBtnClick() {
        binder.inputExpression.text = ""
        binder.outputResult.text = "0"
    }

    private fun equalBtnClick() {
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

    private fun ansBtnClick() {
        binder.inputExpression.text = ans
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