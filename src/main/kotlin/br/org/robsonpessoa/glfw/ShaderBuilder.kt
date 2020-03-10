package br.org.robsonpessoa.glfw

import org.lwjgl.opengl.GL20.*

interface ShaderBuilder {
    var definition: Int?
    var code: String?
    fun code(code: String): FinalShaderBuilder
}

interface FinalShaderBuilder : ShaderBuilder {
    fun build(): Shader {
        return Shader(definition!!, code!!)
    }
}

data class Shader(private val definition: Int, val code: String) {
    private var mId: Int? = null
    val id: Int
        get() = mId!!

    private fun compile(shader: Int) {
        glCompileShader(shader)

        val buffer = IntArray(1)
        glGetShaderiv(shader, GL_COMPILE_STATUS, buffer)
        if (buffer[0] == 0) {
            val error = glGetShaderInfoLog(shader)
            throw RuntimeException("Error while compiling the shader: $error")
        }
    }

    fun attach(program: Program) {
        val shader = glCreateShader(this.definition)
        glShaderSource(shader, this.code)
        compile(shader)
        mId = shader
        glAttachShader(program.id, id)
    }
}

class VertexShaderBuilder : ShaderBuilder, FinalShaderBuilder {
    override var code: String? = null
    override var definition: Int? = null

    init {
        this.definition = GL_VERTEX_SHADER
    }

    override fun code(code: String): FinalShaderBuilder {
        this.code = code
        return this
    }
}

class FragmentShaderBuilder : ShaderBuilder, FinalShaderBuilder {
    override var code: String? = null
    override var definition: Int? = null

    init {
        this.definition = GL_FRAGMENT_SHADER
    }

    override fun code(code: String): FinalShaderBuilder {
        this.code = code
        return this
    }
}


