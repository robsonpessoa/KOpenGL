package br.org.robsonpessoa.workassignments

import br.org.robsonpessoa.Spring
import br.org.robsonpessoa.glfw.*
import br.org.robsonpessoa.utils.toBuffer
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20.glGetUniformLocation
import org.lwjgl.opengl.GL20.glUniformMatrix4fv

class SpiralSpring : Engine.EngineListener, OnKeyListener {

    companion object {
        private const val POSITION = "position"
        private const val TRANSFORMATION = "mat_transformation"
        private const val DIMENSION = 2
    }

    private val spring = Spring()

    override fun onBuildProgram(builder: ProgramBuilder) {
        configureShaders(builder)
        configureVertices(builder)
    }

    override fun onDraw(window: Window, program: Program) {
        GL11.glClearColor(0f, 0f, 0f, 1.0f)

        val loc = glGetUniformLocation(
            program.id,
            TRANSFORMATION
        )
        val transformation = spring.getTransformation()

        glUniformMatrix4fv(
            loc, true,
            transformation.toFloatBuffer()
        )

        program[POSITION]?.let {
            GL11.glDrawArrays(
                GL11.GL_LINE_STRIP,
                0,
                it.capacity() / DIMENSION
            )
        }
    }

    private fun configureVertices(builder: ProgramBuilder) {
        builder.setData(POSITION) {
            spring.ctrlPoints.toBuffer()
        }
    }

    private fun configureShaders(builder: ProgramBuilder) {
        builder.addShader(
            VertexShaderBuilder()
                .code(
                    """
                            attribute vec2 $POSITION;
                            uniform mat4 mat_transformation;
                            void main(){
                                gl_Position = mat_transformation * vec4($POSITION,0.0,1.0);
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

    override fun onKeyPressed(key: Int, scanCode: Int, action: Int, mods: Int) {
        if (scanCode == 116 && action == 1) {
            spring.pull()
        } else if (scanCode == 116 && action == 0) {
            spring.leave()
        }
    }
}
