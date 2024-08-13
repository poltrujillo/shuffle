'use client';

import { useEffect, useState } from 'react';
import { useSearchParams } from 'next/navigation';
import spotifyAuthService from '@/services/SpotifyAuthService';
import { saveUniqueItem } from '@/utils/localStorage';
import { useRouter } from 'next/navigation';

function Page() {
  const router = useRouter();
  const [response, setResponse] = useState('');
  const searchParams = useSearchParams();

  useEffect(() => {
    const code = searchParams.get('code');

    if (!code) {
      console.error('Spotify authorization code not found in URL parameters.');
      return;
    }

    async function fetchData() {
      try {
        const data = await spotifyAuthService.getScopedToken({
          code: code,
          grant_type: 'authorization_code',
          redirect_uri: process.env.NEXT_PUBLIC_SPOTIFY_REDIRECT_URI,
        });

        if (data.access_token) {
          setResponse(data.access_token);
          saveUniqueItem('spotifyToken', data.access_token);
          router.push('/');
        } else {
          throw new Error('Access token is undefined.');
        }
      } catch (error) {
        console.error('Failed to fetch scoped token:', error);
      }
    }

    fetchData();
  }, [searchParams]);

  return (
    <div className="flex justify-center items-center h-screen">
      <p className="text-slate-50 font-bold text-4xl">
        {response ? 'Logged in' : 'Logging in...'}
      </p>
    </div>
  );
}

export default Page;
