package com.example.ui.components

import android.opengl.Matrix
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.*

/**
 * 3D Vector Math representation for Android 3D graphics pipeline.
 */
data class Vec3(val x: Float, val y: Float, val z: Float) {
    fun length(): Float = sqrt(x * x + y * y + z * z)
    fun normalized(): Vec3 {
        val l = length().coerceAtLeast(0.0001f)
        return Vec3(x / l, y / l, z / l)
    }
    fun dot(other: Vec3): Float = x * other.x + y * other.y + z * other.z
    fun cross(other: Vec3): Vec3 = Vec3(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )
    operator fun plus(other: Vec3) = Vec3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vec3) = Vec3(x - other.x, y - other.y, z - other.z)
    operator fun times(scalar: Float) = Vec3(x * scalar, y * scalar, z * scalar)
}

/**
 * 3D Triangle Polygon with normal vector and depth sorting.
 */
data class Polygon3D(
    val v0: Vec3,
    val v1: Vec3,
    val v2: Vec3,
    val normal: Vec3,
    val avgZ: Float
)

/**
 * Lighting Rig parameters for the 3D Scene.
 */
data class SceneLighting(
    val ambientColor: Color = Color(0xFF281810),
    val ambientIntensity: Float = 0.55f,
    val keyLightDir: Vec3 = Vec3(0.577f, 0.707f, 0.577f).normalized(),
    val keyLightColor: Color = Color(0xFFFFF2DC),
    val keyLightIntensity: Float = 1.35f,
    val rimLightDir: Vec3 = Vec3(-0.707f, -0.4f, -0.707f).normalized(),
    val rimLightColor: Color = Color(0xFFE5A950),
    val rimLightIntensity: Float = 1.6f
)

/**
 * Procedural 3D Mesh Generator for the Placeholder Coffee Bean model.
 * Generates an ellipsoid geometry with bean aspect ratio (0.85, 1.35, 0.65).
 */
object BeanMeshGenerator {
    fun generatePlaceholderEllipsoid(
        rings: Int = 24,
        sectors: Int = 24,
        scaleX: Float = 0.85f,
        scaleY: Float = 1.35f,
        scaleZ: Float = 0.65f
    ): List<Polygon3D> {
        val vertices = ArrayList<Vec3>()
        val normals = ArrayList<Vec3>()

        val rStep = PI.toFloat() / rings
        val sStep = (2f * PI.toFloat()) / sectors

        for (r in 0..rings) {
            val phi = r * rStep
            val sinPhi = sin(phi)
            val cosPhi = cos(phi)

            for (s in 0..sectors) {
                val theta = s * sStep
                val sinTheta = sin(theta)
                val cosTheta = cos(theta)

                // Unit normal on sphere
                val nx = sinPhi * cosTheta
                val ny = cosPhi
                val nz = sinPhi * sinTheta
                normals.add(Vec3(nx, ny, nz).normalized())

                // Scaled position to coffee bean proportion
                val px = nx * scaleX
                val py = ny * scaleY
                val pz = nz * scaleZ
                vertices.add(Vec3(px, py, pz))
            }
        }

        val polygons = ArrayList<Polygon3D>()
        for (r in 0 until rings) {
            for (s in 0 until sectors) {
                val first = (r * (sectors + 1)) + s
                val second = first + sectors + 1

                val v0 = vertices[first]
                val v1 = vertices[second]
                val v2 = vertices[first + 1]
                val n0 = (normals[first] + normals[second] + normals[first + 1]) * (1f / 3f)
                val avgZ0 = (v0.z + v1.z + v2.z) / 3f
                polygons.add(Polygon3D(v0, v1, v2, n0.normalized(), avgZ0))

                val v3 = vertices[second]
                val v4 = vertices[second + 1]
                val v5 = vertices[first + 1]
                val n1 = (normals[second] + normals[second + 1] + normals[first + 1]) * (1f / 3f)
                val avgZ1 = (v3.z + v4.z + v5.z) / 3f
                polygons.add(Polygon3D(v3, v4, v5, n1.normalized(), avgZ1))
            }
        }
        return polygons
    }
}

/**
 * High-performance 3D Scene Component built for Android and Jetpack Compose.
 * Establishes base 3D canvas, perspective camera, Blinn-Phong lighting, and placeholder mesh.
 */
@Composable
fun CoffeeScene(
    modifier: Modifier = Modifier,
    lighting: SceneLighting = remember { SceneLighting() }
) {
    var rotX by remember { mutableFloatStateOf(15f) }
    var rotY by remember { mutableFloatStateOf(-25f) }
    var zoomScale by remember { mutableFloatStateOf(1.0f) }
    var showWireframe by remember { mutableStateOf(false) }

    // Idle floating & breathing animation
    val infiniteTransition = rememberInfiniteTransition(label = "sceneIdle")
    val idleRotY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "idleRotY"
    )
    val idleHoverY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "idleHoverY"
    )

    // Precomputed base 3D placeholder bean geometry
    val basePolygons = remember { BeanMeshGenerator.generatePlaceholderEllipsoid() }

    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = AuroraCard),
        border = BorderStroke(1.dp, AuroraGold.copy(alpha = 0.5f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("coffee_scene_3d_container")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Status & Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ViewInAr,
                            contentDescription = null,
                            tint = AuroraGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "3D COFFEE SCENE • ANDROID COMPATIBLE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = AuroraGold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Base Canvas, Lighting & Placeholder Mesh",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        ),
                        color = AuroraCream
                    )
                }

                // Controls (Wireframe toggle & reset rotation)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(
                        onClick = { showWireframe = !showWireframe },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (showWireframe) AuroraGold else AuroraCardElevated,
                            contentColor = if (showWireframe) AuroraBackground else AuroraCream
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (showWireframe) "SOLID" else "WIRE",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    IconButton(
                        onClick = {
                            rotX = 15f
                            rotY = -25f
                            zoomScale = 1.0f
                        },
                        modifier = Modifier
                            .background(AuroraCardElevated, CircleShape)
                            .border(1.dp, AuroraBorder, CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RotateRight,
                            contentDescription = "Reset Angle",
                            tint = AuroraGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3D Canvas Viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF281810),
                                Color(0xFF16100B),
                                Color(0xFF0C0806)
                            )
                        )
                    )
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            rotY += dragAmount.x * 0.45f
                            rotX = (rotX - dragAmount.y * 0.45f).coerceIn(-65f, 65f)
                        }
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val cx = w * 0.5f
                    val cy = h * 0.5f + idleHoverY

                    // Ground contact shadow
                    drawOval(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x99000000),
                                Color(0x44000000),
                                Color.Transparent
                            ),
                            center = Offset(cx, cy + h * 0.36f),
                            radius = w * 0.32f
                        ),
                        topLeft = Offset(cx - w * 0.32f, cy + h * 0.33f),
                        size = androidx.compose.ui.geometry.Size(w * 0.64f, 40f)
                    )

                    // 3D Model Transformation Matrix (Rotation & Scale)
                    val effectiveRotY = (rotY + idleRotY) * (PI.toFloat() / 180f)
                    val effectiveRotX = rotX * (PI.toFloat() / 180f)

                    val cosY = cos(effectiveRotY)
                    val sinY = sin(effectiveRotY)
                    val cosX = cos(effectiveRotX)
                    val sinX = sin(effectiveRotX)

                    val fovFactor = 4.2f
                    val baseScale = (min(w, h) * 0.28f) * zoomScale

                    // Rotate, project, and light the polygons
                    val transformed = basePolygons.map { poly ->
                        val rV0 = rotateVertex(poly.v0, cosY, sinY, cosX, sinX)
                        val rV1 = rotateVertex(poly.v1, cosY, sinY, cosX, sinX)
                        val rV2 = rotateVertex(poly.v2, cosY, sinY, cosX, sinX)
                        val rNorm = rotateVertex(poly.normal, cosY, sinY, cosX, sinX).normalized()

                        val avgZ = (rV0.z + rV1.z + rV2.z) / 3f

                        val p0 = project(rV0, cx, cy, baseScale, fovFactor)
                        val p1 = project(rV1, cx, cy, baseScale, fovFactor)
                        val p2 = project(rV2, cx, cy, baseScale, fovFactor)

                        ProjectedPoly(p0, p1, p2, rNorm, avgZ)
                    }

                    // Depth Sorting (Painter's algorithm - back to front)
                    val sorted = transformed.sortedBy { it.depth }

                    // Render polygons with lighting
                    val baseRoastColor = Color(0xFF381D0F)

                    sorted.forEach { poly ->
                        // Backface culling (if normal points away from camera, z <= 0)
                        if (poly.normal.z > -0.1f) {
                            // Blinn-Phong Shading computation
                            // 1. Ambient component
                            var r = baseRoastColor.red * lighting.ambientIntensity
                            var g = baseRoastColor.green * lighting.ambientIntensity
                            var b = baseRoastColor.blue * lighting.ambientIntensity

                            // 2. Diffuse Key Light (Lambertian)
                            val nDotL = poly.normal.dot(lighting.keyLightDir).coerceAtLeast(0f)
                            r += lighting.keyLightColor.red * nDotL * lighting.keyLightIntensity * 0.6f
                            g += lighting.keyLightColor.green * nDotL * lighting.keyLightIntensity * 0.6f
                            b += lighting.keyLightColor.blue * nDotL * lighting.keyLightIntensity * 0.6f

                            // 3. Specular Key Highlight (Blinn-Phong reflection)
                            val viewDir = Vec3(0f, 0f, 1f)
                            val halfVec = (lighting.keyLightDir + viewDir).normalized()
                            val nDotH = poly.normal.dot(halfVec).coerceAtLeast(0f)
                            val specular = nDotH.pow(16f) * 0.45f
                            r += specular
                            g += specular * 0.9f
                            b += specular * 0.7f

                            // 4. Rim light (Silhouette contour glow)
                            val rim = (1f - poly.normal.dot(viewDir).coerceIn(0f, 1f)).pow(3f)
                            val rimFactor = rim * lighting.rimLightIntensity * 0.35f
                            r += lighting.rimLightColor.red * rimFactor
                            g += lighting.rimLightColor.green * rimFactor
                            b += lighting.rimLightColor.blue * rimFactor

                            val finalColor = Color(
                                red = r.coerceIn(0f, 1f),
                                green = g.coerceIn(0f, 1f),
                                blue = b.coerceIn(0f, 1f),
                                alpha = 1.0f
                            )

                            val path = Path().apply {
                                moveTo(poly.p0.x, poly.p0.y)
                                lineTo(poly.p1.x, poly.p1.y)
                                lineTo(poly.p2.x, poly.p2.y)
                                close()
                            }

                            if (showWireframe) {
                                drawPath(
                                    path = path,
                                    color = AuroraGold.copy(alpha = 0.75f),
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.2f)
                                )
                            } else {
                                drawPath(path = path, color = finalColor)
                                // Subtle wireline definition
                                drawPath(
                                    path = path,
                                    color = Color(0x18000000),
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 0.8f)
                                )
                            }
                        }
                    }

                    // Ambient floating lighting embers
                    for (i in 0 until 18) {
                        val angle = (i * 20f + effectiveRotY * 57.3f * 0.3f) * (PI.toFloat() / 180f)
                        val dist = w * 0.34f + sin(i * 1.5f) * 20f
                        val px = cx + cos(angle) * dist
                        val py = cy + sin(angle) * (dist * 0.5f)
                        drawCircle(
                            color = AuroraGold.copy(alpha = 0.5f),
                            radius = 2.0f,
                            center = Offset(px, py)
                        )
                    }
                }

                // Interactive drag guidance overlay
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AuroraBackground.copy(alpha = 0.8f),
                    border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "TOUCH & DRAG TO ORBIT 3D SCENE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.5.sp,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = AuroraGold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scene Specification Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SceneParamBadge(
                    label = "CANVAS",
                    value = "PERSPECTIVE",
                    sub = "4.2 fov • 60 FPS",
                    modifier = Modifier.weight(1f)
                )
                SceneParamBadge(
                    label = "LIGHTING",
                    value = "BLINN-PHONG",
                    sub = "Key + Rim + Amb",
                    modifier = Modifier.weight(1f)
                )
                SceneParamBadge(
                    label = "MESH",
                    value = "ELLIPSOID",
                    sub = "Bean Proportion",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private data class ProjectedPoly(
    val p0: Offset,
    val p1: Offset,
    val p2: Offset,
    val normal: Vec3,
    val depth: Float
)

private fun rotateVertex(v: Vec3, cosY: Float, sinY: Float, cosX: Float, sinX: Float): Vec3 {
    // 1. Rotate around Y axis (Yaw)
    val x1 = v.x * cosY + v.z * sinY
    val y1 = v.y
    val z1 = -v.x * sinY + v.z * cosY

    // 2. Rotate around X axis (Pitch)
    val x2 = x1
    val y2 = y1 * cosX - z1 * sinX
    val z2 = y1 * sinX + z1 * cosX

    return Vec3(x2, y2, z2)
}

private fun project(v: Vec3, cx: Float, cy: Float, scale: Float, fovFactor: Float): Offset {
    val zDist = v.z + fovFactor
    val pScale = scale / zDist.coerceAtLeast(0.5f)
    return Offset(
        x = cx + v.x * pScale,
        y = cy - v.y * pScale
    )
}

@Composable
private fun SceneParamBadge(
    label: String,
    value: String,
    sub: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = AuroraSurfaceVariant.copy(alpha = 0.85f),
        border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.5.sp,
                    fontSize = 9.sp
                ),
                color = AuroraGold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = AuroraCream
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = sub,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 9.sp,
                    color = AuroraMuted
                )
            )
        }
    }
}
