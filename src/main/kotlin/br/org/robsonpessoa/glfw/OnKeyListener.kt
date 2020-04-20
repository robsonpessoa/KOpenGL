package br.org.robsonpessoa.glfw

interface OnKeyListener {
    fun onKeyPressed(key: Int, scanCode: Int, action: Int, mods: Int)
}
