package br.org.robsonpessoa.glfw

import org.lwjgl.opengl.GL20.*

/**
 * Interface to be used to all Shader type builders.
 *
 */
interface ShaderBuilder {
    var definition: Int?
    var key: String?

    /**
     * Sets the key of the shader.
     *
     * @param key the key string that represents the Shader.
     * @return this [ShaderBuilder].
     */
    fun code(key: String): ShaderBuilder {
        this.key = key
        return this
    }

    /**
     * Builds the [Shader] according to the key and the type of the Shader.
     *
     * @return the [Shader] built.
     */
    fun build(): Shader {
        return Shader(key!!, definition!!)
    }
}

/**
 * Builder to Vertex Shaders.
 *
 */
class VertexShaderBuilder : ShaderBuilder {
    override var key: String? = null
    override var definition: Int? = null

    init {
        this.definition = GL_VERTEX_SHADER
    }
}

/**
 * Builder to Fragment Shaders.
 *
 */
class FragmentShaderBuilder : ShaderBuilder {
    override var key: String? = null
    override var definition: Int? = null

    init {
        this.definition = GL_FRAGMENT_SHADER
    }
}

/**
 * The OpenGL Shader.
 *
 * @property definition the shader type.
 * @property code the shader key.
 */
data class Shader(val code: String, private val definition: Int) {
    private var mId: Int? = null

    val id: Int
        get() = mId!!

    /**
     * Compiles the shader.
     *
     * @param id the Shader id.
     */
    private fun compile(id: Int) {
        glCompileShader(id)

        val buffer = IntArray(1)
        glGetShaderiv(id, GL_COMPILE_STATUS, buffer)
        if (buffer[0] == 0) {
            val error = glGetShaderInfoLog(id)
            throw RuntimeException("Error while compiling the shader: $error")
        }
    }

    /**
     * Creates the OpenGL shader and attach to the program.
     *
     * @param program the [Program] to attach the shader.
     */
    fun attach(program: Program) {
        val id = glCreateShader(this.definition)
        glShaderSource(id, this.code)
        compile(id)
        mId = id
        glAttachShader(program.id, this.id)
    }
}


