import java.io.File

fun main() {

    println("welcome to matrix file generator")
    var n = 1
    while (n % 2 != 0) {
        println("please input an even number n to assign matrix size:")
        n = readLine()!!.toInt()
    }
    val filename1 = "matrixM-$n.txt"
    val filename2 = "matrixN-$n.txt"
    val N = n * n
    val a = Array(N) { (1..n).random().toString() } //narrow elem range from N to n
    val b = Array(N) { (1..n).random().toString() }
//    a.forEach(::println)

    val file1 = File(filename1)
    val file2 = File(filename2)
    file1.writeText("${n}\r\n")
    file2.writeText("${n}\r\n")
    var i = 0
    while (i < N) {
        file1.appendText(a[i])
        file2.appendText(b[i])
        if(i!= N-1){
            file1.appendText(" ")
            file2.appendText(" ")
        }
        i++
    }
    return
}