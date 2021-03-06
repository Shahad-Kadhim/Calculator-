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
import androidx.core.widget.addTextChangedListener
import com.example.week9.databinding.ActivityMainBinding
import kotlin.math.*
import kotlin.Exception as Exception1

class MainActivity : AppCompatActivity() {
     var operation :Operation?=null
     var scienceOperation :ScienceOperation?=null
     var power :Power?=null
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
        binding.result.text=if(binary&&operation!=null) Integer.toBinaryString(makeOperation().toDouble().toInt()).toString() else makeOperation()
    }
    private fun calcMath(v :View){
        val oldValue2=binding.equation.text.toString()
        binding.equation.text=oldValue2 + (v as Button).text.toString()+"("
    }
    private fun calcMath2(v :View){
        if(scienceOperation!=null) {
            binding.equation.text=binding.equation.text.toString()+")"
            scienceOperation=null
        }
        if (operation==null&&power==null){ saveOldValue((v as Button).text.toString()) }
        else{
            val lastResult=binding.result.text.toString()
            oldValue=if(binary) Integer.parseInt(lastResult,2).toDouble() else lastResult.toDouble()
            binding.equation.text=binding.equation.text.toString()+(v as Button).text.toString()
        }
        power=null
    }
    private fun myPow(pow:Double){
        val powString=when (pow){2.0->"??" 3.0->"??" 1.0/3->"???" 0.5->"??" else ->"" }
        if (operation == null&&binding.equation.text.toString().isNotEmpty()) {
            var s=if(binary) Integer.parseInt(binding.equation.text.toString(),2).toString() else binding.equation.text.toString()
            tempResult = Math.pow(s.toDouble(), pow).toString()
            binding.result.text=if(binary) Integer.toBinaryString(tempResult.toDouble().toInt()).toString() else tempResult
            binding.equation.text="(${binding.equation.text})$powString"
        }
        else if(operation!=null) {
            val tempOp=power
            power=null
            val l=binding.equation.text.toString().replace("\\d".toRegex(),"").last()
            var s= extractSecondValue(l).toString().apply { Log.i("MM",this) }
            tempResult = Math.pow(s.toDouble(), pow).toString()
            power=tempOp
            binding.result.text=if(binary) Integer.toBinaryString(makeOperation().toDouble().toInt()).toString() else makeOperation()
            binding.equation.text=binding.equation.text.toString().substringBeforeLast(l)+l+"(${if(binary)Integer.toBinaryString(s.toDouble().toInt())else s})$powString"
        }
        else invalidMessage()
    }
    private fun callback() {
        binding.log.setOnClickListener {
            calcMath(it)
            scienceOperation=ScienceOperation.Log
        }
        binding.ln.setOnClickListener {
            calcMath(it)
            scienceOperation=ScienceOperation.Ln
        }
        binding.sin.setOnClickListener {
            calcMath(it)
            scienceOperation=ScienceOperation.Sin
        }
        binding.cos.setOnClickListener {
            calcMath(it)
            scienceOperation=ScienceOperation.Cos
        }
        binding.tan.setOnClickListener {
            calcMath(it)
            scienceOperation=ScienceOperation.Tan
        }
        binding.tanH.setOnClickListener {
            calcMath(it)
            scienceOperation=ScienceOperation.TanH
        }
        binding.sinH.setOnClickListener {
            calcMath(it)
            scienceOperation=ScienceOperation.SinH
        }
        binding.cosH.setOnClickListener {
            calcMath(it)
            scienceOperation= ScienceOperation.CosH
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
            if (operation == null&& binding.equation.text.isNotEmpty()) {
                tempResult = (binding.equation.text.toString().toDouble() / 100.0).toString()
                binding.result.text=tempResult
                binding.equation.text="${binding.equation.text}%"
                power=Power.Cube //any thing
            }
            else if(operation!=null)  {
                val l=binding.equation.text.toString().replace("\\d".toRegex(),"").last()
                var s= extractSecondValue(l).toString()
                tempResult = (s.toDouble() / 100.0).toString().apply { Log.i("MM",this) }
                Log.i("MM",oldValue.toString())
                binding.result.text= makeOperation()
                binding.equation.text=binding.equation.text.toString().substringBeforeLast(l)+l+"$s%"
                power=Power.Cube //any thing
            }else invalidMessage()
        }
        binding.square.setOnClickListener {
            power=Power.Square
            myPow(2.0)
        }
        binding.cube.setOnClickListener {
            power=Power.Cube
            myPow(3.0) }
        binding.cubeRoot.setOnClickListener {
            power=Power.CubeRoot
            myPow(1.0/3)
        }
        binding.squarRoot.setOnClickListener {
            power=Power.SquareRoot
            myPow(0.5) }
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
            binding.equation.text=binding.result.text.toString().apply { clear() }
        }
    }
    private fun  makeOperation() =
            when(operation) {
                Operation.Plus -> (oldValue + extractSecondValue('+').apply { Log.i("MM",this.toString()) }).toString()
                Operation.Minus -> (oldValue - extractSecondValue('-')).toString()
                Operation.Mul -> (oldValue * extractSecondValue('*')).toString()
                Operation.Div -> (oldValue / extractSecondValue('/')).toString()
                null -> ""
            }
    private fun trigonometric(number:Double)=
            when(scienceOperation){
                ScienceOperation.Sin -> sin(Math.toRadians(number))
                ScienceOperation.Cos -> cos(Math.toRadians(number))
                ScienceOperation.Tan -> tan(Math.toRadians(number))
                ScienceOperation.SinH -> sinh(Math.toRadians(number))
                ScienceOperation.CosH -> cosh(Math.toRadians(number))
                ScienceOperation.TanH -> tanh(Math.toRadians(number))
                ScienceOperation.Log -> log10(number)
                ScienceOperation.Ln -> ln1p(number)
                null -> 0.0
        }
    private fun extractSecondValue(op :Char):Double{
        val secondNumber :String
        val equation=binding.equation.text.toString()
        if(scienceOperation!=null) {
            secondNumber=equation.substring(equation.lastIndexOf("(")+1).trimStart()
            return trigonometric(secondNumber.toDouble())
        }
//        Log.i("MM",oldValue.toString())
        if(power!=null){
             return tempResult.apply {Log.i("MfM",this)  }.toDouble()
        }
        secondNumber=equation.substring(equation.lastIndexOf(op)+1).trimStart()
        if(secondNumber[0]=='.') {
            invalidMessage()
            return 0.0
        }
       return secondNumber.apply { if(binary) return Integer.parseInt(this,2).toDouble() }.toDouble()
    }
    private fun invalidMessage() {
        Toast.makeText(this, "invalid operation", Toast.LENGTH_SHORT).show()
    }
    private fun saveOldValue(op:String){
            if(binary) {
                var s=binding.equation.text.toString()
                binding.equation.text= "$s$op"
                oldValue= Integer.parseInt(s, 2).toDouble()
            } else  oldValue=binding.equation.text.toString().apply{ binding.equation.text= "$this$op" }.toDouble()
    }
    private fun clear(){
        binding.result.text=""
        binding.equation.text=""
        operation=null
        scienceOperation=null
        power=null
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