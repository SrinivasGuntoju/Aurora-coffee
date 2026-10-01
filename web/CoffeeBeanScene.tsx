import React, { useRef, useState } from 'react';
import { Canvas, useFrame, useThree } from '@react-three/fiber';
import { Float, Html, PerspectiveCamera, Environment, useScroll, ScrollControls } from '@react-three/drei';
import * as THREE from 'three';
import { createHighQualityCoffeeBeanAssets } from './CoffeeBean3DAsset';

/**
 * AURORA COFFEE - 3D Coffee Bean Scene
 * Built with React Three Fiber (@react-three/fiber) and @react-three/drei
 */

/**
 * Scroll Progress Tracking Utility
 * Maps user scroll position (0 to 1) to 3D rotation, expansion, camera zoom, and core illumination.
 */
export function mapScrollProgressToBeanTransform(scrollOffset: number) {
  const smooth = (x: number) => {
    const c = Math.max(0, Math.min(1, x));
    return c * c * (3 - 2 * c);
  };
  const clamped = Math.max(0, Math.min(1, scrollOffset));
  const progress = smooth(clamped);

  // 1. Rotation: Fluid 360-degree rotation
  const rotationY = progress * Math.PI * 2;
  const rotationX = (1 - progress * 2) * (Math.PI / 12);
  const rotationZ = Math.sin(progress * Math.PI * 2) * 0.08;

  // 2. Expansion / Split Amount:
  let expansion = 0;
  if (clamped >= 0.2 && clamped <= 0.5) {
    expansion = smooth((clamped - 0.2) / 0.3);
  } else if (clamped > 0.5 && clamped <= 0.8) {
    expansion = 1.0;
  } else if (clamped > 0.8) {
    expansion = 1.0 - smooth((clamped - 0.8) / 0.2);
  }

  // 3. Camera distance & core glow
  const cameraZ = 4.5 - expansion * 0.9;
  const coreGlow = expansion * 4.5;

  return {
    progress,
    rotationY,
    rotationX,
    rotationZ,
    expansion,
    cameraZ,
    coreGlow,
  };
}

interface CoffeeBeanProps {
  isSplit?: boolean;
}

export function CoffeeBeanModel({ isSplit = false }: CoffeeBeanProps) {
  const leftHalfRef = useRef<THREE.Mesh>(null);
  const rightHalfRef = useRef<THREE.Mesh>(null);
  const coreLightRef = useRef<THREE.PointLight>(null);
  const scroll = useScroll();

  // Create deformed coffee bean geometry with authentic center crease
  const beanGeometry = React.useMemo(() => {
    const geo = new THREE.SphereGeometry(1, 48, 48);
    geo.scale(0.85, 1.35, 0.65);
    const pos = geo.attributes.position;
    for (let i = 0; i < pos.count; i++) {
      let x = pos.getX(i);
      let y = pos.getY(i);
      let z = pos.getZ(i);
      if (z < 0) z *= 0.7; // flatter backside
      const creaseDist = Math.abs(x);
      if (creaseDist < 0.28 && z > 0) {
        z -= (0.28 - creaseDist) * 0.85; // deep center fissure
      }
      pos.setXYZ(i, x, y, z);
    }
    geo.computeVertexNormals();
    return geo;
  }, []);

  // Generate high-resolution procedural PBR assets with cross-sectional internal textures
  const assets = React.useMemo(() => {
    return typeof window !== 'undefined' ? createHighQualityCoffeeBeanAssets() : null;
  }, []);

  useFrame((state, delta) => {
    // Scroll-linked rotation and split progress
    const scrollOffset = scroll ? scroll.offset : 0;
    const targetSplit = isSplit ? 0.85 : scrollOffset * 1.2;

    if (leftHalfRef.current && rightHalfRef.current) {
      // Smoothly separate bean halves
      leftHalfRef.current.position.x = THREE.MathUtils.damp(
        leftHalfRef.current.position.x,
        -targetSplit * 0.7 - 0.05,
        4,
        delta
      );
      rightHalfRef.current.position.x = THREE.MathUtils.damp(
        rightHalfRef.current.position.x,
        targetSplit * 0.7 + 0.05,
        4,
        delta
      );

      // Continuous slow orbital rotation + scroll responsiveness
      const rotY = state.clock.getElapsedTime() * 0.35 + scrollOffset * Math.PI * 2;
      leftHalfRef.current.rotation.y = rotY;
      rightHalfRef.current.rotation.y = rotY;
      leftHalfRef.current.rotation.z = Math.sin(state.clock.getElapsedTime() * 0.5) * 0.08;
      rightHalfRef.current.rotation.z = Math.sin(state.clock.getElapsedTime() * 0.5) * 0.08;
    }

    if (coreLightRef.current) {
      coreLightRef.current.intensity = THREE.MathUtils.lerp(0.5, 4.5, targetSplit);
    }
  });

  return (
    <group position={[0, 0, 0]}>
      {/* Roasting Core Amber Glow */}
      <pointLight ref={coreLightRef} color="#f3c06b" distance={6} decay={2} />

      {/* Left Bean Half with PBR Outer Shell and Split Interior */}
      <group ref={leftHalfRef as any}>
        <mesh
          geometry={assets ? assets.leftGeometry : beanGeometry}
          material={assets ? assets.outerMaterial : undefined}
          castShadow
          receiveShadow
        />
        {/* Interior Split Face with Cellular Roasting Map */}
        <mesh
          position={[0.02, 0, 0]}
          rotation={[0, Math.PI / 2, 0]}
          material={assets ? assets.interiorMaterial : undefined}
        >
          <planeGeometry args={[0.9, 1.8]} />
        </mesh>
      </group>

      {/* Right Bean Half with PBR Outer Shell and Split Interior */}
      <group ref={rightHalfRef as any}>
        <mesh
          geometry={assets ? assets.rightGeometry : beanGeometry}
          material={assets ? assets.outerMaterial : undefined}
          castShadow
          receiveShadow
        />
        {/* Interior Split Face with Cellular Roasting Map */}
        <mesh
          position={[-0.02, 0, 0]}
          rotation={[0, -Math.PI / 2, 0]}
          material={assets ? assets.interiorMaterial : undefined}
        >
          <planeGeometry args={[0.9, 1.8]} />
        </mesh>
      </group>

      {/* Parallax Floating 3D HTML Annotations */}
      <Html position={[-1.8, 1.2, 0]} center distanceFactor={6}>
        <div style={badgeStyle}>
          <div style={badgeLabelStyle}>ORIGIN</div>
          <div style={badgeValueStyle}>ETHIOPIA</div>
          <div style={badgeSubStyle}>Yirgacheffe • 2,150m</div>
        </div>
      </Html>

      <Html position={[1.8, 0.8, 0]} center distanceFactor={6}>
        <div style={badgeStyle}>
          <div style={badgeLabelStyle}>ROAST</div>
          <div style={badgeValueStyle}>MEDIUM</div>
          <div style={badgeSubStyle}>Caramelized 208°C</div>
        </div>
      </Html>

      <Html position={[0, -1.8, 0]} center distanceFactor={6}>
        <div style={badgeStyle}>
          <div style={badgeLabelStyle}>NOTES</div>
          <div style={badgeValueStyle}>CHOCOLATE • CARAMEL • BERRY</div>
          <div style={badgeSubStyle}>Natural Anaerobic Fermentation</div>
        </div>
      </Html>
    </group>
  );
}

/**
 * Camera Controller for Scroll-Linked Interaction
 */
function CameraController() {
  const { camera } = useThree();
  const scroll = useScroll();

  useFrame((state, delta) => {
    const offset = scroll ? scroll.offset : 0;
    // Move camera around the bean based on scroll position
    const targetZ = 4.5 - offset * 1.5;
    const targetY = Math.sin(offset * Math.PI) * 0.6;
    const targetX = Math.cos(offset * Math.PI * 0.5) * 0.4;

    camera.position.x = THREE.MathUtils.damp(camera.position.x, targetX, 3, delta);
    camera.position.y = THREE.MathUtils.damp(camera.position.y, targetY, 3, delta);
    camera.position.z = THREE.MathUtils.damp(camera.position.z, targetZ, 3, delta);
    camera.lookAt(0, 0, 0);
  });

  return null;
}

/**
 * Main React Three Fiber Canvas Scene
 */
export default function CoffeeBeanScene() {
  const [isSplit, setIsSplit] = useState(false);

  return (
    <div style={{ width: '100%', height: '100vh', background: '#16100B', position: 'relative' }}>
      <Canvas shadows dpr={[1, 2]}>
        <PerspectiveCamera makeDefault position={[0, 0, 4.5]} fov={45} />
        
        {/* Cinematic Studio Lighting */}
        <ambientLight intensity={0.7} color="#d4af37" />
        <directionalLight position={[4, 5, 4]} intensity={2.2} color="#fff6e8" castShadow />
        <pointLight position={[-4, -3, -2]} intensity={3} color="#e5a950" />

        <ScrollControls pages={3} damping={0.25}>
          <CameraController />
          <Float speed={2} rotationIntensity={0.5} floatIntensity={0.8}>
            <CoffeeBeanModel isSplit={isSplit} />
          </Float>
        </ScrollControls>
      </Canvas>

      {/* UI Split Toggle Button */}
      <button
        onClick={() => setIsSplit(!isSplit)}
        style={buttonStyle}
      >
        {isSplit ? 'MERGE BEAN' : 'SPLIT 3D BEAN'}
      </button>
    </div>
  );
}

const badgeStyle: React.CSSProperties = {
  background: 'rgba(34, 23, 17, 0.88)',
  border: '1px solid rgba(212, 175, 55, 0.5)',
  borderRadius: '12px',
  padding: '8px 14px',
  color: '#F8F3EA',
  fontFamily: 'serif',
  textAlign: 'center',
  pointerEvents: 'none',
  backdropFilter: 'blur(8px)',
  whiteSpace: 'nowrap',
  boxShadow: '0 8px 24px rgba(0,0,0,0.5)',
};

const badgeLabelStyle: React.CSSProperties = {
  fontSize: '10px',
  letterSpacing: '2px',
  color: '#D4AF37',
  fontWeight: 'bold',
};

const badgeValueStyle: React.CSSProperties = {
  fontSize: '13px',
  fontWeight: 'bold',
  letterSpacing: '1px',
  marginTop: '2px',
};

const badgeSubStyle: React.CSSProperties = {
  fontSize: '10px',
  color: '#A69485',
  marginTop: '2px',
};

const buttonStyle: React.CSSProperties = {
  position: 'absolute',
  bottom: '24px',
  right: '24px',
  background: '#D4AF37',
  color: '#0C0806',
  border: 'none',
  borderRadius: '12px',
  padding: '12px 20px',
  fontWeight: 'bold',
  fontSize: '12px',
  letterSpacing: '1px',
  cursor: 'pointer',
  zIndex: 10,
};
