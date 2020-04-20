package br.org.robsonpessoa.transformation

import br.org.robsonpessoa.math.IdentityMatrix
import br.org.robsonpessoa.math.Matrix
import java.nio.FloatBuffer

open class Transformation(protected var matrix: Matrix = IdentityMatrix(DIMENSION)) {

    companion object {
        protected const val DIMENSION = 4
    }

    fun getTransformationMatrix(): Matrix = this.matrix

    open fun toFloatBuffer(): FloatBuffer = matrix.toBuffer()

    operator fun plus(other: Transformation): Transformation {
        return Transformation(this.matrix * other.matrix)
    }
}
