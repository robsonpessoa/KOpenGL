package br.org.robsonpessoa

import br.org.robsonpessoa.glfw.*
import org.lwjgl.opengl.GL11

fun main() {
    val app = Engine()

    app.setWindowConfiguration("Hello World", 600, 720)
    app.setOnEngineListener(EngineListenerImpl())

    app.run()
}

class EngineListenerImpl: Engine.EngineListener {

    companion object {
        private const val POSITION = "position"
    }

    override fun onLoadProgramSettings(settings: ProgramSettings) {
        configureShaders(settings)
        configureVertices(settings)
    }

    override fun onDraw(program: Program) {
        GL11.glDrawArrays(GL11.GL_POINTS, 0, program.data[POSITION]!!.count())
    }

    private fun configureVertices(settings: ProgramSettings) {
        settings.setData(POSITION) {
            val vertices = floatArrayOf(
                -0.5f, 0.5f,
                -0.5f, -0.5f, // FIXME Por algum motivo isso não é plotado
                +0.5f, +0.5f,
                -0.5f, -0.5f
            )

            vertices
        }
    }

    private fun configureShaders(settings: ProgramSettings) {
        settings.addShader(
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

        settings.addShader(
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
