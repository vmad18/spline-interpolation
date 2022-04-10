import utils.Point2D

fun main() {
    var grid:Grid = Grid(20, 20)

    //grid.graph_func(-10, func = utils.ExampleFunctions.tanh_func)

    var points = arrayOf(Point2D(0, 2), Point2D(-3, 10), Point2D(4, 7), Point2D(8, -2), Point2D(10, 5))

    val spline:Spline = Spline()

    spline.add_points(
        points
    )

    spline.interpolate

    spline.graph_spline(grid){"*"}

    grid.show

}
