'use client';

import React from 'react';
import { useSearchParams } from 'next/navigation';
import Link from 'next/link';

const Page = () => {
  const searchParams = useSearchParams();
  const tracksGuessed = parseInt(searchParams.get('guesses'), 10) + 1;
  const id = searchParams.get('id');
  return (
    <div className="flex flex-col justify-center items-center h-screen w-screen gap-5">
      <h1 className="text-slate-50 font-bold text-2xl">Game Results</h1>
      <p className="text-slate-50">
        You have make {tracksGuessed * 100} points in this game!
      </p>
      <div className="flex flex-row gap-3 justify-items items-center">
        <Link href="/" className="p-3 bg-slate-500 rounded-xl">
          Go back to main menu
        </Link>
        <Link
          href={`/game/settings?id=${id}`}
          className="p-3 bg-shuffle-primary rounded-xl"
        >
          Play again
        </Link>
      </div>
    </div>
  );
};

export default Page;
