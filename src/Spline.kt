import utils.Point2D
import kotlin.math.max
import kotlin.math.pow

class Spline {

    private var points: ArrayList<Point2D> = ArrayList()

    private var pieces: ArrayList<Parametric> = ArrayList()

    fun add_points(p: Array<Point2D>): Unit {
        points.addA(p)
    }

    private fun add_coeffs(polys: ArrayList<ArrayList<Double>>, der: Int = 1): ArrayList<ArrayList<Double>> {
        var matrix: ArrayList<ArrayList<Double>> = ArrayList()

        var cnt: Int = -1

        for (p in 0 until polys.size) {
            var c_0: ArrayList<Double> = ArrayList()
            when (p) {
                0 -> {
                    c_0.addL(polys[p])
                    for (i: Int in 0 until (polys.size - 2) * 4) c_0.add(0.0)
                    matrix.add(c_0)
                }
                polys.size - 1 -> {
                    for (i: Int in 0 until (polys.size - 2) * 4) c_0.add(0.0)
                    c_0.addL(polys[p])
                    matrix.add(c_0)
                }
                else -> {
                    for (i: Int in 0..1) {
                        c_0 = ArrayList()
                        for (j: Int in 0..(polys.size - 2) * 4) {
                            if ((cnt + i) * 4 == j) c_0.addL(polys[p])
                            else c_0.add(0.0)
                        }
                        matrix.add(c_0)
                    }
                }
            }
            cnt++
        }
        return matrix
    }

    private fun add_d(arr: ArrayList<Polynomial>): ArrayList<ArrayList<Double>> {

        var matrix: ArrayList<ArrayList<Double>> = ArrayList()
        var r: ArrayList<ArrayList<Double>> = ArrayList()
        var derv: ArrayList<Polynomial> = arr

        for (i: Int in 1 until derv.size - 1) {
            var temp: ArrayList<Double> = ArrayList()
            for (e: Double in derv[i].coeffs) temp.add(e)
            for (e: Double in derv[i].coeffs) temp.add(-e)
            r.add(temp)
        }

        for ((cnt, arr: ArrayList<Double>) in r.withIndex()) {
            var temp: ArrayList<Double> = ArrayList()
            for (j: Int in 0..8 * (r.size)) {
                if (cnt * 4 == j) temp.addL(arr)
                temp.add(0.0)
            }
            if (4 * cnt == 8 * (r.size - 1) - 8)
                temp.addL(arr)
            matrix.add(temp)
        }

        return matrix
    }

    //Gaussian Elimination
    private fun gauss(mat_: ArrayList<ArrayList<Double>>, res_: ArrayList<Double>): ArrayList<Double> {

        var mat: ArrayList<ArrayList<Double>> = mat_.clone() as ArrayList<ArrayList<Double>>
        var res: ArrayList<Double> = res_.clone() as ArrayList<Double>

        for (a: Int in 0 until mat.size) mat[a].add(res[a])

        for (i: Int in 0 until mat.size) {
            for (j: Int in i + 1 until mat.size) {
                if (mat[i][i] == 0.0) {
                    var k = i
                    while (mat[k][i] == 0.0) k++

                    var t1 = mat[k].clone() as ArrayList<Double>
                    mat[k] = mat[i].clone() as ArrayList<Double>
                    mat[i] = t1
                }

                val ratio: Double = mat[j][i] / mat[i][i]

                for (k: Int in i..mat.size) mat[j][k] -= ratio * mat[i][k]
            }
        }

        //backwards substitution
        var ans: ArrayList<Double> = ArrayList()
        for (i: Int in mat.size - 1 downTo 0) {
            ans.add(0, mat[i][mat.size] / mat[i][i])
            for (j: Int in i - 1 downTo 0) {
                mat[j][mat.size] -= mat[j][i] * ans[0]
            }
        }

        return ans
    }

    val interpolate: Unit
        get() {
            var polys: ArrayList<Polynomial> = ArrayList()
            var output: ArrayList<Double> = ArrayList()

            for ((curr, p: Point2D) in points.withIndex()) {
                polys.add(Polynomial(1.0, x = p.x.toDouble(), d = 1.0))

                if (curr == 0 || curr == points.size - 1) output.add(p.y.toDouble())
                else {
                    output.add(p.y.toDouble())
                    output.add(p.y.toDouble())
                }
            }

            for (i: Int in 0 until 4 * (polys.size - 1) - output.size) output.add(0.0)


            var matrix: ArrayList<ArrayList<Double>> = ArrayList()

            matrix.addL(add_coeffs(polys.coeffs))
            matrix.addL(add_d(polys.derivatives))
            matrix.addL(add_d(polys.derivatives.derivatives))

            var s_f = polys[0].derivative.derivative.coeffs
            var s_s = polys[polys.size - 1].derivative.derivative.coeffs

            for (i: Int in 0 until 4 * (polys.size - 2)) {
                s_f.add(0.0)
                s_s.add(0, 0.0)
            }

            matrix.addL(arrayListOf(s_f))
            matrix.addL(arrayListOf(s_s))

            var f_coeffs = gauss(matrix, output)

            for (c: Int in 0 until f_coeffs.size step 4) {
                println("${f_coeffs[c]}*x^3 + ${f_coeffs[c + 1]}*x^2 + ${f_coeffs[c + 2]}*x + ${f_coeffs[c + 3]} [${points[c / 4].x}, ${points[c / 4 + 1].x}]")
                pieces.add(Parametric(points[c / 4].x, points[c / 4 + 1].x) {
                    f_coeffs[c] * it * it * it + f_coeffs[c + 1] * it * it + f_coeffs[c + 2] * it + f_coeffs[c + 3]
                })
            }
        }

    fun graph_spline(grid:Grid, l:()->String){
        for(pf:Parametric in pieces) grid.graph_func(pf.start, pf.end, l.invoke(), pf.func)
        grid.emplace(points){" x "}
    }
}

data class Parametric(val start: Int, val end: Int, val func: (Double) -> (Double))

data class Polynomial(
    var a: Double,
    var b: Double = a,
    var c: Double = a,
    var d: Double,
    var x: Double,
    var der: Int = 0
) {

    val derivative: Polynomial
        get() = Polynomial(a * (3 - der), b * (2 - der), c * (1 - der), d * (0 - der), x, der + 1)

    val coeffs: ArrayList<Double>
        get() = arrayListOf(a * (x.pow(3 - der)), b * (x.pow(2 - der)), c * (x.pow(max(1 - der, 0))), d)

}