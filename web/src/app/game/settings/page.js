'use client';

import { useSearchParams } from 'next/navigation';
import React, { useState } from 'react';
import { Switch, Button, Input } from '@nextui-org/react';

const Page = () => {
  const [rounds, setRounds] = useState(5);
  const [seconds, setSeconds] = useState(30);
  const [difficultMode, setDifficultMode] = useState(true);
  const searchParams = useSearchParams();
  const id = searchParams.get('id');
  const gameMode = searchParams.get('gameMode');

  return (
    <div className="flex flex-col justify-center items-center h-screen w-screen gap-14">
      <h1 className="text-4xl font-bold">Game Settings</h1>
      <div className="flex w-4/12 flex-wrap md:flex-nowrap gap-6 flex-col justify-center items-center">
        <Input
          type="number"
          label="Rounds Number"
          placeholder="Default: 5 | Max: 10 | Min: 1"
          max={10}
          min={1}
          onChange={(e) => setRounds(e.target.value)}
        />
        <Input
          type="number"
          max={60}
          min={10}
          placeholder="Default: 30 | Max: 60 | Min: 10"
          label="Seconds of the Song"
          onChange={(e) => setSeconds(e.target.value)}
        />
        <Switch
          defaultSelected
          value="true"
          onChange={(e) => {
            setDifficultMode(e.target.checked);
          }}
          className=""
        >
          Easy Mode (with clues)
        </Switch>
        <Button
          color="primary"
          variant="shadow"
          size="lg"
          className="mt-4 bg-shuffle-primary"
        >
          <a
            className="w-full"
            href={`/game?id=${id}&seconds=${seconds}&rounds=${rounds}&difficult=${difficultMode}&gameMode=${gameMode}`}
          >
            Play!
          </a>
        </Button>
      </div>
    </div>
  );
};

export default Page;
