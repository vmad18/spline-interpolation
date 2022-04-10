package utils

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.tanh

object ExampleFunctions {
    val sine_func = {x:Double -> (3* sin(x*PI/4))}
    val tanh_func = {x:Double -> 3*tanh(10*x)}
    val swish_func = {x:Double -> x*3/(1+exp(-x))}
    val exp_func = {x:Double -> exp(x/4)}
}