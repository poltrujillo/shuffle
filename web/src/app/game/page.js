'use client';

import React, { useState, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import SpotifyPlayer from '@/components/SpotifyPlayer';
import { getPlaylistTracks } from '@/services/SpotifyArtistService';
import { getItem } from '@/utils/localStorage';
import { selectRandomTracks } from '@/utils/artistsUtil';
import Swal from 'sweetalert2';
import rankingService from '@/services/rankingService';
import { useAuth } from '@/context/AuthContext';
import gameService from '@/services/gameService';

const Page = () => {
  const searchParams = useSearchParams();
  const secondsOfSong = searchParams.get('seconds');
  const difficultMode = searchParams.get('difficult');
  const roundsOfGame = searchParams.get('rounds');
  const playlistId = searchParams.get('id');
  const gameMode = searchParams.get('gameMode');

  const [tracks, setTracks] = useState([]);
  const [currentTrackIndex, setCurrentTrackIndex] = useState(0);
  const [userGuess, setUserGuess] = useState('');
  const [round, setRound] = useState(1);
  const [clue, setClue] = useState('');
  const [modifiedTrackName, setModifiedTrackName] = useState('');
  const [gameStarted, setGameStarted] = useState(false);
  const [guessedSongs, setGuessedSongs] = useState(0);
  const [totalRounds, setTotalRounds] = useState(parseInt(roundsOfGame) || 5);
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const router = useRouter();
  const { user } = useAuth();

  const startGame = async () => {
    const accessToken = getItem('spotifyToken');
    const fetchedTracks = await getPlaylistTracks(accessToken, playlistId);
    const selectedTracks = selectRandomTracks(fetchedTracks, totalRounds);
    setTracks(selectedTracks);
    setRound(1);
    setCurrentTrackIndex(0);
    setGameStarted(true);
  };

  useEffect(() => {
    if (user) {
      setUsername(user.user.username);
      setEmail(user.user.email);
    }
    if (gameStarted && tracks.length > 0) {
      startRound();
    }
  }, [currentTrackIndex, gameStarted, tracks]);

  const startRound = () => {
    if (currentTrackIndex >= tracks.length) {
      console.error('Track index is out of bounds');
      return;
    }

    let trackName = tracks[currentTrackIndex]?.name;

    if (!trackName) {
      console.error('Current track is undefined');
      return;
    }

    const truncateAt = trackName.search(/(ft\.|feat\.|\(|\[)/i);
    if (truncateAt !== -1) {
      trackName = trackName.substring(0, truncateAt).trim();
    }
    setModifiedTrackName(trackName);
    const displayClue =
      difficultMode === 'true' ? trackName.replace(/\S/g, '_ ') : '';
    setClue(displayClue);
    console.log('Real answer:', trackName);
    console.log(email, username);
  };

  const handleGuessChange = (event) => {
    setUserGuess(event.target.value);
  };

  const handleTimeUp = () => {
    if (currentTrackIndex < tracks.length - 1) {
      setCurrentTrackIndex((currentIndex) => currentIndex + 1);
      setRound((prevRound) => prevRound + 1);
    } else {
      setGameStarted(false);
      router.push(`/game/results?guesses=${guessedSongs}&id=${playlistId}`);
    }
  };

  const checkGuess = async () => {
    if (userGuess.toLowerCase() === modifiedTrackName.toLowerCase()) {
      setGuessedSongs(guessedSongs + 1);
      Swal.fire({
        title: 'Correct!',
        text: 'Moving to the next song.',
        icon: 'success',
        confirmButtonText: 'OK',
      });
      if (currentTrackIndex < tracks.length - 1) {
        setCurrentTrackIndex((currentIndex) => currentIndex + 1);
        setRound(round + 1);
      } else {
        Swal.fire({
          title: "Congratulations! You've completed the game.",
          icon: 'success',
        });
        setTracks([]);
        router.push(`/game/results?guesses=${guessedSongs}&id=${playlistId}`);

        try {
          const response = await gameService.saveGame(
            parseInt(gameMode, 10),
            user.user.id
          );
          if (response.status === 200) {
            console.log('Score saved in the database');
          }
        } catch (error) {
          console.error('Error adding value in the database: ', error);
          Swal.fire({
            title: 'Error!',
            text: 'An error occurred while saving the score.',
            icon: 'error',
            confirmButtonText: 'OK',
          });
        }

        try {
          const rankings = await rankingService.getRankings();
          const userExists = rankings.find(
            (ranking) => ranking.email === email
          );
          if (userExists) {
            if (userExists.score > (guessedSongs + 1) * 100) {
              return;
            }
            await rankingService.updateRanking(
              userExists.id,
              (guessedSongs + 1) * 100
            );
          } else {
            await rankingService.addRanking(
              email,
              username,
              (guessedSongs + 1) * 100
            );
          }
        } catch (error) {
          console.error('Error adding document: ', error);
          Swal.fire({
            title: 'Error!',
            text: 'An error occurred while saving the score.',
            icon: 'error',
            confirmButtonText: 'OK',
          });
        }
      }
    } else {
      Swal.fire({
        title: 'Incorrect!',
        text: 'Try again!',
        icon: 'error',
        confirmButtonText: 'OK',
      });
    }
    setUserGuess('');
  };

  return (
    <div className="flex flex-col justify-center items-center h-screen w-screen">
      {!gameStarted && tracks.length === 0 && (
        <button
          onClick={startGame}
          className="bg-shuffle-primary text-white px-4 py-2 rounded-md my-5"
        >
          Start Game
        </button>
      )}
      {gameStarted && (
        <>
          <p className="text-slate-50 font-bold text-xl">
            Round: {round}/{totalRounds}
          </p>
          <div className="flex flex-row items-center mt-4">
            {clue.split('').map((char, index) => (
              <span key={index} className="inline-block px-[0.25rem] text-3xl">
                {char}
              </span>
            ))}
          </div>
          <input
            type="text"
            value={userGuess}
            onChange={handleGuessChange}
            placeholder="Guess the song name"
            className="mt-4 px-4 py-2 rounded-md border-2 border-slate-50 w-80 text-slate-50 focus:outline-none focus:border-shuffle-primary transition duration-300 ease-in-out text-xl"
          />
          <button
            onClick={checkGuess}
            className="bg-shuffle-primary hover:opacity-80 transition-all text-white px-4 py-2 rounded-md my-5"
          >
            Submit Guess
          </button>
        </>
      )}
      {!gameStarted && tracks.length > 0 && (
        <a
          href={`/game/results?guesses=${guessedSongs}&id=${playlistId}`}
          className="bg-shuffle-primary text-white px-4 py-2 rounded-md mt-4"
        >
          See the results
        </a>
      )}
      <SpotifyPlayer
        accessToken={getItem('spotifyToken')}
        trackUri={
          gameStarted && tracks.length > 0
            ? tracks[currentTrackIndex]?.uri
            : null
        }
        durationMs={secondsOfSong * 1000}
        onTimeUp={handleTimeUp}
      />
    </div>
  );
};

export default Page;
