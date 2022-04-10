import utils.Point2D
import kotlin.math.*

class Grid(var xs: Int, var ys: Int, xp:Double = .2, yp:Double = .8) {

    private var shift_x:Int

    private var shift_y:Int

    private var grid: ArrayList<ArrayList<String>> = ArrayList()

    init{
        shift_x = (xp * xs).toInt()
        shift_y = (yp * ys).toInt()

        for(i:Int in 0 until ys){
            val r:ArrayList<String> = ArrayList()
            for(j:Int in 0 until xs) r.add(" . ")
            grid.add(r)
        }

        for(i:ArrayList<String> in grid) i[shift_x] = " | "

        for(i:Int in 0 until this.grid[shift_y].size) grid[shift_y][i] = " ─ "
    }

    fun emplace(x:Int, y:Int, v:() -> String = {" * "}): Unit{
        if(shift_y-y >= ys || shift_x+x >= xs || shift_y-y < 0 || shift_x+x < 0) return
        grid[shift_y-y][x+shift_x] = v.invoke()
    }

    fun emplace(p: ArrayList<Point2D>, v:() -> String = {" * "}): Unit{
        p.forEach {
            emplace(it.x, it.y){v.invoke()}
        }
    }

    fun graph_func(x1:Int, x2:Int = xs, th:String = "*", func:(Double) -> (Double)):Unit {
        for(i:Int in min(x1, x2).. max(x1, x2)){
            emplace(i, ((100 * func.invoke(i.toDouble())).roundToInt() /100.0).toInt()){" $th "}
        }
    }

    val show: Unit
        get() {
            for(i:ArrayList<String> in grid){
                for(j:Int in 0 until i.size) print("${i[j]}")
                println()
            }
        }

    val reset: Unit
        get(){

            for(i:ArrayList<String> in grid){
                for(j:Int in 0 until i.size) i[j] = " . "
            }

            for(i:ArrayList<String> in grid) i[shift_x] = " | "

            for(i:Int in 0 until this.grid[shift_y].size) grid[shift_y][i] = " - "
        }
}