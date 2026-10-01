package com.example.ui.components

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.*
import com.example.ui.util.Bean3DTransform
import com.example.ui.util.Scroll3DTracker

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ThreeJsCoffeeBean3D(
    isSplit: Boolean,
    onToggleSplit: () -> Unit,
    transform: Bean3DTransform = Scroll3DTracker.calculateTransform(0),
    modifier: Modifier = Modifier
) {
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    // Synchronize scroll-mapped 3D transformations with Three.js scene
    LaunchedEffect(transform, isSplit) {
        val effectiveSplit = if (isSplit) 1.0f else transform.expansionProgress
        webViewRef?.evaluateJavascript(
            "if (window.setScrollTransform) { window.setScrollTransform(${transform.rotationYDegrees}, ${transform.rotationXDegrees}, $effectiveSplit, ${transform.cameraDistance}); }",
            null
        )
    }

    val threeJsHtml = remember {
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
            <style>
                body { margin: 0; padding: 0; overflow: hidden; background-color: #16100B; }
                #canvas-container { width: 100vw; height: 100vh; display: block; }
            </style>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/three.js/r128/three.min.js"></script>
        </head>
        <body>
            <div id="canvas-container"></div>
            <script>
                let scene, camera, renderer, leftHalf, rightHalf, particleGroup;
                let isSplit = false;
                let splitAmount = 0;
                let targetSplit = 0;
                let targetRotY = 0;
                let targetRotX = 0;
                let scrollProgress = 0;
                let isDragging = false;
                let previousMousePosition = { x: 0, y: 0 };

                function init() {
                    const container = document.getElementById('canvas-container');
                    const width = window.innerWidth;
                    const height = window.innerHeight;

                    // 1. Scene setup
                    scene = new THREE.Scene();
                    scene.background = new THREE.Color(0x16100B);
                    scene.fog = new THREE.FogExp2(0x16100B, 0.05);

                    // 2. Camera setup
                    camera = new THREE.PerspectiveCamera(45, width / height, 0.1, 100);
                    camera.position.set(0, 0, 4.5);

                    // 3. Renderer setup
                    renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
                    renderer.setSize(width, height);
                    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
                    renderer.shadowMap.enabled = true;
                    renderer.toneMapping = THREE.ACESFilmicToneMapping;
                    container.appendChild(renderer.domElement);

                    // 4. Lighting setup (Cinematic Luxury Lighting)
                    const ambientLight = new THREE.AmbientLight(0xd4af37, 0.8);
                    scene.add(ambientLight);

                    const keyLight = new THREE.DirectionalLight(0xffeedd, 2.0);
                    keyLight.position.set(3, 4, 3);
                    scene.add(keyLight);

                    const rimLight = new THREE.PointLight(0xe5a950, 3.5, 10);
                    rimLight.position.set(-3, -2, -2);
                    scene.add(rimLight);

                    const coreGlow = new THREE.PointLight(0xf3c06b, 0, 5);
                    coreGlow.position.set(0, 0, 0);
                    scene.add(coreGlow);
                    window.coreGlow = coreGlow;

                    // 5. 3D Coffee Bean Geometry Construction
                    createCoffeeBean();

                    // 6. Floating Particles
                    createParticles();

                    // 7. Event Listeners for Touch/Mouse Drag Interaction
                    container.addEventListener('touchstart', onTouchStart, { passive: false });
                    container.addEventListener('touchmove', onTouchMove, { passive: false });
                    container.addEventListener('touchend', onTouchEnd);
                    window.addEventListener('resize', onWindowResize);

                    animate();
                }

                function createCoffeeBean() {
                    // Procedural Canvas Textures
                    const outerTexCanvas = document.createElement('canvas');
                    outerTexCanvas.width = 512;
                    outerTexCanvas.height = 512;
                    const oCtx = outerTexCanvas.getContext('2d');
                    const oGrad = oCtx.createRadialGradient(256, 256, 50, 256, 256, 256);
                    oGrad.addColorStop(0, '#4a2815');
                    oGrad.addColorStop(0.5, '#30180c');
                    oGrad.addColorStop(1, '#150803');
                    oCtx.fillStyle = oGrad;
                    oCtx.fillRect(0, 0, 512, 512);
                    for (let i = 0; i < 1500; i++) {
                        oCtx.fillStyle = Math.random() > 0.5 ? 'rgba(92, 51, 26, 0.15)' : 'rgba(10, 4, 2, 0.2)';
                        oCtx.beginPath();
                        oCtx.arc(Math.random() * 512, Math.random() * 512, Math.random() * 2, 0, Math.PI * 2);
                        oCtx.fill();
                    }
                    const outerMap = new THREE.CanvasTexture(outerTexCanvas);

                    const inTexCanvas = document.createElement('canvas');
                    inTexCanvas.width = 512;
                    inTexCanvas.height = 512;
                    const iCtx = inTexCanvas.getContext('2d');
                    const iGrad = iCtx.createRadialGradient(256, 256, 20, 256, 256, 250);
                    iGrad.addColorStop(0, '#e5a950');
                    iGrad.addColorStop(0.3, '#c68038');
                    iGrad.addColorStop(0.7, '#6b3616');
                    iGrad.addColorStop(1, '#2b1207');
                    iCtx.fillStyle = iGrad;
                    iCtx.fillRect(0, 0, 512, 512);

                    // Concentric roasting cell rings
                    iCtx.strokeStyle = 'rgba(243, 192, 107, 0.3)';
                    iCtx.lineWidth = 2;
                    for (let r = 40; r < 230; r += 25) {
                        iCtx.beginPath();
                        iCtx.ellipse(256, 256, r * 0.7, r * 1.15, 0, 0, Math.PI * 2);
                        iCtx.stroke();
                    }

                    // Vascular radial lines
                    iCtx.strokeStyle = 'rgba(212, 175, 55, 0.22)';
                    iCtx.lineWidth = 1.2;
                    for (let a = 0; a < Math.PI * 2; a += Math.PI / 14) {
                        iCtx.beginPath();
                        iCtx.moveTo(256 + Math.cos(a) * 20, 256 + Math.sin(a) * 20);
                        iCtx.lineTo(256 + Math.cos(a) * 220, 256 + Math.sin(a) * 220);
                        iCtx.stroke();
                    }
                    const interiorMap = new THREE.CanvasTexture(inTexCanvas);

                    // Parametric ellipsoid bean geometry
                    const geometry = new THREE.SphereGeometry(1, 36, 36);
                    geometry.scale(0.85, 1.35, 0.65);

                    const pos = geometry.attributes.position;
                    for (let i = 0; i < pos.count; i++) {
                        let x = pos.getX(i);
                        let y = pos.getY(i);
                        let z = pos.getZ(i);

                        if (z < 0) z *= 0.7;

                        const creaseDist = Math.abs(x);
                        if (creaseDist < 0.25 && z > 0) {
                            z -= (0.25 - creaseDist) * 0.8;
                        }

                        pos.setXYZ(i, x, y, z);
                    }
                    geometry.computeVertexNormals();

                    // Outer Roasted Bean Material with procedural texture
                    const beanMaterial = new THREE.MeshStandardMaterial({
                        map: outerMap,
                        color: 0x3d2212,
                        roughness: 0.35,
                        metalness: 0.15
                    });

                    // Interior Cross-Section Material with roasted core texture
                    const interiorMaterial = new THREE.MeshStandardMaterial({
                        map: interiorMap,
                        color: 0x8a5329,
                        roughness: 0.55,
                        metalness: 0.05,
                        emissive: 0x3d1c06,
                        emissiveIntensity: 0.3
                    });

                    // Create composite groups for left and right halves
                    leftHalf = new THREE.Group();
                    const leftMesh = new THREE.Mesh(geometry.clone(), beanMaterial);
                    leftHalf.add(leftMesh);

                    const leftInterior = new THREE.Mesh(new THREE.PlaneGeometry(0.8, 1.6), interiorMaterial);
                    leftInterior.rotation.y = Math.PI / 2;
                    leftInterior.position.x = 0.02;
                    leftHalf.add(leftInterior);
                    leftHalf.position.x = -0.05;

                    rightHalf = new THREE.Group();
                    const rightMesh = new THREE.Mesh(geometry.clone(), beanMaterial);
                    rightHalf.add(rightMesh);

                    const rightInterior = new THREE.Mesh(new THREE.PlaneGeometry(0.8, 1.6), interiorMaterial);
                    rightInterior.rotation.y = -Math.PI / 2;
                    rightInterior.position.x = -0.02;
                    rightHalf.add(rightInterior);
                    rightHalf.position.x = 0.05;

                    scene.add(leftHalf);
                    scene.add(rightHalf);
                }

                function createParticles() {
                    particleGroup = new THREE.Group();
                    const pGeo = new THREE.SphereGeometry(0.02, 8, 8);
                    const pMat = new THREE.MeshBasicMaterial({ color: 0xd4af37, transparent: true, opacity: 0.6 });

                    for (let i = 0; i < 30; i++) {
                        const p = new THREE.Mesh(pGeo, pMat);
                        p.position.set(
                            (Math.random() - 0.5) * 5,
                            (Math.random() - 0.5) * 5,
                            (Math.random() - 0.5) * 3
                        );
                        particleGroup.add(p);
                    }
                    scene.add(particleGroup);
                }

                function onTouchStart(e) {
                    if (e.touches.length === 1) {
                        isDragging = true;
                        previousMousePosition = {
                            x: e.touches[0].clientX,
                            y: e.touches[0].clientY
                        };
                    }
                }

                function onTouchMove(e) {
                    if (!isDragging || e.touches.length !== 1) return;
                    const deltaX = e.touches[0].clientX - previousMousePosition.x;
                    const deltaY = e.touches[0].clientY - previousMousePosition.y;

                    targetRotY += deltaX * 0.01;
                    targetRotX += deltaY * 0.01;

                    previousMousePosition = {
                        x: e.touches[0].clientX,
                        y: e.touches[0].clientY
                    };
                }

                function onTouchEnd() {
                    isDragging = false;
                }

                function onWindowResize() {
                    const width = window.innerWidth;
                    const height = window.innerHeight;
                    camera.aspect = width / height;
                    camera.updateProjectionMatrix();
                    renderer.setSize(width, height);
                }

                // Public bridges
                window.setScrollTransform = function(rotYDeg, rotXDeg, expansion, cameraZ) {
                    targetRotY = (rotYDeg * Math.PI) / 180;
                    targetRotX = (rotXDeg * Math.PI) / 180;
                    targetSplit = expansion * 0.75;
                    if (camera && cameraZ) {
                        camera.position.z = cameraZ;
                    }
                };

                window.setSplitState = function(split) {
                    isSplit = split;
                    targetSplit = split ? 0.75 : 0;
                };

                window.setScrollProgress = function(progress) {
                    scrollProgress = progress;
                };

                function animate() {
                    requestAnimationFrame(animate);

                    // Smooth spring interpolation for split
                    splitAmount += (targetSplit - splitAmount) * 0.08;
                    leftHalf.position.x = -splitAmount - 0.05;
                    rightHalf.position.x = splitAmount + 0.05;

                    // Dynamic core glow when split
                    if (window.coreGlow) {
                        window.coreGlow.intensity = splitAmount * 4;
                    }

                    // Camera and bean rotation
                    const idleTime = Date.now() * 0.001;
                    const baseRotY = targetRotY + (isDragging ? 0 : idleTime * 0.4) + scrollProgress * Math.PI;
                    const baseRotX = targetRotX + Math.sin(idleTime * 0.8) * 0.1;

                    leftHalf.rotation.y = baseRotY;
                    leftHalf.rotation.x = baseRotX;
                    rightHalf.rotation.y = baseRotY;
                    rightHalf.rotation.x = baseRotX;

                    // Idle floating bob
                    const floatY = Math.sin(idleTime * 1.5) * 0.08;
                    leftHalf.position.y = floatY;
                    rightHalf.position.y = floatY;

                    // Orbiting particles
                    if (particleGroup) {
                        particleGroup.rotation.y = idleTime * 0.15;
                    }

                    renderer.render(scene, camera);
                }

                window.onload = init;
            </script>
        </body>
        </html>
        """.trimIndent()
    }

    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = AuroraCard),
        border = BorderStroke(1.dp, AuroraGold.copy(alpha = 0.5f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("threejs_r3f_coffee_bean_container")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
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
                            text = "WEBGL THREE.JS / R3F SCENE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = AuroraGold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Scroll & Touch 3D Camera",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        ),
                        color = AuroraCream
                    )
                }

                // Interactive Buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(
                        onClick = onToggleSplit,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (isSplit) AuroraGold else AuroraCardElevated,
                            contentColor = if (isSplit) AuroraBackground else AuroraCream
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("three_split_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallSplit,
                            contentDescription = "Split",
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isSplit) "MERGE" else "SPLIT",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    IconButton(
                        onClick = {
                            webViewRef?.evaluateJavascript("targetRotY += Math.PI / 2;", null)
                        },
                        modifier = Modifier
                            .background(AuroraCardElevated, CircleShape)
                            .border(1.dp, AuroraBorder, CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RotateRight,
                            contentDescription = "Rotate",
                            tint = AuroraGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3D Three.js WebGL Stage
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(310.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF16100B))
            ) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            settings.cacheMode = WebSettings.LOAD_NO_CACHE
                            setBackgroundColor(0xFF16100B.toInt())
                            webViewClient = WebViewClient()
                            webChromeClient = WebChromeClient()
                            loadDataWithBaseURL("https://threejs.org", threeJsHtml, "text/html", "UTF-8", null)
                            webViewRef = this
                        }
                    },
                    update = { view ->
                        webViewRef = view
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Drag gesture overlay label
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AuroraBackground.copy(alpha = 0.75f),
                    border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp)
                ) {
                    Text(
                        text = "TOUCH & DRAG TO ORBIT 3D CAMERA",
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

            Spacer(modifier = Modifier.height(18.dp))

            // Floating Information Badges (Strictly matching prompt: ORIGIN (ETHIOPIA), ROAST (MEDIUM), NOTES (CHOCOLATE, CARAMEL, BERRY))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ThreeSpecBadge(
                    label = "ORIGIN",
                    value = "ETHIOPIA",
                    subValue = "Yirgacheffe • 2,150 MASL",
                    modifier = Modifier.weight(1f)
                )
                ThreeSpecBadge(
                    label = "ROAST",
                    value = "MEDIUM",
                    subValue = "Caramelized 208°C",
                    modifier = Modifier.weight(1f)
                )
                ThreeSpecBadge(
                    label = "NOTES",
                    value = "CHOCOLATE",
                    subValue = "Caramel • Berry",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ThreeSpecBadge(
    label: String,
    value: String,
    subValue: String,
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
                text = subValue,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 9.sp,
                    color = AuroraMuted
                ),
                maxLines = 1
            )
        }
    }
}
