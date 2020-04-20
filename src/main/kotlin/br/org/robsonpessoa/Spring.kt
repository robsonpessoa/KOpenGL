package br.org.robsonpessoa

import br.org.robsonpessoa.transformation.Rotation
import br.org.robsonpessoa.transformation.Scale
import br.org.robsonpessoa.transformation.Transformation
import br.org.robsonpessoa.transformation.Translation
import kotlin.math.*
import kotlin.random.Random

/**
 * Represents a Spring object.
 *
 */
class Spring {

    val ctrlPoints: FloatArray

    init {
        ctrlPoints = buildPoints()
    }

    companion object {
        private const val NUM_ARCH = 10
        private const val COEFFICIENT = 380
        private const val AMPLITUDE = 0.07
        private const val STEPS = 0.001
        private const val MAX_STRENGTH = 30
        private const val MAX_DISTANCE = 0.8f
        private const val MAX_HEIGHT = 4
    }

    private enum class DIRECTION {
        LEFT,
        RIGHT
    }

    private enum class State {
        INITIAL,
        APPLYING_STRENGTH,
        LEAVING_STRENGTH,
        JUMPING,
        FINAL;

        fun nextState(): State {
            if (this == FINAL) return FINAL
            return values()[this.ordinal + 1]
        }
    }

    private val jumpDirection = DIRECTION.values()[Random.nextInt(0, 2)]
    private val angleRotation: Lazy<Double>
        get() = lazy { if (jumpDirection == DIRECTION.LEFT) 1.0 else -1.0 }

    private var currentState = State.INITIAL

    private var strengthApplied: Int = 0
    private var jumpStep: Int = 0

    /**
     * Gets the max value of the object in relation to the y coordinate.
     */
    private val top: Float
        get() = ctrlPoints.last()

    /**
     * Gets the max value of the object in relation to the x coordinate.
     */
    private val left: Float
        get() = ctrlPoints.filterIndexed { index, _ -> index % 2 == 0 }.max()!!

    /**
     * Gets the center params (x, y) of the Spring object.
     */
    private val center: Pair<Float, Float>
        get() = Pair(0.0f, top / 2)

    /**
     * Gets the max value of the Spring in relation to coordinate y while jumping.
     */
    private val maxHeight: Float
        get() = MAX_HEIGHT * top

    /**
     * Gets the max value of the Spring in relation to coordinate x while jumping.
     */
    private val maxDistance: Float
        get() = MAX_DISTANCE - left

    /**
     * Create the points (x, y) that represents the Spring.
     *
     * @return the array of points.
     */
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

    /**
     * Changes the state of the transformation to the next state allowed according to Spring [State] enum.
     *
     */
    private fun nextState() {
        currentState = currentState.nextState()
    }

    /**
     * Apply a strength at the top of the Spring.
     *
     */
    fun pull() {
        if (currentState == State.INITIAL) {
            nextState()
        }
        if (strengthApplied < MAX_STRENGTH) {
            strengthApplied += 1
        }
    }

    /**
     * Leaves the Spring after a strength applied to the object changing the state of the object.
     *
     */
    fun leave() {
        nextState()
    }

    /**
     * Gets the transformation that represents the current state of the Spring.
     *
     * @return the [Transformation].
     */
    fun getTransformation(): Transformation = when (currentState) {
        State.INITIAL -> Transformation()
        State.APPLYING_STRENGTH -> shrink()
        State.LEAVING_STRENGTH -> expand()
        State.JUMPING -> jump()
        State.FINAL -> Translation(maxDistance * angleRotation.value.toFloat() * -1.0f, 0.0f)
    }

    /**
     * Builds the Transformation that represents the Jumping of the Spring.
     *
     * @return the movement [Transformation].
     */
    private fun jump(): Transformation {
        val nextAngle = getCurrentAngle()
        val radians = Math.toRadians(nextAngle)

        if (abs(nextAngle) == 180.0) {
            nextState()
        }

        val (shiftX, shiftY) = calculateShift(radians)

        return (Translation(shiftX, shiftY) + Rotation(center.first, center.second, radians)).apply {
            jumpStep++
        }
    }

    /**
     * Gets the current angle of rotation according to the jump step.
     *
     * @return the current Angle in degrees.
     */
    private fun getCurrentAngle() = jumpStep * angleRotation.value

    /**
     * To simulate a movement of the object, this method calculates the params (x,y)
     * of the [Translation] transformation according to the angle in radians of the rotation.
     *
     * @param radians the angle in radians.
     * @return the params (x, y).
     */
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

    /**
     * Builds the Scale transformation according to the strength applied to the object until now.
     *
     * @return the [Scale] Transformation.
     */
    private fun shrink(): Transformation {
        return scale(strengthApplied)
    }

    /**
     * Builds the Scale transformation according to the strength applied to the object
     * after leaving an unit of the strength applied.
     *
     * @return the [Scale] Transformation.
     */
    private fun expand(): Transformation {
        return scale(leaveStrength())
    }

    /**
     * Applies a subtract to the strength applied to the object until it gets zero.
     * If there is no strength to the object anymore, then changes the state to the next one ([State.JUMPING]).
     *
     * @return the strength unit after leaving one unit
     */
    private fun leaveStrength(): Int {
        if (strengthApplied > 0) {
            strengthApplied -= 1
        } else {
            nextState()
        }
        return strengthApplied
    }

    /**
     * Builds the Scale transformation according to the strength applied to the object
     *
     * @return the [Scale] Transformation.
     */
    private fun scale(strength: Int) = Scale(1.0f, (MAX_STRENGTH - strength) / (MAX_STRENGTH * 1f))
}
