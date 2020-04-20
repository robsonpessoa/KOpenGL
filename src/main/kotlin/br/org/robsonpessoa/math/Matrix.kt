package br.org.robsonpessoa.math

import br.org.robsonpessoa.utils.toBuffer
import java.nio.FloatBuffer

/**
 * A class that represents a Matrix of Floats filled with zero.
 *
 * @property rows the number of rows.
 * @property columns the number of columns.
 */
open class Matrix(private val rows: Int, private val columns: Int) {
    protected val data = Array(rows) { FloatArray(columns) }

    /**
     * Gets the value of the param (x, y).
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return the value.
     */
    operator fun get(x: Int, y: Int): Float = data[x][y]

    /**
     * Sets the value (x,y) of the Matrix.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param value the value to be set.
     */
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

    /**
     * The product of two [Matrix]'es.
     *
     * @param other the other [Matrix].
     * @return the product [Matrix].
     */
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

    /**
     * Creates an array of Float based on the Matrix values.
     *
     * @return the array.
     */
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

    /**
     * Creates a buffer of Float based on the Matrix values.
     *
     * @return the buffer.
     */
    fun toBuffer(): FloatBuffer {
        return toArray().toBuffer()
    }

    /**
     * Get's a [String] representing the Matrix values.
     *
     * @return the [String] object.
     */
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

/**
 * A Matrix with the main diagonal filled with 1.
 *
 * @property dimension the dimension of the Matrix.
 */
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
