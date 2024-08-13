'use client';

import { useEffect, useState } from 'react';
import genreData from '@/data/genreData';
import Link from 'next/link';
import Image from 'next/image';

const Page = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [filteredGenres, setFilteredGenres] = useState(genreData);
  const [currentId, setCurrentId] = useState('');
  const [titleChoice, setTitleChoice] = useState('Choose a genre to play');
  const [imageSelection, setImageSelection] = useState(
    '/img/singleplayer/interrogante.jpg'
  );

  useEffect(() => {
    const filtered = genreData.filter((genre) =>
      genre.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredGenres(filtered);
    setCurrentPage(0);
  }, [searchTerm]);

  return (
    <div className="w-screen h-screen flex flex-col gap-20 justify-center items-center">
      <h1 className="font-bold text-2xl">Genre Selection</h1>
      <div className="flex flex-row justify-center items-center w-full">
        <div className="w-full mx-20">
          <div className="flex flex-col gap-4">
            <input
              type="text"
              placeholder="Search for a genre"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="mb-4 p-2 border rounded"
            />
            {filteredGenres.map((genre) => (
              <div
                key={genre.id}
                className="hover:scale-105 transition-transform duration-300 flex flex-row justify-center items-center gap-3rounded cursor-pointer"
                onClick={() => {
                  setTitleChoice(genre.name);
                  setImageSelection(genre.img);
                  setCurrentId(genre.id);
                }}
              >
                <ul className="w-full flex flex-row justify-between items-center gap-3 hover:bg-slate-800 transition-all px-5 rounded-lg">
                  <li>
                    <Image
                      src={genre.img}
                      alt={genre.alt}
                      width={80}
                      height={80}
                      className="rounded-full"
                    />
                  </li>
                  <li>{genre.name}</li>
                </ul>
              </div>
            ))}
          </div>
        </div>
        <div className="w-full flex flex-col justify-center items-center">
          <Image
            src={imageSelection}
            alt="Genre selection"
            width={400}
            height={400}
            className="w-96 rounded-md"
          />
          <h2 className="mt-4">{titleChoice}</h2>
          <Link
            href={`/game/settings?id=${currentId}&gameMode=1`}
            passHref
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
