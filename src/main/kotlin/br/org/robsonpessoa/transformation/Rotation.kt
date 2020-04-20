package br.org.robsonpessoa.transformation

import br.org.robsonpessoa.math.IdentityMatrix
import br.org.robsonpessoa.math.Matrix
import kotlin.math.cos
import kotlin.math.sin

class Rotation(angle: Double) : Transformation() {

    init {
        this.matrix = getMatrixTransformation(angle)
    }

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
