package br.org.robsonpessoa.transformation

import br.org.robsonpessoa.math.IdentityMatrix
import br.org.robsonpessoa.math.Matrix
import java.nio.FloatBuffer

/**
 * Class that represents transformation operations through Transformation [Matrix]'es.
 *
 * @property matrix - the initial Matrix (default is [IdentityMatrix]) to be used in the Transformation
 */
open class Transformation(protected var matrix: Matrix = IdentityMatrix(DIMENSION)) {

    companion object {
        protected const val DIMENSION = 4
    }

    /**
     * Returns the Transformation Matrix that represents the current object transformation.
     *
     * @return the transformation [Matrix].
     */
    fun getTransformationMatrix(): Matrix = this.matrix

    /**
     * Gets the current Transformation Matrix as a [FloatBuffer]
     *
     * @return the Matrix as a buffer
     */
    open fun toFloatBuffer(): FloatBuffer = matrix.toBuffer()

    /**
     * Adds another transformation to the current using the product of the two [Matrix]'es
     *
     * @param other the other [Transformation] to be added.
     * @return the resulting Transformation
     */
    operator fun plus(other: Transformation): Transformation {
        return Transformation(this.matrix * other.matrix)
    }
}
