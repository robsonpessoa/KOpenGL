package br.org.robsonpessoa.math

import br.org.robsonpessoa.utils.toBuffer
import java.nio.FloatBuffer

open class Matrix(private val rows: Int, private val columns: Int) {
    protected val data = Array(rows) { FloatArray(columns) }

    operator fun get(x: Int, y: Int): Float = data[x][y]
    operator fun set(x: Int, y: Int, value: Float) {
        data[x][y] = value
    }

    init {
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                this.data[i][j] = 0.0f
            }
        }
    }

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

    override fun toString(): String {
        var str = ""
        (0 until rows).forEach { row ->
            (0 until columns).forEach { column ->
                if (column == 0) {
                    str += "|"
                }

                str += "\t\t${data[row][column]}\t\t"

                if (column == columns - 1) {
                    str += "|\n"
                }
            }
        }
        return str
    }
}

data class IdentityMatrix(val dimension: Int) : Matrix(dimension, dimension) {
    init {
        for (i in 0 until dimension) {
            this.data[i][i] = 1.0f
        }
    }

    override fun toString(): String {
        return super.toString()
    }
}
