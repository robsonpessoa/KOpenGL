/***
 * The first version was a copy of the class Engine of https://github.com/Zi0P4tch0/LWJGL-Kotlin-Example
 *
 * Some modifications were made to accomplish better decoupling of functions.
 */

package br.org.robsonpessoa.glfw

import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

class Engine {

    private var engineListener: EngineListener? = null

    private var program: Program? = null
    private var window: Window? = null

    private fun init() {
        if (window == null) {
            throw java.lang.IllegalStateException("The Engine must have its window configured before it starts.")
        }

        window!!.init()

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities()

        configureGLSLProgram()

        // Set the clear color
        glClearColor(0f, 0f, 0f, 1.0f)
    }

    private fun configureGLSLProgram() {
        val builder = ProgramSettings()
        engineListener?.onLoadProgramSettings(builder)
        program = builder.apply()
    }

    private fun loop() {
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (window!!.isOpen()) {
            // Clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            engineListener?.onDraw(window!!, program!!)
            window!!.sync()
        }
    }

    fun run() {
        GLFWModule.use {
            init()
            loop()

            // Destroy window
            window!!.destroy()
        }
    }

    fun setWindowConfiguration(title: String, height: Int, width: Int) {
        window = Window(title, width, height)
    }

    fun getWindow(): Window? = window

    fun getProgram(): Program? = program

    fun setOnEngineListener(listener: EngineListener) {
        this.engineListener = listener
    }

    interface EngineListener {
        fun onLoadProgramSettings(settings: ProgramSettings)
        fun onDraw(window: Window, program: Program)
    }

}
