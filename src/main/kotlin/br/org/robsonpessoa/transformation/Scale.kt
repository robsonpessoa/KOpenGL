package br.org.robsonpessoa.transformation

class Scale(x: Float = 1.0f, y: Float = 1.0f, z: Float = 1.0f) : Transformation() {
    init {
        matrix.apply {
            this[0, 0] = x
            this[1, 1] = y
            this[2, 2] = z
        }
    }
}
