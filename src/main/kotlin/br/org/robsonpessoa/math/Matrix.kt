package br.org.robsonpessoa.math

import br.org.robsonpessoa.utils.toBuffer
import java.nio.FloatBuffer

data class Matrix(val rows: Int, val columns: Int) {
    val data = Array(rows) { FloatArray(columns) }

    operator fun times(other: Matrix): Matrix {
        val product = Matrix(rows, other.columns)

        for (i in 0 until rows) {
            for (j in 0 until other.columns) {
                for (k in 0 until columns) {
                    product.data[i][j] += data[i][k] * other.data[k][j]
                }
            }
        }

        return product
    }

    private fun toArray(): FloatArray {
        return FloatArray(rows * columns).apply {
            var index = 0
            (0 until rows).forEach { row ->
                (0 until columns).forEach { column ->
                    this[index++] = data[row][column]
                }
            }
        }
    }

    fun toBuffer(): FloatBuffer {
        return toArray().toBuffer()
    }
}
