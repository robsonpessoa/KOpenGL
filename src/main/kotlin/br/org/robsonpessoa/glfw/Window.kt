package br.org.robsonpessoa.glfw

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWMouseButtonCallback
import org.lwjgl.system.MemoryUtil

data class Window(val title: String, val width: Int, val height: Int) {
    private var mId: Long? = null

    private var keyCallback: GLFWKeyCallback? = null
    private var mouseCallback: GLFWMouseButtonCallback? = null

    private var keyListener: OnKeyListener? = null
    private var mouseListener: OnMouseListener? = null

    val id: Long
        get() = mId!!

    fun init() {
        if (!GLFWModule.isRunning) {
            throw IllegalStateException("GLFW Module must be started before create a new window.")
        }

        // Configure our window
        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)

        // Create the window
        mId = GLFW.glfwCreateWindow(
            width,
            height,
            title,
            MemoryUtil.NULL,
            MemoryUtil.NULL
        )
        if (mId == MemoryUtil.NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        // Get the resolution of the primary monitor
        val vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())!!

        // Center our window
        GLFW.glfwSetWindowPos(
            id,
            (vidMode.width() - width) / 2,
            (vidMode.height() - height) / 2
        )

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(id)
        // Enable v-sync
        GLFW.glfwSwapInterval(1)

        // Make the window visible
        GLFW.glfwShowWindow(id)

        configureEventsCallback()
    }

    fun destroy() {
        GLFW.glfwDestroyWindow(id)
        keyCallback?.free()
        mouseCallback?.free()
    }

    fun isOpen(): Boolean {
        return !GLFW.glfwWindowShouldClose(id)
    }

    fun sync() {
        // Swap the color buffers
        GLFW.glfwSwapBuffers(id);

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        GLFW.glfwPollEvents();
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

    private fun configureEventsCallback() {
        keyCallback = GLFW.glfwSetKeyCallback(id, object : GLFWKeyCallback() {
            override fun invoke(
                window: Long,
                key: Int,
                scancode: Int,
                action: Int,
                mods: Int
            ) {

//                println("[key event] key: $key")
//                println("[key event] scancode: $scancode")
//                println("[key event] action: $action")
//                println("[key event] mods: $mods")
//                println()

                keyListener?.onKeyPressed(key, scancode, action, mods)
            }
        })

        mouseCallback = GLFW.glfwSetMouseButtonCallback(id, object : GLFWMouseButtonCallback() {
            override fun invoke(window: Long, button: Int, action: Int, mods: Int) {
//                println("[mouse event] button: $button")
//                println("[mouse event] action: $action")
//                println("[mouse event] mods: $mods")
//                println()

                mouseListener?.onMouseClick(button, action, mods)
            }
        })
    }
}
