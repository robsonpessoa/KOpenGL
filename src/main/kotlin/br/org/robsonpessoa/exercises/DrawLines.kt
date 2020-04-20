package br.org.robsonpessoa.exercises

import br.org.robsonpessoa.glfw.*
import br.org.robsonpessoa.utils.toBuffer
import org.lwjgl.opengl.GL11

class DrawLines : Engine.EngineListener {

    companion object {
        private const val POSITION = "position"
        private const val DIMENSION = 2
    }

    override fun onBuildProgram(builder: ProgramBuilder) {
        configureShaders(builder)
        configureVertices(builder)
    }

    override fun onDraw(window: Window, program: Program) {
        GL11.glClearColor(0f, 0f, 0f, 1.0f)

        program[POSITION]?.let {
            GL11.glDrawArrays(
                GL11.GL_LINE_LOOP,
                0,
                it.capacity() / DIMENSION
            )
        }
    }

    private fun configureVertices(builder: ProgramBuilder) {
        builder.setData(POSITION) {
            floatArrayOf(
                -0.5f, 0.5f,
                -0.5f, -0.5f,
                +0.5f, -0.5f,
                +0.5f, +0.5f
            ).toBuffer()
        }
    }

    private fun configureShaders(builder: ProgramBuilder) {
        builder.addShader(
            VertexShaderBuilder()
                .code(
                    """
                            attribute vec2 $POSITION;
                            void main(){
                                gl_Position = vec4($POSITION,0.0,1.0);
                            }
                            """.trimIndent()
                )
                .build()
        )

        builder.addShader(
            FragmentShaderBuilder()
                .code(
                    """
                            void main(){
                                gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
                            }
                        """.trimIndent()
                )
                .build()
        )
    }
}
