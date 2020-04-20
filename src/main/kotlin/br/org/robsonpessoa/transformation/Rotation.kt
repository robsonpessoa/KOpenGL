package br.org.robsonpessoa.transformation

import br.org.robsonpessoa.math.IdentityMatrix
import br.org.robsonpessoa.math.Matrix
import kotlin.math.cos
import kotlin.math.sin

/**
 * Represents the Rotation [Transformation].
 *
 * @constructor
 * Builds the transformation according to the angle.
 *
 * @param angle the angle in radians.
 */
class Rotation(angle: Double) : Transformation() {

    init {
        this.matrix = getMatrixTransformation(angle)
    }

    /**
     * Builds the transformation based on a pivot param (x, y).
     *
     * @param x the coordinate x param.
     * @param y the coordinate y param.
     * @param angle the angle in radians.
     */
    constructor(x: Float, y: Float, angle: Double) : this(angle) {
        this.matrix = (Translation(x, y) + buildTransformation(angle) + Translation(-x, -y))
            .getTransformationMatrix()
    }

    companion object {
        private const val DIMENSION = 4

        private fun buildTransformation(angle: Double): Transformation = Transformation(getMatrixTransformation(angle))

        private fun getMatrixTransformation(angle: Double): Matrix = IdentityMatrix(DIMENSION).apply {
            val (cos, sin) = angleCalculus(angle)

            this[0, 0] = cos
            this[0, 1] = -1.0f * sin
            this[1, 0] = sin
            this[1, 1] = cos
        }

        private fun angleCalculus(angle: Double): Pair<Float, Float> {
            val cos = cos(angle.toFloat())
            val sin = sin(angle.toFloat())

            return Pair(cos, sin)
        }
    }

}
