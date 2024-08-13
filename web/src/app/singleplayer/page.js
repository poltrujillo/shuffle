'use client';
import Carrousel from '@/components/Carrousel';
import singleplayerData from '../../data/singleplayerData';
import { useState, useEffect } from 'react';
import { ScaleLoader } from 'react-spinners';

const Page = () => {
  const [isClient, setIsClient] = useState(false);

  useEffect(() => {
    setIsClient(true);
  }, []);

  if (!isClient) {
    return (
      <div className="h-screen flex justify-center items-center">
        <ScaleLoader color="#ffffff" />
      </div>
    );
  }

  return (
    <div className="w-screen h-screen flex justify-center items-center flex-col gap-20">
      <h1 className="text-4xl font-bold">Choose a Game Mode</h1>
      <Carrousel items={singleplayerData} speed="fast" pauseOnHover="true" />
    </div>
  );
};

export default Page;
