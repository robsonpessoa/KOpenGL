package br.org.robsonpessoa

import br.org.robsonpessoa.glfw.*
import org.lwjgl.opengl.GL11

fun main() {
    val app = Application("Hello World")
    app.setOnApplicationListener(ApplicationListenerImpl())
    app.run()
}

class ApplicationListenerImpl: Application.ApplicationListener {

    companion object {
        private const val POSITION = "position"
    }

    override fun onConfiguringProgram(builder: ProgramBuilder) {
        configureShaders(builder)
        configureVertices(builder)
    }

    override fun onDraw(program: Program) {
        GL11.glDrawArrays(GL11.GL_POINTS, 0, program.data[POSITION]!!.count())
    }

    private fun configureVertices(builder: ProgramBuilder) {
        builder.setData(POSITION) {
            val vertices = floatArrayOf(
                -0.5f, 0.5f,
                -0.5f, -0.5f, // FIXME Por algum motivo isso não é plotado
                +0.5f, +0.5f,
                -0.5f, -0.5f
            )

            vertices
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
