import * as THREE from 'three';

/**
 * AURORA COFFEE - High-Quality 3D Coffee Bean Asset Generator
 * Optimized for Real-Time Rendering in React Three Fiber & WebGL.
 *
 * Features:
 * - Anatomically accurate dual-half bean geometries (Dorsal curve & Ventral groove).
 * - Procedural PBR Textures (BaseColor, Bump/Normal, Roughness, Emissive Roasting Core).
 * - Cellular endosperm cross-section with vascular radial striations.
 * - MeshPhysicalMaterial with oily sheen & clearcoat reflections.
 */

export interface CoffeeBean3DAssets {
  leftGeometry: THREE.BufferGeometry;
  rightGeometry: THREE.BufferGeometry;
  outerMaterial: THREE.MeshPhysicalMaterial;
  interiorMaterial: THREE.MeshPhysicalMaterial;
  coreGlowMaterial: THREE.MeshBasicMaterial;
  textures: {
    outerColor: THREE.CanvasTexture;
    outerBump: THREE.CanvasTexture;
    outerRoughness: THREE.CanvasTexture;
    interiorColor: THREE.CanvasTexture;
    interiorBump: THREE.CanvasTexture;
  };
}

/**
 * Generates procedural canvas textures for real-time PBR shading with zero external network latency.
 */
export function generateBeanPbrTextures(): CoffeeBean3DAssets['textures'] {
  // 1. Outer Roasted Bean Base Color Map (Rich espresso, dark chocolate & mahogany)
  const outerCanvas = document.createElement('canvas');
  outerCanvas.width = 1024;
  outerCanvas.height = 1024;
  const ctxOut = outerCanvas.getContext('2d')!;

  const outerGrad = ctxOut.createRadialGradient(512, 512, 100, 512, 512, 512);
  outerGrad.addColorStop(0, '#4a2815');
  outerGrad.addColorStop(0.5, '#30180c');
  outerGrad.addColorStop(0.85, '#1e0c05');
  outerGrad.addColorStop(1, '#110602');
  ctxOut.fillStyle = outerGrad;
  ctxOut.fillRect(0, 0, 1024, 1024);

  // Micro surface stippling for organic roasted texture
  for (let i = 0; i < 4000; i++) {
    const x = Math.random() * 1024;
    const y = Math.random() * 1024;
    const r = Math.random() * 2 + 0.5;
    ctxOut.fillStyle = Math.random() > 0.5 ? 'rgba(92, 51, 26, 0.15)' : 'rgba(12, 5, 2, 0.2)';
    ctxOut.beginPath();
    ctxOut.arc(x, y, r, 0, Math.PI * 2);
    ctxOut.fill();
  }

  // 2. Outer Bump / Height Map
  const bumpCanvas = document.createElement('canvas');
  bumpCanvas.width = 512;
  bumpCanvas.height = 512;
  const ctxBump = bumpCanvas.getContext('2d')!;
  ctxBump.fillStyle = '#808080';
  ctxBump.fillRect(0, 0, 512, 512);
  for (let i = 0; i < 2500; i++) {
    const x = Math.random() * 512;
    const y = Math.random() * 512;
    const rad = Math.random() * 3 + 1;
    ctxBump.fillStyle = Math.random() > 0.5 ? 'rgba(255, 255, 255, 0.12)' : 'rgba(0, 0, 0, 0.12)';
    ctxBump.beginPath();
    ctxBump.arc(x, y, rad, 0, Math.PI * 2);
    ctxBump.fill();
  }

  // 3. Outer Roughness Map (Freshly roasted bean oil sheen variations)
  const roughCanvas = document.createElement('canvas');
  roughCanvas.width = 512;
  roughCanvas.height = 512;
  const ctxRough = roughCanvas.getContext('2d')!;
  ctxRough.fillStyle = '#555555'; // semi-glossy base
  ctxRough.fillRect(0, 0, 512, 512);
  for (let i = 0; i < 600; i++) {
    const x = Math.random() * 512;
    const y = Math.random() * 512;
    const rad = Math.random() * 30 + 10;
    const grad = ctxRough.createRadialGradient(x, y, 0, x, y, rad);
    grad.addColorStop(0, 'rgba(40, 40, 40, 0.4)'); // oil sheen highlight
    grad.addColorStop(1, 'rgba(90, 90, 90, 0)');
    ctxRough.fillStyle = grad;
    ctxRough.beginPath();
    ctxRough.arc(x, y, rad, 0, Math.PI * 2);
    ctxRough.fill();
  }

  // 4. Split-Open Internal Endosperm Texture Map
  const inCanvas = document.createElement('canvas');
  inCanvas.width = 1024;
  inCanvas.height = 1024;
  const ctxIn = inCanvas.getContext('2d')!;

  // Warm roasting nucleus gradient
  const inGrad = ctxIn.createRadialGradient(512, 512, 50, 512, 512, 500);
  inGrad.addColorStop(0, '#e5a950'); // Glowing core caramelization
  inGrad.addColorStop(0.25, '#c68038');
  inGrad.addColorStop(0.6, '#86471e');
  inGrad.addColorStop(0.85, '#4e240f');
  inGrad.addColorStop(1, '#240d04');
  ctxIn.fillStyle = inGrad;
  ctxIn.fillRect(0, 0, 1024, 1024);

  // Concentric cellular roasting rings & embryo cavity
  ctxIn.strokeStyle = 'rgba(243, 192, 107, 0.25)';
  ctxIn.lineWidth = 3;
  for (let r = 80; r < 460; r += 35) {
    ctxIn.beginPath();
    ctxIn.ellipse(512, 512, r * 0.7, r * 1.15, 0, 0, Math.PI * 2);
    ctxIn.stroke();
  }

  // Radial vascular striations extending outward from the bean core
  ctxIn.strokeStyle = 'rgba(212, 175, 55, 0.2)';
  ctxIn.lineWidth = 1.5;
  for (let a = 0; a < Math.PI * 2; a += Math.PI / 18) {
    ctxIn.beginPath();
    ctxIn.moveTo(512 + Math.cos(a) * 40, 512 + Math.sin(a) * 40);
    ctxIn.lineTo(512 + Math.cos(a) * 440, 512 + Math.sin(a) * 440);
    ctxIn.stroke();
  }

  // 5. Internal Bump Map (Porous cellular honeycomb texture)
  const inBumpCanvas = document.createElement('canvas');
  inBumpCanvas.width = 512;
  inBumpCanvas.height = 512;
  const ctxInBump = inBumpCanvas.getContext('2d')!;
  ctxInBump.fillStyle = '#808080';
  ctxInBump.fillRect(0, 0, 512, 512);
  for (let i = 0; i < 3500; i++) {
    const x = Math.random() * 512;
    const y = Math.random() * 512;
    const rad = Math.random() * 2 + 0.8;
    ctxInBump.fillStyle = Math.random() > 0.4 ? 'rgba(0,0,0,0.25)' : 'rgba(255,255,255,0.2)';
    ctxInBump.beginPath();
    ctxInBump.arc(x, y, rad, 0, Math.PI * 2);
    ctxInBump.fill();
  }

  // Create Three.js Canvas Textures
  const outerColor = new THREE.CanvasTexture(outerCanvas);
  const outerBump = new THREE.CanvasTexture(bumpCanvas);
  const outerRoughness = new THREE.CanvasTexture(roughCanvas);
  const interiorColor = new THREE.CanvasTexture(inCanvas);
  const interiorBump = new THREE.CanvasTexture(inBumpCanvas);

  [outerColor, outerBump, outerRoughness, interiorColor, interiorBump].forEach((tex) => {
    tex.wrapS = THREE.RepeatWrapping;
    tex.wrapT = THREE.RepeatWrapping;
  });

  return {
    outerColor,
    outerBump,
    outerRoughness,
    interiorColor,
    interiorBump,
  };
}

/**
 * Builds anatomical 3D geometry for both bean halves with central furrow deformation.
 */
export function createCoffeeBeanGeometries(): { leftGeometry: THREE.BufferGeometry; rightGeometry: THREE.BufferGeometry } {
  // Base high-subdivision hemisphere/ellipsoid for Left Half
  const leftGeo = new THREE.SphereGeometry(1, 48, 48, 0, Math.PI);
  leftGeo.scale(0.82, 1.38, 0.68);

  const leftPos = leftGeo.attributes.position;
  for (let i = 0; i < leftPos.count; i++) {
    let x = leftPos.getX(i);
    let y = leftPos.getY(i);
    let z = leftPos.getZ(i);

    // Anatomical curvature: taper ends slightly
    const endTaper = 1 - Math.pow(y / 1.38, 2) * 0.22;
    x *= endTaper;
    z *= endTaper;

    // Deep central ventral furrow crease along the inner flat edge
    if (x > -0.15 && z > 0) {
      z -= (0.15 + x) * 0.7;
    }

    leftPos.setXYZ(i, x, y, z);
  }
  leftGeo.computeVertexNormals();

  // Mirror geometry for Right Half
  const rightGeo = new THREE.SphereGeometry(1, 48, 48, Math.PI, Math.PI);
  rightGeo.scale(0.82, 1.38, 0.68);

  const rightPos = rightGeo.attributes.position;
  for (let i = 0; i < rightPos.count; i++) {
    let x = rightPos.getX(i);
    let y = rightPos.getY(i);
    let z = rightPos.getZ(i);

    const endTaper = 1 - Math.pow(y / 1.38, 2) * 0.22;
    x *= endTaper;
    z *= endTaper;

    if (x < 0.15 && z > 0) {
      z -= (0.15 - x) * 0.7;
    }

    rightPos.setXYZ(i, x, y, z);
  }
  rightGeo.computeVertexNormals();

  return { leftGeometry: leftGeo, rightGeometry: rightGeo };
}

/**
 * Assembles all 3D assets into ready-to-use materials, geometries, and textures.
 */
export function createHighQualityCoffeeBeanAssets(): CoffeeBean3DAssets {
  const textures = generateBeanPbrTextures();
  const { leftGeometry, rightGeometry } = createCoffeeBeanGeometries();

  // Premium MeshPhysicalMaterial with clearcoat oil reflections
  const outerMaterial = new THREE.MeshPhysicalMaterial({
    map: textures.outerColor,
    bumpMap: textures.outerBump,
    bumpScale: 0.04,
    roughnessMap: textures.outerRoughness,
    roughness: 0.35,
    metalness: 0.12,
    clearcoat: 0.35,
    clearcoatRoughness: 0.25,
    reflectivity: 0.6,
  });

  // Internal split cross-section material with caramelized emissive warmth
  const interiorMaterial = new THREE.MeshPhysicalMaterial({
    map: textures.interiorColor,
    bumpMap: textures.interiorBump,
    bumpScale: 0.06,
    roughness: 0.55,
    metalness: 0.05,
    emissive: new THREE.Color(0xf3a850),
    emissiveIntensity: 0.25,
  });

  const coreGlowMaterial = new THREE.MeshBasicMaterial({
    color: 0xf3c06b,
    transparent: true,
    opacity: 0.85,
  });

  return {
    leftGeometry,
    rightGeometry,
    outerMaterial,
    interiorMaterial,
    coreGlowMaterial,
    textures,
  };
}
