/***
 * Cópia da classe Engine de https://github.com/Zi0P4tch0/LWJGL-Kotlin-Example
 */

package br.org.robsonpessoa.glfw

import br.org.robsonpessoa.OnKeyListener
import br.org.robsonpessoa.OnMouseListener
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWMouseButtonCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

class Application(private val title: String) {

    companion object {
        val WINDOW_SIZE = Pair(720, 600)
    }

    private var errorCallback : GLFWErrorCallback? = null
    private var keyCallback : GLFWKeyCallback? = null
    private var mouseCallback: GLFWMouseButtonCallback? = null

    private var window : Long? = null

    private var keyListener: OnKeyListener? = null
    private var mouseListener: OnMouseListener? = null
    private var applicationListener: ApplicationListener? = null

    private var program: Program? = null

    private fun init() {

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        errorCallback = glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err))

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() ) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        configureWindow()
        configureEventsCallback()

        // Make the OpenGL context current
        glfwMakeContextCurrent(window!!)
        // Enable v-sync
        glfwSwapInterval(1)

        // Make the window visible
        glfwShowWindow(window!!)
    }

    private fun configureWindow() {
        // Configure our window
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        // Create the window
        window = glfwCreateWindow(WINDOW_SIZE.first, WINDOW_SIZE.second, title, NULL, NULL)
        if (window == NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        // Get the resolution of the primary monitor
        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!

        // Center our window
        glfwSetWindowPos(
            window!!,
            (vidmode.width() - WINDOW_SIZE.first) / 2,
            (vidmode.height() - WINDOW_SIZE.second) / 2
        )
    }

    private fun configureEventsCallback() {
        keyCallback = glfwSetKeyCallback(window!!, object : GLFWKeyCallback() {
            override fun invoke(
                window: Long,
                key: Int,
                scancode: Int,
                action: Int,
                mods: Int
            ) {

                println("[key event] key: $key")
                println("[key event] scancode: $scancode")
                println("[key event] action: $action")
                println("[key event] mods: $mods")
                println()

                keyListener?.onKeyPressed(key, scancode, action, mods)
            }
        })

        mouseCallback = glfwSetMouseButtonCallback(window!!, object : GLFWMouseButtonCallback() {
            override fun invoke(window: Long, button: Int, action: Int, mods: Int) {
                println("[mouse event] button: $button")
                println("[mouse event] action: $action")
                println("[mouse event] mods: $mods")
                println()

                mouseListener?.onMouseClick(button, action, mods)
            }
        })
    }

    private fun configureGLSLProgram() {
        val builder = ProgramBuilder()
        applicationListener?.onConfiguringProgram(builder)
        program = builder.build()
    }

    private fun loop() {

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities()

        configureGLSLProgram()

        // Set the clear color
        glClearColor(0f, 0f, 0f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window!!) ) {

            // Clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            glClearColor(0f, 0f, 0f, 1.0f)

            applicationListener?.onDraw(program!!)

            // Swap the color buffers
            glfwSwapBuffers(window!!);

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }

    }

    fun run() {

        try {

            init()
            loop()
            // Destroy window
            glfwDestroyWindow(window!!);
            keyCallback?.free()
            mouseCallback?.free()

        } finally {

            // Terminate GLFW
            glfwTerminate()
            errorCallback?.free()

        }
    }

    fun setOnKeyListener(listener: OnKeyListener) {
        keyListener = listener
    }

    fun removeKeyListener() {
        keyListener = null
    }

    fun setOnMouseListener(listener: OnMouseListener) {
        mouseListener = listener
    }

    fun removeMouseListener() {
        mouseListener = null
    }

    fun setOnApplicationListener(listener: ApplicationListener) {
        this.applicationListener = listener
    }

    interface ApplicationListener {
        fun onConfiguringProgram(builder: ProgramBuilder)
        fun onDraw(program: Program)
    }

}
