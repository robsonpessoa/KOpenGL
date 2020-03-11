package br.org.robsonpessoa

import br.org.robsonpessoa.exercises.DrawLines
import br.org.robsonpessoa.glfw.Engine

fun main() {
    val app = Engine()
    val exercise = DrawLines()

    app.setWindowConfiguration("Hello World", 600, 600)
    app.setOnEngineListener(exercise)

    app.run()
}

