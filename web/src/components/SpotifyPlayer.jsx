import { useEffect, useState } from 'react';

const SpotifyPlayer = ({ accessToken, trackUri, durationMs, onTimeUp }) => {
  const [deviceID, setDeviceID] = useState(null);
  const [progressMs, setProgressMs] = useState(durationMs);

  useEffect(() => {
    const script = document.createElement('script');
    script.src = 'https://sdk.scdn.co/spotify-player.js';
    script.async = true;
    document.body.appendChild(script);

    const initSpotifyPlayer = () => {
      if (window.Spotify && !window.spotifyPlayerInitialized) {
        window.spotifyPlayerInitialized = true;
        const spotifyPlayer = new window.Spotify.Player({
          name: 'Web Playback SDK Quick Start Player',
          getOAuthToken: (cb) => {
            cb(accessToken);
          },
          volume: 0.5,
        });

        spotifyPlayer.addListener('ready', ({ device_id }) => {
          console.log('Ready with Device ID', device_id);
          setDeviceID(device_id);
        });

        spotifyPlayer.addListener('not_ready', ({ device_id }) => {
          console.log('Device ID has gone offline', device_id);
        });

        spotifyPlayer.connect();
      }
    };

    window.onSpotifyWebPlaybackSDKReady = initSpotifyPlayer;

    return () => {
      if (window.spotifyPlayer) {
        window.spotifyPlayer.disconnect();
      }
    };
  }, [accessToken]);

  useEffect(() => {
    if (deviceID && trackUri) {
      fetch(`https://api.spotify.com/v1/me/player/play?device_id=${deviceID}`, {
        method: 'PUT',
        body: JSON.stringify({ uris: [trackUri] }),
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${accessToken}`,
        },
      })
        .then(() => {
          setProgressMs(durationMs);
        })
        .catch((err) => console.error('Playback error:', err));

      const intervalId = setInterval(() => {
        setProgressMs((prev) => {
          const newProgress = prev - 1000;
          if (newProgress <= 0) {
            clearInterval(intervalId);
            onTimeUp && onTimeUp();
          }
          return newProgress > 0 ? newProgress : 0;
        });
      }, 1000);

      return () => clearInterval(intervalId);
    }
  }, [deviceID, trackUri, accessToken, durationMs]);

  const progressPercentage = ((progressMs / durationMs) * 100).toFixed(2);

  return (
    <div className="w-screen flex flex-col justify-center items-center gap-2">
      <div style={{ width: '50%', backgroundColor: '#ccc' }}>
        <div
          className={`h-5 ${
            progressMs > 0 ? 'bg-shuffle-primary' : 'bg-red-500'
          }`}
          style={{ width: `${progressPercentage}%` }}
        ></div>
      </div>
      <div style={{ color: 'white', marginTop: '10px' }}>
        {progressMs > 0
          ? `${Math.ceil(progressMs / 1000)}s remaining`
          : "Time's up!"}
      </div>
    </div>
  );
};

export default SpotifyPlayer;
