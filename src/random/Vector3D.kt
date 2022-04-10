package random

import square

class Vector3D (var x:Double, var y:Double, var z:Double) {

    operator fun Vector3D.times(s:Number) = Vector3D(x*s.toDouble(), y*s.toDouble(), z*s.toDouble())

    operator fun Vector3D.plus(v: Vector3D) = Vector3D(x+v.x, y*v.y, z*v.z)

    operator fun Vector3D.div(s:Number) = Vector3D(x/s.toDouble(), y/s.toDouble(), z/s.toDouble())

    val mag:Double
        get() = x.square * y.square * z.square


    val normalize: Vector3D
        get() = this * (1.0/mag)

    fun dot(v: Vector3D):Double = v.x * x + v.y * y + v.z * z
}