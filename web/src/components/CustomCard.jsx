'use client';

import React, { useState, useEffect } from 'react';
import { Card, CardFooter, Button, Image } from '@nextui-org/react';
import Link from 'next/link';
import { getItem } from '@/utils/localStorage';
import Swal from 'sweetalert2';

const CustomCard = (props) => {
  const [token, setToken] = useState(null);

  useEffect(() => {
    setToken(getItem('spotifyToken'));
  }, []);

  const handleNoSpotify = () => {
    Swal.fire({
      title: 'You need to log in with Spotify',
      text: 'You need to log in with Spotify to play this game',
      icon: 'info',
      confirmButtonText: 'OK',
      confirmButtonColor: '#6D28D9',
    });
  };

  return (
    <>
      <Card
        isFooterBlurred
        radius="lg"
        className="border-none hover:scale-105 transition-transform duration-300 ease-in-out"
      >
        <Image
          alt={props.alt}
          className="object-cover"
          src={props.src}
          height={400}
          width={400}
          priority="true"
        />
        <CardFooter className="justify-between before:bg-white/10 border-white/20 border-1 overflow-hidden py-1 absolute before:rounded-xl rounded-large bottom-1 w-[calc(100%_-_8px)] shadow-small ml-1 z-10">
          <p className="text-tiny text-white/80">{props.gameName}</p>
          <Button
            size="sm"
            radius="full"
            className="bg-gradient-to-tr from-purple-500 to-pink-700 text-white shadow-lg"
          >
            {token ? (
              <Link className="p-4" href={props.route}>
                {props.btn}
              </Link>
            ) : (
              <span className="p-4" onClick={handleNoSpotify}>
                {props.btn}
              </span>
            )}
          </Button>
        </CardFooter>
      </Card>
    </>
  );
};

export default CustomCard;
