async function getPlaylistTracks(accessToken, playlistId) {
  const headers = new Headers({
    Authorization: `Bearer ${accessToken}`,
    'Content-Type': 'application/x-www-form-urlencoded',
  });

  let tracks = [];
  let url = `https://api.spotify.com/v1/playlists/${playlistId}/tracks`;

  try {
    while (url) {
      const response = await fetch(url, { headers: headers });
      const data = await response.json();
      tracks = tracks.concat(data.items.map((item) => item.track));

      url = data.next;
    }

    return tracks;
  } catch (error) {
    console.error('Error fetching playlist tracks:', error);
    return [];
  }
}

async function getCurrentUserPlaylists(accessToken) {
  const headers = new Headers({
    Authorization: `Bearer ${accessToken}`,
    'Content-Type': 'application/x-www-form-urlencoded',
  });

  let playlists = [];
  let url = 'https://api.spotify.com/v1/me/playlists';

  try {
    while (url) {
      const response = await fetch(url, { headers: headers });
      const data = await response.json();
      playlists = playlists.concat(data.items);

      url = data.next;
    }

    return playlists;
  } catch (error) {
    console.error('Error fetching user playlists:', error);
    return [];
  }
}

export { getPlaylistTracks, getCurrentUserPlaylists };
