import java.io.File

//DATA STRUCTURE OF MATRIX---2D ARRAY AND getSize() FUNCTION
class Matrix(val n: Int) {

    //if n=0, initialization failure
    init {
        require(n > 0, { println("invalid matrix initialization") })
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
fun matrixMultipliation(A: Matrix, B: Matrix): Matrix {
    if (A.getSize() != B.getSize()) {
        println("Matrix size doesn't fit")
    }
    val n = A.getSize()
    var C = Matrix(n)
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
    var C = Matrix(n)
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
    println("matrix size: n = $n")
    var i = 0
    var j = 0
    while (i < n) {
        j = 0
        while (j < n) {
            print("${M.elem[i][j]}\t\t")
            j++
        }
        i++
        println("\n")
    }
    println()
}

//-----------------------------------------------------------------------------//
fun main(args: Array<String>) {
    //file read
    var M = readMatrix("matrixM.txt")
    var N = readMatrix("matrixN.txt")
    var n = 0
    if (M.getSize() == N.getSize()) {
        n = M.getSize()
    } else {
        println("file reading error")
        return
    }
    val m = n / 2
    println("Matrix for multipliation:")
    println("Matrix M:")
    printMatrix(M)
    println("Matrix N:")
    printMatrix(N)

    //naive
    val ans = matrixMultipliation(M, N)
    println("Matrix ANSWER:")
    printMatrix(ans)

    //construct Strassen factor --- checked
    val A = cutSubMatrix(M, 0, 0, m)
    val B = cutSubMatrix(M, 0, m, m)
    val C = cutSubMatrix(M, m, 0, m)
    val D = cutSubMatrix(M, m, m, m)

    val E = cutSubMatrix(N, 0, 0, m)
    val F = cutSubMatrix(N, 0, m, m)
    val G = cutSubMatrix(N, m, 0, m)
    val H = cutSubMatrix(N, m, m, m)

    //Strassen's Matrix Multipliation------------------------------------------------------//
    //Step1 form factor 1-7
    val P1 = matrixMultipliation(A, matrixMinus(F, H))                  //p1 = a(f-h)
    val P2 = matrixMultipliation(H, matrixAdd(A, B))                    //p2 = (a+b)h
    val P3 = matrixMultipliation(E, matrixAdd(C, D))                    //p3 = (c+d)e
    val P4 = matrixMultipliation(D, matrixMinus(G, E))                  //p4 = d(g-e)
    val P5 = matrixMultipliation(matrixAdd(A, D), matrixAdd(E, H))      //p5 = (a+d)(e+h)
    val P6 = matrixMultipliation(matrixMinus(B, D), matrixAdd(G, H))    //p6 = (b-d)(g+h)
    val P7 = matrixMultipliation(matrixMinus(A, C), matrixAdd(E, F))    //p7 = (a-c)(e+f)

    //Step2 form 4 sub matrix
    val NorthWest = matrixAdd(matrixAdd(P5, P6), matrixMinus(P4, P2))   //NW = P5+P4-P2+P6
    val NorthEast = matrixAdd(P1, P2)                                   //NE = P1+P2
    val SouthWest = matrixAdd(P3, P4)                                   //SW = P3+P4
    val SouthEast = matrixAdd(matrixMinus(P1, P3), matrixMinus(P5, P7))   //SE = P1+P5-P3-P7

    //Step3 form result matrix
    val result = formMatrix(NorthWest, NorthEast, SouthWest, SouthEast)
    //-------------------------------------------------------------------------------------//

    println("Matrix RESULT:")
    printMatrix(result)

}