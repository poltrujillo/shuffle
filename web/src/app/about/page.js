import React from 'react';
import Image from 'next/image';

const About = () => {
  return (
    <div className="flex justify-center h-screen pt-14">
      <div className="w-4/6 flex flex-col justify-stretch gap-7">
        <h1 className="text-2xl font-semibold leading-tight text-center mb-5">
          About
        </h1>
        <p className="text-lg text-wrap">
          Shuffle es una aplicación que consiste en un juego de adivinar
          canciones de Spotify. Este proyecto esta desarrollado por tres
          desarrolladores: Ramon Mormeneo, Carla Flores y Pol Trujillo,
          estudiantes de la Salle de Gràcia.
        </p>
        <div className="flex flex-row justify-center items-center pt-20">
          <Image
            src="/img/la-salle-gracias_logo.png"
            alt="About"
            width={400}
            height={300}
            className="rounded-lg"
          />
        </div>
      </div>
    </div>
  );
};

export default About;
