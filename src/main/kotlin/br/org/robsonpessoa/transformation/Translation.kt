package br.org.robsonpessoa.transformation

/**
 * Represents the Translate [Transformation].
 *
 * @constructor
 * Builds the transformation according to the shift of each coordinate.
 *
 * @param x the shift value according to the x axis.
 * @param y the shift value according to the y axis.
 * @param z the shift value according to the z axis.
 */
class Translation(x: Float = 0.0f, y: Float = 0.0f, z: Float = 0.0f) : Transformation() {
    init {
        this.matrix.apply {
            this[0, 3] = x
            this[1, 3] = y
            this[2, 3] = z
        }
    }
}
