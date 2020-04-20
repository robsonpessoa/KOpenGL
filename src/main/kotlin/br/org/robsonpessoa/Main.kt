package br.org.robsonpessoa

import br.org.robsonpessoa.exercises.TSpiralSpring
import br.org.robsonpessoa.glfw.Engine

fun main() {
    val app = Engine()
    val exercise = TSpiralSpring()

    app.setWindowConfiguration("Hello World", 900, 900)
    app.setOnEngineListener(exercise)
    app.getWindow()!!.setOnKeyListener(exercise)

    app.run()
}

