package br.org.robsonpessoa.glfw

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20.*
import java.nio.FloatBuffer

class ProgramSettings {
    private val shaders: MutableList<Shader> = mutableListOf()
    private val data = mutableMapOf<String, FloatBuffer>()

    fun addShader(shader: Shader): ProgramSettings {
        if (!shaders.contains(shader)) {
            shaders.add(shader)
        }
        return this
    }

    fun setData(name: String, data: FloatBuffer): ProgramSettings {
        this.data[name] = data
        return this
    }

    fun setData(name: String, f: () -> FloatBuffer): ProgramSettings {
        setData(name, f())
        return this
    }

    fun apply(): Program {
        val program = glCreateProgram()
        return Program(program, shaders, data)
    }

}

data class Program(val id: Int, val shaders: List<Shader>, val data: Map<String, FloatBuffer>) {
    private val location = mutableMapOf<String, Int>()

    fun getLocation(name: String): Int = location[name]!!

    init {
        configureShaders()
        configureBuffer()
        glUseProgram(id)
    }

    private fun configureShaders() {
        shaders.forEach { shader ->
            shader.attach(this)
        }
        link()
    }

    private fun configureBuffer() {
        data.forEach { (key, value) ->
            val buffer = GL15.glGenBuffers()

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer)
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, value, GL15.GL_DYNAMIC_DRAW)
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer)

            location[key] = glGetAttribLocation(this.id, key)
            glEnableVertexAttribArray(getLocation(key))

            // FIXME When using the pointer variable, it doesn't work
            glVertexAttribPointer(getLocation(key), 2, GL11.GL_FLOAT, false, VERTICES_DATA_STRIDE_BYTES, 0)
        }
    }

    private fun link() {
        glLinkProgram(id)

        val buffer = IntArray(1)
        glGetProgramiv(id, GL_LINK_STATUS, buffer)
        if (buffer[0] != GL11.GL_TRUE) {
            val error = glGetProgramInfoLog(id)
            throw RuntimeException("Error while linking the GLSL file: $error")
        }
    }

    companion object {
        private const val DIMENSIONS = 2
        private const val FLOAT_SIZE_BYTES = 4
        private const val VERTICES_DATA_STRIDE_BYTES = DIMENSIONS * FLOAT_SIZE_BYTES

    }
}
