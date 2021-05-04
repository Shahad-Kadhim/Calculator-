package com.example.week9

import android.content.pm.ActivityInfo
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import com.example.week9.databinding.ActivityMainBinding
import kotlin.math.*

class MainActivity : AppCompatActivity() {
     var operation :Operation?=null
    private lateinit var tempResult:String
     var oldValue : Double = 0.0
    var binary=false
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callback()
    }
    fun addNumber(v : View){
        val oldValue2=binding.equation.text.toString()
        binding.equation.text=oldValue2 + (v as Button).text.toString()
    }
    private fun calcMath(v :View){
        takeIf { operation !=null }?.let { binding.result.text=makeOperation() }
        binding.equation.text=""
        addNumber(v)
    }
    private fun calcMath2(v :View){
        takeIf { operation !=null }?.let { binding.equation.text=makeOperation() }
        saveOldValue((v as Button).text.toString())
    }
    private fun myPow(pow:Double){

        if (operation == null) {
            var s=if(binary) Integer.parseInt(binding.equation.text.toString(),2).toString() else binding.equation.text.toString()
            tempResult = Math.pow(s.toDouble(), pow).toString()
            Toast.makeText(this,tempResult.toDouble().toInt().toString(),Toast.LENGTH_LONG).show()
            if(binary) tempResult=Integer.toBinaryString(tempResult.toDouble().toInt()).toString()
            binding.result.text = tempResult
            binding.equation.text=tempResult
        }
        else if(binding.result.text.isNotEmpty())  {
            var s=if(binary) Integer.parseInt(binding.result.text.toString(),2).toString() else binding.result.text.toString()
            tempResult = Math.pow(s.toDouble(), pow).toString()
            if(binary) tempResult=Integer.toBinaryString(tempResult.toDouble().toInt()).toString()
            binding.result.text=tempResult
        }
        else invalidMessage()
    }
    private fun callback() {
        binding.log.setOnClickListener {
            calcMath(it)
            operation=Operation.Log
        }
        binding.ln.setOnClickListener {
            calcMath(it)
            operation=Operation.Ln
        }
        binding.sin.setOnClickListener {
            calcMath(it)
            operation=Operation.Sin
        }
        binding.cos.setOnClickListener {
            calcMath(it)
            operation=Operation.Cos
        }
        binding.tan.setOnClickListener {
            calcMath(it)
            operation=Operation.Tan
        }
        binding.tanH.setOnClickListener {
            calcMath(it)
            operation=Operation.TanH
        }
        binding.sinH.setOnClickListener {
            calcMath(it)
            operation=Operation.SinH
        }
        binding.cosH.setOnClickListener {
            calcMath(it)
            operation=Operation.CosH
        }
        binding.digitDelete.setOnClickListener {
            binding.equation.text.toString().takeIf { it.isNotEmpty() }?.let {
                if(!it.last().isDigit()) operation=null
                tempResult=it.substring(0,it.lastIndex)
                binding.equation.text=tempResult
            }
        }
        binding.pi.setOnClickListener {
            val oldValue=binding.equation.text.toString()
            binding.equation.text="${oldValue}3.14"
        }
        binding.clear.setOnClickListener { clear() }
        binding.mulMinus.setOnClickListener {
            if(operation==null){
                tempResult=(binding.equation.text.toString().toDouble()* -1.0).toString()
                binding.result.text=tempResult
            }
            else if(binding.result.text.isNotEmpty())  {
                tempResult=(binding.result.text.toString().toDouble()* -1.0).toString()
                binding.result.text=tempResult
            }
            else invalidMessage()

        }
        binding.percent.setOnClickListener {
            if (operation == null) {
                tempResult = (binding.equation.text.toString().toDouble() / 100.0).toString()
                binding.result.text = "$tempResult %"
            }
            else if(binding.result.text.isNotEmpty())  {
                tempResult = (binding.result.text.toString().toDouble() / 100.0).toString()
                binding.result.text=tempResult
            }else invalidMessage()
        }
        binding.square.setOnClickListener { myPow(2.0) }
        binding.cube.setOnClickListener { myPow(3.0) }
        binding.cubeRoot.setOnClickListener { myPow(1.0/3) }
        binding.squarRoot.setOnClickListener { myPow(0.5) }
        binding.plus.setOnClickListener {
            calcMath2(it)
            operation=Operation.Plus
        }
        binding.mul.setOnClickListener {
            calcMath2(it)
            operation=Operation.Mul
        }
        binding.sub.setOnClickListener {
            calcMath2(it)
            operation=Operation.Minus
        }
        binding.div.setOnClickListener {
           calcMath2(it)
            operation=Operation.Div
        }
        binding.equal.setOnClickListener {
            var temp =makeOperation()
            if(binary) temp =Integer.toBinaryString(temp.toDouble().toInt()).toString()
            binding.result.text =temp
        }
    }

    private fun  makeOperation() =
            when(operation) {
                Operation.Plus -> (oldValue + extractSecondValue('+')).toString()
                Operation.Minus -> (oldValue - extractSecondValue('-')).toString()
                Operation.Mul -> (oldValue * extractSecondValue('*')).toString()
                Operation.Div -> (oldValue / extractSecondValue('/')).toString()
                null -> binding.equation.text.toString()
                Operation.Sin ->  sin(Math.toRadians(extractNumber())).toString()
                Operation.Cos ->  cos(Math.toRadians(extractNumber())).toString()
                Operation.Tan ->  tan(Math.toRadians(extractNumber())).toString()
                Operation.SinH ->  sinh(Math.toRadians(extractNumber())).toString()
                Operation.CosH ->  cosh(Math.toRadians(extractNumber())).toString()
                Operation.TanH ->  tanh(Math.toRadians(extractNumber())).toString()
                Operation.Log -> log10(extractNumber()).toString()
                Operation.Ln -> ln1p(extractNumber()).toString()
            }
    private fun extractNumber():Double{
        return if(binary) Integer.parseInt(binding.equation.text.toString(), 2).toDouble()
        else binding.equation.text.toString().replace("[a-z]".toRegex(),"").toDouble()
    }
    private fun extractSecondValue(op :Char):Double{
        val equation=binding.equation.text.toString()
        val secondNumber=equation.substring(equation.indexOf(op)+1).trimStart()
        if(secondNumber[0]=='.') {
            invalidMessage()
            return 0.0
        }
       return secondNumber.toDouble()
    }
    private fun invalidMessage() {
        Toast.makeText(this, "invalid operation", Toast.LENGTH_SHORT).show()
    }
    private fun saveOldValue(op:String){
        if(binary) {
            var s=binding.equation.text.toString()
            binding.equation.text= "$s  $op  "
            oldValue= Integer.parseInt(s, 2).toDouble()
        } else  oldValue=binding.equation.text.toString().apply{ binding.equation.text= "$this  $op  " }.toDouble()
    }
    private fun clear(){
        binding.result.text=""
        binding.equation.text=""
        operation=null
    }
    fun binary(v :View){
        clear()
        binary=!binary
        binding.digitSeven.isEnabled=!binary
        binding.digitEight.isEnabled=!binary
        binding.digitNine.isEnabled=!binary
        binding.digitFour.isEnabled=!binary
        binding.digitFive.isEnabled=!binary
        binding.digitSix.isEnabled=!binary
        binding.digitThree.isEnabled=!binary
        binding.digitTwo.isEnabled=!binary
        binding.percent.isEnabled=!binary
        binding.pi.isEnabled=!binary
        binding.sin.isEnabled=!binary
        binding.cos.isEnabled=!binary
        binding.sinH.isEnabled=!binary
        binding.cosH.isEnabled=!binary
        binding.tan.isEnabled=!binary
        binding.tanH.isEnabled=!binary
        binding.log.isEnabled=!binary
        binding.ln.isEnabled=!binary
        binding.digitDot.isEnabled=!binary
    }
}