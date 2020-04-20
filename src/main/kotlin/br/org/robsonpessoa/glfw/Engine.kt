/***
 * The first version was a copy of the class Engine of https://github.com/Zi0P4tch0/LWJGL-Kotlin-Example
 *
 * Some modifications were made to accomplish better decoupling of functions.
 */

package br.org.robsonpessoa.glfw

import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

/**
 * A class that represents the engine that makes graphical computation using OpenGL libraries.
 *
 */
class Engine {

    private var engineListener: EngineListener? = null

    private var program: Program? = null
    private var window: Window? = null

    /**
     * Starts the engine configuration.
     *
     */
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

    /**
     * Configures the GLSL Program according to the [EngineListener] applied to the object.
     *
     */
    private fun configureGLSLProgram() {
        val builder = ProgramBuilder()
        engineListener?.onBuildProgram(builder)
        program = builder.apply()
    }

    /**
     * Loop and print the objects configured while the Window created is open.
     *
     */
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

    /**
     * Starts the engine.
     *
     */
    fun run() {
        GLFWModule.use {
            init()
            loop()

            window!!.destroy()
        }
    }

    /**
     * Creates a [Window].
     *
     * @param title the window title.
     * @param height the window height.
     * @param width the window width.
     */
    fun setWindowConfiguration(title: String, height: Int, width: Int) {
        window = Window(title, width, height)
    }

    /**
     * Gets the configured window.
     *
     * @return the [Window].
     */
    fun getWindow(): Window? = window

    /**
     * Gets the configured GLSL Program.
     *
     * @return the [Program].
     */
    fun getProgram(): Program? = program

    /**
     * Setter of the Engine Listener.
     *
     * @param listener the [EngineListener].
     */
    fun setOnEngineListener(listener: EngineListener) {
        this.engineListener = listener
    }

    /**
     * Interface that all the listeners of the [Engine] must implement.
     *
     */
    interface EngineListener {
        /**
         * Called when the GLSL Program is being built.
         *
         * @param builder the [ProgramBuilder].
         */
        fun onBuildProgram(builder: ProgramBuilder)

        /**
         * Called in all [loop] iteration to draw objects according to the program built configuration.
         *
         * @param window
         * @param program
         */
        fun onDraw(window: Window, program: Program)
    }

}
