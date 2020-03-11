package br.org.robsonpessoa.utils

import br.org.robsonpessoa.math.Matrix
import org.lwjgl.BufferUtils
import java.nio.FloatBuffer

fun FloatArray.toBuffer(): FloatBuffer {
    val buffer = BufferUtils.createFloatBuffer(count())
    buffer.put(this).flip()
    return buffer
}

fun FloatBuffer.asMatrix(columns: Int): Matrix {
    val matrix = Matrix(array().count() / columns, columns)
    (0 until array().count()).forEach {
        matrix.data[it / columns][it % columns] = get(it)
    }
    return matrix
}
