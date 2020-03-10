package br.org.robsonpessoa.glfw

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback

object GLFWModule {
    private var mStatus = Status.OFF

    private var errorCallback: GLFWErrorCallback? = null

    val isRunning
        get() = mStatus == Status.ON

    fun use(function: () -> Unit) {
        try {
            start()
            function()
        } finally {
            stop()
        }
    }

    fun start() {
        if (mStatus == Status.OFF) {
            // Setup an error callback. The default implementation
            // will print the error message in System.err.
            errorCallback = GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err))

            // Initialize GLFW. Most GLFW functions will not work before doing this.
            if (!GLFW.glfwInit()) {
                throw IllegalStateException("Unable to initialize GLFW")
            }

            mStatus = Status.ON
        }
    }

    fun stop() {
        if (mStatus == Status.ON) {
            // Terminate GLFW
            GLFW.glfwTerminate()
            errorCallback?.free()
            mStatus = Status.OFF
        }
    }

    private enum class Status {
        ON,
        OFF
    }
}
