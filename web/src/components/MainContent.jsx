'use client';

import React, { useEffect, useState } from 'react';
import Image from 'next/image';
import Link from 'next/link';
import shuffleLogo from './../../public/logo/LightLogo.svg';
import gamesData from './../data/gamesData';
import CustomCard from './CustomCard';
import { getItem } from '@/utils/localStorage';
import spotifyAuthService from '@/services/SpotifyAuthService';
import Check from './icons/Check';
import { useAuth } from '@/context/AuthContext';

const MainContent = () => {
  const { user } = useAuth();
  const [token, setToken] = useState(null);
  useEffect(() => {
    setToken(getItem('spotifyToken'));
  }, []);

  const url = spotifyAuthService.getAuthURL();

  return (
    <>
      <Image src={shuffleLogo} alt="Shuffle Logo" width={275} height={275} />
      <div className="hidden md:flex-row md:flex gap-24">
        {gamesData.map((game) => (
          <CustomCard
            key={game.id}
            alt={game.alt}
            src={game.src.src}
            gameName={game.gameName}
            btn={game.btn}
            route={game.route}
          />
        ))}
      </div>
      {token ? (
        <div className="mt-4 text-shuffle-secondary font-bold py-2 px-4 rounded flex flex-row gap-3">
          <span>Spotify</span> <Check />
        </div>
      ) : (
        <Link
          href={url}
          passHref
          className="mt-4 text-white bg-shuffle-primary hover:opacity-80 font-bold py-2 px-4 rounded-md transition-all"
        >
          Log in with Spotify
        </Link>
      )}
    </>
  );
};

export default MainContent;
