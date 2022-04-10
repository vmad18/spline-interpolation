package utils

class Point2D(var x:Int, var y:Int) {

    operator fun Point2D.plus(p2: Point2D): Point2D = Point2D(x+p2.x, y+p2.y)

}