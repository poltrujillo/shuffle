'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import Image from 'next/image';
import { getItem } from '@/utils/localStorage';

const Page = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [playlists, setPlaylists] = useState([]);
  const [currentId, setCurrentId] = useState('');
  const [titleChoice, setTitleChoice] = useState('Select a playlist');
  const [imageSelection, setImageSelection] = useState(
    '/img/singleplayer/interrogante.jpg'
  );

  async function fetchUserPlaylists(accessToken) {
    const headers = new Headers({
      Authorization: `Bearer ${accessToken}`,
      'Content-Type': 'application/x-www-form-urlencoded',
    });
    let url = `https://api.spotify.com/v1/me/playlists?limit=50`;

    try {
      let allPlaylists = [];
      while (url) {
        const response = await fetch(url, { headers: headers });
        const data = await response.json();
        allPlaylists = allPlaylists.concat(data.items);
        url = data.next;
      }
      setPlaylists(allPlaylists);
    } catch (error) {
      console.error('Error fetching user playlists:', error);
    }
  }

  useEffect(() => {
    const token = getItem('spotifyToken');
    if (token) {
      fetchUserPlaylists(token);
    }
  }, []);

  const filteredPlaylists = playlists.filter((playlist) =>
    playlist.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="w-screen h-screen flex flex-col gap-20 justify-center items-center">
      <h1 className="font-bold text-2xl">Playlist Selection</h1>
      <div className="flex flex-row justify-center items-center w-full">
        <div className="w-full mx-20">
          <div className="flex flex-col gap-4">
            <input
              type="text"
              placeholder="Search for a playlist"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="mb-4 p-2 border rounded"
            />
            {filteredPlaylists.map((playlist) => (
              <div
                key={playlist.id}
                onClick={() => {
                  setTitleChoice(playlist.name);
                  setImageSelection(
                    playlist.images[0]?.url ||
                      '/img/singleplayer/interrogante.jpg'
                  );
                  setCurrentId(playlist.id);
                }}
                className="hover:scale-105 transition-transform duration-300 flex flex-row justify-center items-center gap-3 cursor-pointer rounded"
              >
                <ul className="w-full flex flex-row justify-between items-center gap-3 hover:bg-slate-800 transition-all px-5 rounded-lg">
                  <li>
                    <Image
                      src={
                        playlist.images[0]?.url ||
                        '/img/singleplayer/interrogante.jpg'
                      }
                      alt={playlist.name}
                      width={80}
                      height={80}
                      className="rounded-full"
                    />
                  </li>
                  <li>{playlist.name}</li>
                </ul>
              </div>
            ))}
          </div>
        </div>
        <div className="w-full flex flex-col justify-center items-center">
          <Image
            src={imageSelection}
            alt="Playlist selection"
            width={400}
            height={400}
            className="w-96 rounded-md"
          />
          <h2 className="mt-4">{titleChoice}</h2>
          <Link
            href={`/game/settings?id=${currentId}&gameMode=3`}
            className="bg-shuffle-primary text-white p-2 rounded-md mt-4 hover:bg-primary-dark transition-all duration-300 cursor-pointer px-5 hover:scale-105"
          >
            Play
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Page;
