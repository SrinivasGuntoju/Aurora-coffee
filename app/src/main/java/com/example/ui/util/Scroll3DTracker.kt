package com.example.ui.util

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.*
import kotlin.math.*

/**
 * Mathematical transformation values mapped from scroll position for the 3D Coffee Bean model.
 */
data class Bean3DTransform(
    val normalizedProgress: Float, // 0.0 to 1.0 within the active section
    val rotationYDegrees: Float,   // Continuous rotation around Y axis (e.g. 0 to 360+)
    val rotationXDegrees: Float,   // Pitch tilt (-15° to +25°)
    val rotationZDegrees: Float,   // Subtle dynamic banking tilt
    val expansionProgress: Float,  // 0.0 (merged) to 1.0 (fully split open)
    val cameraDistance: Float,     // Camera Z distance (zooming in during split)
    val coreGlowIntensity: Float,  // Glowing embers intensity at the core (0.0 to 1.0)
    val parallaxOffsetY: Float     // Background floating parallax displacement in DP
)

/**
 * Scroll progress tracking and mapping utility for 3D viewport scenes.
 */
object Scroll3DTracker {

    /**
     * Maps scroll coordinates to precision 3D rotation, expansion, and camera parameters.
     */
    fun calculateTransform(
        scrollY: Int,
        sectionTriggerStart: Int = 400,
        sectionTriggerEnd: Int = 1600
    ): Bean3DTransform {
        val totalRange = (sectionTriggerEnd - sectionTriggerStart).coerceAtLeast(1)
        val rawProgress = ((scrollY - sectionTriggerStart).toFloat() / totalRange).coerceIn(0f, 1f)

        // Smooth cubic ease-in-out curve to prevent abrupt transitions
        val smoothProgress = smoothStep(0f, 1f, rawProgress)

        // 1. Rotation Y: Complete 360° fluid spin tied to scroll depth
        val rotY = smoothProgress * 360f

        // 2. Rotation X: Natural perspective pitch as user scrolls past the bean
        // Starts tilted slightly up (+18°), levels at center (0°), tilts down (-12°)
        val rotX = (1f - smoothProgress * 2f) * 15f

        // 3. Rotation Z: Gentle banking sway
        val rotZ = sin(smoothProgress * PI.toFloat() * 2f) * 4f

        // 4. Expansion / Split Amount:
        // Closed at start (0.0 to 0.25)
        // Splits open dynamically in the prime viewing zone (0.25 to 0.75)
        // Merges back as user transitions to next section (0.75 to 1.0)
        val expansion = when {
            rawProgress < 0.20f -> 0f
            rawProgress in 0.20f..0.50f -> {
                val t = (rawProgress - 0.20f) / 0.30f
                smoothStep(0f, 1f, t)
            }
            rawProgress in 0.50f..0.80f -> 1.0f
            else -> {
                val t = (rawProgress - 0.80f) / 0.20f
                1.0f - smoothStep(0f, 1f, t)
            }
        }

        // 5. Camera Distance: Zooms slightly closer when bean splits open
        // 4.5f (resting distance) -> 3.6f (macro view) -> 4.5f
        val cameraZ = 4.5f - (expansion * 0.9f)

        // 6. Core Glow: Flares up as the bean splits and reveals the roasted core
        val glow = (expansion * 0.95f).coerceIn(0f, 1f)

        // 7. Parallax offset for floating background particles and tags
        val parallax = (rawProgress - 0.5f) * -60f

        return Bean3DTransform(
            normalizedProgress = smoothProgress,
            rotationYDegrees = rotY,
            rotationXDegrees = rotX,
            rotationZDegrees = rotZ,
            expansionProgress = expansion,
            cameraDistance = cameraZ,
            coreGlowIntensity = glow,
            parallaxOffsetY = parallax
        )
    }

    /**
     * GLSL-style smoothstep interpolation: 3x^2 - 2x^3
     */
    private fun smoothStep(edge0: Float, edge1: Float, x: Float): Float {
        val t = ((x - edge0) / (edge1 - edge0)).coerceIn(0f, 1f)
        return t * t * (3f - 2f * t)
    }
}

/**
 * Composable state tracker that recalculates 3D transforms as the user scrolls.
 */
@Composable
fun rememberBean3DTransform(
    scrollState: ScrollState,
    sectionTriggerStart: Int = 400,
    sectionTriggerEnd: Int = 1600
): State<Bean3DTransform> {
    return remember(scrollState.value, sectionTriggerStart, sectionTriggerEnd) {
        derivedStateOf {
            Scroll3DTracker.calculateTransform(
                scrollY = scrollState.value,
                sectionTriggerStart = sectionTriggerStart,
                sectionTriggerEnd = sectionTriggerEnd
            )
        }
    }
}
