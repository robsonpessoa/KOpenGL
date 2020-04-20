package br.org.robsonpessoa

import br.org.robsonpessoa.glfw.Engine
import br.org.robsonpessoa.workassignments.SpiralSpring

fun main() {
    val app = Engine()
    val exercise = SpiralSpring()

    app.setWindowConfiguration("Hello World", 900, 900)
    app.setOnEngineListener(exercise)
    app.getWindow()!!.setOnKeyListener(exercise)

    app.run()
}

