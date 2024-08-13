const findArtistNameById = (id, artists) => {
  const artist = artists.find((artist) => artist.id === id);
  return artist ? artist.name : '';
};

function selectRandomTracks(tracks, count = 5) {
  const shuffled = tracks.sort(() => 0.5 - Math.random());
  return shuffled.slice(0, count);
}

export { findArtistNameById, selectRandomTracks };
