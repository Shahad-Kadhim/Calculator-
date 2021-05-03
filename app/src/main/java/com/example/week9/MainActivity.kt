package com.example.week9

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.week9.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
     var operation :Operation?=null
    private lateinit var tempResult:String
     var oldValue : Double = 0.0
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callback()
    }
    fun addNumber(v : View){
        val oldValue=binding.equation.text.toString()
        binding.equation.text=oldValue + (v as Button).text.toString()
    }
    private fun callback() {
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
        binding.clear.setOnClickListener {
            binding.result.text=""
            binding.equation.text=""
            operation=null
        }
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
        binding.square.setOnClickListener {
            if (operation == null) {
                tempResult = Math.pow(binding.equation.text.toString().toDouble(), 2.0).toString()
                binding.result.text = tempResult
                binding.equation.text=tempResult
            }
            else if(binding.result.text.isNotEmpty())  {
                tempResult = Math.pow(binding.result.text.toString().toDouble(), 2.0).toString()
                binding.result.text=tempResult
            }
            else invalidMessage()
        }
        binding.plus.setOnClickListener {
            takeIf { operation !=null }?.let {
                binding.equation.text=makeOperation()
            }
            operation=Operation.Plus
            saveOldValue((it as Button).text.toString())

        }
        binding.mul.setOnClickListener {
            takeIf { operation !=null }?.let {
                binding.equation.text=makeOperation()
            }
            operation=Operation.Mul
            saveOldValue((it as Button).text.toString())
        }
        binding.sub.setOnClickListener {
            takeIf { operation !=null }?.let {
                binding.equation.text=makeOperation()
            }
            operation=Operation.Minus
            saveOldValue((it as Button).text.toString())
        }
        binding.div.setOnClickListener {
            takeIf { operation !=null }?.let {
                binding.equation.text=makeOperation()
            }
            operation=Operation.Div
            saveOldValue((it as Button).text.toString())
        }
        binding.equal.setOnClickListener {
            binding.result.text=makeOperation()
        }
    }
    private fun  makeOperation() =
            when(operation) {
                Operation.Plus -> (oldValue + extractSecondValue('+')).toString()
                Operation.Minus -> (oldValue - extractSecondValue('-')).toString()
                Operation.Mul -> (oldValue * extractSecondValue('*')).toString()
                Operation.Div -> (oldValue / extractSecondValue('/')).toString()
                null -> binding.equation.text.toString()
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
        oldValue=binding.equation.text.toString().apply{
            binding.equation.text= "$this  $op  "
        }.toDouble()
    }
}