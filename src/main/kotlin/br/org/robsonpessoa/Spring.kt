package br.org.robsonpessoa

import br.org.robsonpessoa.transformation.Rotation
import br.org.robsonpessoa.transformation.Scale
import br.org.robsonpessoa.transformation.Transformation
import br.org.robsonpessoa.transformation.Translation
import kotlin.math.*
import kotlin.random.Random

class Spring {

    private enum class DIRECTION {
        LEFT,
        RIGHT
    }

    private val jumpDirection = DIRECTION.values()[Random.nextInt(0, 2)]
    private val angleRotation: Lazy<Double>
        get() = lazy { if (jumpDirection == DIRECTION.LEFT) 1.0 else -1.0 }

    val ctrlPoints: FloatArray
    private val centerY: Float

    companion object {
        private const val NUM_ARCH = 10
        private const val COEFFICIENT = 380
        private const val AMPLITUDE = 0.07
        private const val STEPS = 0.001
        private const val MAX_STRENGTH = 30
    }

    private var reseting = false
    private var jumping = false
    private var jumped = false

    private var strengthApplied: Int = 0
    private var jumpStep: Int = 0

    private val top: Float
        get() = centerY * 2

    private val maxHeight: Float
        get() = 4 * top

    private val maxDistance: Float
        get() = 0.8f - ctrlPoints.filterIndexed { index, _ -> index % 2 == 0 }.max()!!

    fun pull() {
        if (strengthApplied < MAX_STRENGTH) {
            strengthApplied += 1
        }
    }

    private fun expand() {
        if (strengthApplied > 0) {
            strengthApplied -= 1
        } else {
            reseting = false
            jumping = true
        }
    }

    fun leave() {
        reseting = true
    }

    init {
        ctrlPoints = buildPoints()
        centerY = ctrlPoints.last() / 2
    }

    private fun buildPoints(): FloatArray {
        val vertices = mutableListOf<Float>()

        var periodDrawn = 0
        var x = 0.0
        var y = 0.0

        while (periodDrawn < NUM_ARCH) {
            vertices.add(x.toFloat())
            vertices.add(y.toFloat())

            x = AMPLITUDE * sin(y * COEFFICIENT)
            y += STEPS

            periodDrawn = floor(y / (2 * Math.PI / COEFFICIENT)).roundToInt()
        }

        return vertices.toFloatArray()
    }

    fun getTransformation(): Transformation {
        if (jumping) {
            return jump()
        }
        if (jumped) {
            return Translation(maxDistance * angleRotation.value.toFloat() * -1.0f, 0.0f)
        }
        return scale()
    }

    private fun jump(): Transformation {
        val nextAngle = getCurrentAngle()
        val radians = Math.toRadians(nextAngle)

        if (abs(nextAngle) == 180.0) {
            stopJumping()
        }

        val (shiftX, shiftY) = calculateShift(radians)

        return (Translation(shiftX, shiftY) + Rotation(0.0f, centerY, radians)).apply {
            jumpStep++
        }
    }

    private fun getCurrentAngle() = jumpStep * angleRotation.value

    private fun calculateShift(radians: Double): Pair<Float, Float> {
        val rising = cos(radians) in 0.0..1.0
        val middle = maxDistance / 2

        var (shiftX, shiftY) = if (rising) {
            Pair(
                middle * (1 - cos(radians)),
                maxHeight * abs(sin(radians))
            )
        } else {
            Pair(
                middle + (middle * abs(cos(radians))),
                maxHeight - maxHeight * (1 - sin((abs(radians))))
            )
        }

        if (jumpDirection == DIRECTION.LEFT) {
            shiftX *= -1.0f
        }

        return Pair(shiftX.toFloat(), shiftY.toFloat())
    }

    private fun stopJumping() {
        jumping = false
        jumpStep = 0
        jumped = true
    }

    private fun scale(): Transformation {
        if (reseting) {
            expand()
        }
        return shrink()
    }

    private fun shrink(): Transformation {
        return if (strengthApplied != 0) {
            Scale(1.0f, (MAX_STRENGTH - strengthApplied) / (MAX_STRENGTH * 1f))
        } else Transformation()
    }
}
