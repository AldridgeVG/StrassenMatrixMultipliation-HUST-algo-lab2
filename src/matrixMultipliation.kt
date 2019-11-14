import java.io.File
import javax.swing.text.html.HTML.Attribute.N

//DATA STRUCTURE OF MATRIX---2D ARRAY AND getSize() FUNCTION
class Matrix(val n: Int) {

    //if n=0, initialization failure
    init {
        require(n > 0) { println("invalid matrix initialization") }
    }

    //initialize n*n Integer array elem
    var elem = Array(n) { IntArray(n) { 0 } }

    fun getSize(): Int {
        return n
    }
}

//CONSTRUCT SUB MATRIX FROM EXISTING MATRIX-----------------------------------//
//CONSTRUCT MATRIX FROM EXISTING SUB MATRIX
fun cutSubMatrix(A: Matrix, i: Int, j: Int, n: Int): Matrix {
    if (n > A.getSize()) {
        println("subMatrix initiation failure")
        return A
    }
    var B = Matrix(n)
    var i1 = i
    var j1 = j
    while (i1 < i + n) {
        j1 = j
        while (j1 < j + n) {
            B.elem[i1 - i][j1 - j] = A.elem[i1][j1]
            j1++
        }
        i1++
    }
    return B
}

fun formMatrix(NW: Matrix, NE: Matrix, SW: Matrix, SE: Matrix): Matrix {
    val m = NW.getSize()
    val n = m + m
    var M = Matrix(n)
    var i = 0
    var j = 0
    //copy NorthWest
    i = 0
    while (i < m) {
        j = 0
        while (j < m) {
            M.elem[i][j] = NW.elem[i][j]
            j++
        }
        i++
    }
    //copy NorthEast
    i = 0
    while (i < m) {
        j = m
        while (j < n) {
            M.elem[i][j] = NE.elem[i][j - m]
            j++
        }
        i++
    }
    //copy SouthWest
    i = m
    while (i < n) {
        j = 0
        while (j < m) {
            M.elem[i][j] = SW.elem[i - m][j]
            j++
        }
        i++
    }
    //copy SouthEast
    i = m
    while (i < n) {
        j = m
        while (j < n) {
            M.elem[i][j] = SE.elem[i - m][j - m]
            j++
        }
        i++
    }

    return M
}
//----------------------------------------------------------------------------//

//CALCULATION BETWEEN MATRIX--------------------------------------------------//
fun matrixMultiplication(A: Matrix, B: Matrix): Matrix {
    if (A.getSize() != B.getSize()) {
        println("Matrix size doesn't fit")
    }
    val n = A.getSize()
    val C = Matrix(n)
    var i = 0
    var j = 0
    var k = 0
    while (i < n) {
        j = 0
        while (j < n) {
            k = 0
            while (k < n) {
                C.elem[i][j] += A.elem[i][k] * B.elem[k][j]
                k++
            }
            j++
        }
        i++
    }
    return C
}   //---checked

fun matrixAdd(A: Matrix, B: Matrix): Matrix {
    if (A.getSize() != B.getSize()) {
        println("Matrix size doesn't fit")
    }
    val n = A.getSize()
    val C = Matrix(n)
    var i = 0
    var j = 0
    while (i < n) {
        j = 0
        while (j < n) {
            C.elem[i][j] = A.elem[i][j] + B.elem[i][j]
            j++
        }
        i++
    }
    return C
}

fun matrixMinus(A: Matrix, B: Matrix): Matrix {
    if (A.getSize() != B.getSize()) {
        println("Matrix size doesn't fit")
    }
    val n = A.getSize()
    var C = Matrix(n)
    var i = 0
    var j = 0
    while (i < n) {
        j = 0
        while (j < n) {
            C.elem[i][j] += A.elem[i][j] - B.elem[i][j]
            j++
        }
        i++
    }
    return C
}
//----------------------------------------------------------------------------//

//IO-OP OF MATRIX-------------------------------------------------------------//
fun readMatrix(filename: String): Matrix {
    val line = File(filename).readLines()
    val n = line[0].toInt()
    val readElem = line[1].split(" ").toList().toTypedArray()
    if (readElem.size != n * n) {
        println("file reading error")
        return Matrix(0)
    }
    var M = Matrix(n)
    var i = 0
    var j = 0
    var k = 0
    while (i < n) {
        j = 0
        while (j < n) {
            M.elem[i][j] = readElem[k].toInt()
            j++
            k++
        }
        i++
    }
    return M
}

fun printMatrix(M: Matrix) {
    val n = M.getSize()
    if (n <= 0) {
        println("invalid matrix input!")
        return
    }
    println("matrix size: n = $n\n")
    var i = 0
    var j = 0
    while (i < n) {
        j = 0
        while (j < n) {
            print("${M.elem[i][j]} \t\t")
            j++
        }
        i++
        println("\n\n")
    }
    println()
}

//-----------------------------------------------------------------------------//
fun main(args: Array<String>) {
    //file read
    println("input filename of matrix M:")
    val filename1 = readLine()!!
    println("input filename of matrix N:")
    val filename2 = readLine()!!
    val M = readMatrix(filename1)
    val N = readMatrix(filename2)
    var n = 0
    if (M.getSize() == N.getSize()) {
        n = M.getSize()
    } else {
        println("file reading error")
        return
    }
    val m = n / 2
    println("Matrix for Multiplication:")
    println("Matrix M:")
    printMatrix(M)
    println("Matrix N:")
    printMatrix(N)

    //naive matrix multi answer for comparison----------------------------//
    val startTime = System.nanoTime()
    val ans = matrixMultiplication(M, N)
    val endTime = System.nanoTime()
    println("naive ANSWER:")
    printMatrix(ans)
    //-----------------------------------------------------------------//

    //construct Strassen factor --- checked
    val A = cutSubMatrix(M, 0, 0, m)
    val B = cutSubMatrix(M, 0, m, m)
    val C = cutSubMatrix(M, m, 0, m)
    val D = cutSubMatrix(M, m, m, m)

    val E = cutSubMatrix(N, 0, 0, m)
    val F = cutSubMatrix(N, 0, m, m)
    val G = cutSubMatrix(N, m, 0, m)
    val H = cutSubMatrix(N, m, m, m)

    //naive D&V multi answer----------------------------------------------------------------------------//
    val startTime1 = System.nanoTime()
    val NorthWest1 = matrixAdd(matrixMultiplication(A, E), matrixMultiplication(B, G))   //NorthWest = ae+bg
    val NorthEast1 = matrixAdd(matrixMultiplication(A, F), matrixMultiplication(B, F))   //NorthEast = af+bh
    val SouthWest1 = matrixAdd(matrixMultiplication(C, E), matrixMultiplication(D, G))   //SouthWest = ce+dg
    val SouthEast1 = matrixAdd(matrixMultiplication(C, F), matrixMultiplication(D, H))   //SouthEast = cf+dh
    val endTime1 = System.nanoTime()
    val result1 = formMatrix(NorthWest1, NorthEast1, SouthWest1, SouthEast1)
    println("naive D&V MM ANSWER:")
    printMatrix(result1)
    //--------------------------------------------------------------------------------------------------//


    //Strassen's Matrix Multiplication------------------------------------------------------//
    //Step1 form factor 1-7
    val startTime2 = System.nanoTime()
    val P1 = matrixMultiplication(A, matrixMinus(F, H))                  //p1 = a(f-h)
    val P2 = matrixMultiplication(matrixAdd(A, B), H)                    //p2 = (a+b)h
    val P3 = matrixMultiplication(matrixAdd(C, D), E)                    //p3 = (c+d)e
    val P4 = matrixMultiplication(D, matrixMinus(G, E))                  //p4 = d(g-e)
    val P5 = matrixMultiplication(matrixAdd(A, D), matrixAdd(E, H))      //p5 = (a+d)(e+h)
    val P6 = matrixMultiplication(matrixMinus(B, D), matrixAdd(G, H))    //p6 = (b-d)(g+h)
    val P7 = matrixMultiplication(matrixMinus(A, C), matrixAdd(E, F))    //p7 = (a-c)(e+f)

    //Step2 form 4 sub matrix
    val NorthWest = matrixAdd(matrixAdd(P5, P6), matrixMinus(P4, P2))       //NW = P5+P4-P2+P6
    val NorthEast = matrixAdd(P1, P2)                                       //NE = P1+P2
    val SouthWest = matrixAdd(P3, P4)                                       //SW = P3+P4
    val SouthEast = matrixAdd(matrixMinus(P1, P3), matrixMinus(P5, P7))     //SE = P1+P5-P3-P7
    val endTime2 = System.nanoTime()

    //Step3 form result matrix
    val result = formMatrix(NorthWest, NorthEast, SouthWest, SouthEast)
    //-------------------------------------------------------------------------------------//

    println("Matrix RESULT:")
    printMatrix(result)

    println("for matrix size n = $n:\n")
    println("naive method time:        " + (endTime - startTime) + "ns")
    println("naive D&V MM method time: " + (endTime1 - startTime1) + "ns")
    println("SMM method time:          " + (endTime2 - startTime2) + "ns")
    println("SMM algo is ${(endTime - startTime) - (endTime2 - startTime2)}ns(${((endTime - startTime) - (endTime2 - startTime2)) / 100000000.00}s) faster than naive MM algo")
    println("SMM algo is ${(endTime1 - startTime1) - (endTime2 - startTime2)}ns(${((endTime1 - startTime1) - (endTime2 - startTime2)) / 1000000000.0}s) faster than normal D&V MM algo")
//    println("SMM algo is %${((endTime - startTime)- (endTime1 - startTime1))/(endTime - startTime)*100} faster than normal D&V MM algo")
}