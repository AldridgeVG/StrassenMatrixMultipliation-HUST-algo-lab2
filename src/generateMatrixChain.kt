import java.io.File

fun main() {
    var i = 0
    var j = 0
    var k = 0
    println("welcome to matrix file generator")
    println("please input test number:")
    val testNum = readLine()!!.toInt()
    println("please input number of matrices:")
    val n = readLine()!!.toInt()
    println("input ${n + 1} numbers signifying m,n,o,p,...")
    var num = IntArray(n + 1) { 0 }
    while(i<n+1){
        println("input number $i")
        num[i] = readLine()!!.toInt()
        i++
    }
    //traverse num array
    num.forEach(::println)

    var filename = Array(n) { "" }
    i=0
    var x = 0
    var y = 0
    k=0
    while(k<n) {
        x=num[k]
        y=num[k+1]
        filename[k]="matrixChain_test${testNum}_matrix${k+1}.txt"
        var N = x*y
        val a = Array(N) { (1..x).random().toString() }
//    a.forEach(::println)

        var file = File(filename[k])
        file.writeText("${x}\r\n${y}\r\n")
        i = 0
        while (i < N) {
            file.appendText(a[i])
            if (i != N - 1) {
                file.appendText(" ")
            }
            i++
        }
        k++
    }
    return
}