import React, { useRef } from 'react';
import { Canvas, useFrame } from '@react-three/fiber';
import { OrbitControls, PerspectiveCamera, Float, ContactShadows } from '@react-three/drei';
import * as THREE from 'three';

/**
 * Placeholder Mesh for the 3D Coffee Bean
 * Configured with bean-like ellipsoid proportions and roasted material properties.
 */
function PlaceholderBean() {
  const meshRef = useRef<THREE.Mesh>(null);

  useFrame((state, delta) => {
    if (meshRef.current) {
      // Gentle idle rotation to verify scene rendering
      meshRef.current.rotation.y += delta * 0.4;
      meshRef.current.rotation.x = Math.sin(state.clock.getElapsedTime() * 0.8) * 0.1;
    }
  });

  return (
    <Float speed={2} rotationIntensity={0.6} floatIntensity={0.8}>
      <mesh ref={meshRef} scale={[0.85, 1.35, 0.65]} castShadow receiveShadow>
        {/* Placeholder Sphere Geometry scaled to bean proportions */}
        <sphereGeometry args={[1, 36, 36]} />
        {/* Dark roast coffee aesthetic material */}
        <meshStandardMaterial
          color="#381d0f"
          roughness={0.35}
          metalness={0.15}
          bumpScale={0.05}
        />
      </mesh>
    </Float>
  );
}

export interface CoffeeSceneProps {
  className?: string;
  style?: React.CSSProperties;
}

/**
 * CoffeeScene Component
 * Sets up the React Three Fiber Canvas, cinematic studio lighting, camera, and placeholder mesh.
 */
export function CoffeeScene({ className, style }: CoffeeSceneProps) {
  return (
    <div
      className={className}
      style={{
        width: '100%',
        height: '100%',
        minHeight: '400px',
        backgroundColor: '#16100B',
        position: 'relative',
        overflow: 'hidden',
        ...style,
      }}
    >
      <Canvas
        shadows
        dpr={[1, 2]}
        gl={{
          antialias: true,
          toneMapping: THREE.ACESFilmicToneMapping,
          toneMappingExposure: 1.1,
        }}
      >
        {/* Perspective Camera */}
        <PerspectiveCamera makeDefault position={[0, 0, 4.5]} fov={45} />

        {/* Cinematic Studio Lighting */}
        {/* 1. Warm ambient base fill */}
        <ambientLight intensity={0.7} color="#d4af37" />

        {/* 2. Primary directional key light for depth and contrast */}
        <directionalLight
          position={[4, 5, 3]}
          intensity={2.2}
          color="#fff5e6"
          castShadow
          shadow-mapSize-width={1024}
          shadow-mapSize-height={1024}
          shadow-bias={-0.0001}
        />

        {/* 3. Golden rim/accent point light */}
        <pointLight position={[-3, -2, -2]} intensity={2.8} color="#e5a950" />

        {/* 4. Soft top fill light */}
        <directionalLight position={[-2, 3, -2]} intensity={0.8} color="#a67c52" />

        {/* Placeholder 3D Mesh Environment */}
        <PlaceholderBean />

        {/* Soft contact shadow at the base */}
        <ContactShadows
          position={[0, -2, 0]}
          opacity={0.65}
          scale={6}
          blur={2.2}
          far={3}
          color="#0c0502"
        />

        {/* Interactive Orbit Controls for inspection */}
        <OrbitControls
          enableZoom={false}
          enablePan={false}
          minPolarAngle={Math.PI / 4}
          maxPolarAngle={(3 * Math.PI) / 4}
          dampingFactor={0.08}
        />
      </Canvas>

      {/* Preparation Status Overlay */}
      <div
        style={{
          position: 'absolute',
          bottom: '16px',
          left: '50%',
          transform: 'translateX(-50%)',
          backgroundColor: 'rgba(22, 16, 11, 0.85)',
          border: '1px solid rgba(212, 175, 55, 0.4)',
          borderRadius: '20px',
          padding: '6px 14px',
          color: '#d4af37',
          fontFamily: 'serif',
          fontSize: '11px',
          letterSpacing: '1.5px',
          pointerEvents: 'none',
          whiteSpace: 'nowrap',
        }}
      >
        COFFEE SCENE READY • R3F ENVIRONMENT INITIALIZED
      </div>
    </div>
  );
}

export default CoffeeScene;
