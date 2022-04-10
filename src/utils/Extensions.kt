val Number.square:Double
        get() = this.toDouble() * this.toDouble()

fun <T> ArrayList<T>.addA(arr: Array<T>){
        for(e:T in arr) this.add(e)
}

fun <T> ArrayList<T>.addL(arr: ArrayList<T>){
        for(e:T in arr) this.add(e)
}

val ArrayList<Polynomial>.derivatives: ArrayList<Polynomial>
        get(){
                var arr:ArrayList<Polynomial> = ArrayList()
                for(p:Polynomial in this) arr.add(p.derivative)
                return arr
        }

val ArrayList<Polynomial>.coeffs: ArrayList<ArrayList<Double>>
        get(){
                var r:ArrayList<ArrayList<Double>> = ArrayList()
                for(p:Polynomial in this) r.add(p.coeffs)
                return r
        }