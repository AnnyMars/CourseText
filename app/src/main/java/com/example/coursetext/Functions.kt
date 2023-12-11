package com.example.coursetext

import kotlin.math.abs

class Functions {

    fun parseMatrix(matrixString: String): Array<DoubleArray>? {
        if (matrixString.isBlank()) {
            return null
        }
        return try {
            val rows = matrixString.trim().split("\n")
            rows.map { row ->
                row.trim().split(" ").map { it.toDouble() }.toDoubleArray()
            }.toTypedArray()
        } catch (e: NumberFormatException) {
            null
        }
    }

    fun parseVector(vectorString: String): DoubleArray? {
        if (vectorString.isBlank()) {
            return null
        }
        return try {
            vectorString.trim().split(" ").map { it.toDouble() }.toDoubleArray()
        } catch (e: NumberFormatException) {
            null
        }
    }

    fun calculateError(x: DoubleArray, xPrev: DoubleArray): Double {
        var error = 0.0
        for (i in x.indices) {
            error += abs(x[i] - xPrev[i])
        }
        return error
    }

    fun solveLinearEquations(A: Array<DoubleArray>, b: DoubleArray, maxIterations: Int, tolerance: Double): DoubleArray? {
        val n = A.size
        val x = DoubleArray(n)
        var iteration = 0
        var error = Double.MAX_VALUE

        while (iteration < maxIterations && error > tolerance) {
            val xPrev = x.copyOf()

            for (i in 0 until n) {
                var sum = 0.0
                for (j in 0 until n) {
                    if (j != i) {
                        sum += A[i][j] * xPrev[j]
                    }
                }
                x[i] = (b[i] - sum) / A[i][i]
            }

            error = calculateError(x, xPrev)
            iteration++
        }

        return if (error <= tolerance) x else null
    }

}