package br.org.robsonpessoa.transformation

/**
 * Represents the Scale [Transformation].
 *
 * @constructor
 * Builds the transformation according to the aspect ratio of each coordinate.
 *
 * @param x the aspect radio value according to the x axis.
 * @param y the aspect radio value according to the y axis.
 * @param z the aspect radio value according to the z axis.
 */
class Scale(x: Float = 1.0f, y: Float = 1.0f, z: Float = 1.0f) : Transformation() {
    init {
        matrix.apply {
            this[0, 0] = x
            this[1, 1] = y
            this[2, 2] = z
        }
    }
}
