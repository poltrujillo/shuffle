import Image from 'next/image';
import React from 'react';

const Help = () => {
  return (
    <div className="flex justify-center h-screen pt-14">
      <div className="w-4/6 flex flex-col justify-stretch gap-7">
        <h1 className="text-2xl font-semibold leading-tight text-center mb-5">
          Help
        </h1>
        <p className="text-lg text-wrap">
          <span className="font-bold text-purple-200">
            Registro y Vinculación de Spotify:
          </span>{' '}
          Al registrarte en Shuffle, asegúrate de vincular tu cuenta de Sound
          Shuffle con Spotify. <br />
          <br />
          <span className="font-bold text-purple-200">
            Modos de Juego y Nº Jugadores:
          </span>{' '}
          Elige entre tres emocionantes modos de juego: &quot;Genres&quot;,
          &quot;Artist&quot; y &quot;Playlist&quot;. Se puede jugar en solitario
          o en grupos de hasta 4 jugadores.
          <br />
          <br />
          <span className="font-bold text-purple-200">
            Configuración de partida:
          </span>{' '}
          En cada partida se puede configurar el número de rondas, la duración
          de reproducción de cada canción y el nivel de dificultad. <br />
          <br />
          <span className="font-bold text-purple-200">Modo difícil:</span> En el
          modo difícil, juega sin ninguna pista visual y demuestra tus
          habilidades auditivas. <br />
          <br />
          <span className="font-bold text-purple-200">Ranking:</span> Accede al
          Ranking para ver las puntuaciones de todos los jugadores.
        </p>
        <div className="flex flex-row justify-between">
          <Image
            src="/img/help.jpg"
            alt="Help"
            width={400}
            height={300}
            className="rounded-lg"
          />
          <Image
            src="/img/help2.jpg"
            alt="Help"
            width={300}
            height={300}
            className="rounded-lg"
          />
        </div>
      </div>
    </div>
  );
};

export default Help;
