package br.org.robsonpessoa.transformation

class Translation(x: Float = 0.0f, y: Float = 0.0f, z: Float = 0.0f) : Transformation() {
    init {
        this.matrix.apply {
            this[0, 3] = x
            this[1, 3] = y
            this[2, 3] = z
        }
    }
}
