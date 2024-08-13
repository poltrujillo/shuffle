import queryString from 'query-string';

const spotifyAuthService = {
  url: 'https://accounts.spotify.com/authorize?',
  token: '',
  redirect_uri: process.env.NEXT_PUBLIC_SPOTIFY_REDIRECT_URI,
  response_type: 'code',
  client_id: process.env.NEXT_PUBLIC_SPOTIFY_CLIENT_ID,
  headers: 'application/x-www-form-urlencoded',
  scope: 'user-read-private user-read-email streaming',
  client_secret: process.env.NEXT_PUBLIC_SPOTIFY_CLIENT_SECRET,

  getToken: function () {
    return this.token;
  },

  setToken: function (newToken) {
    this.token = newToken;
  },

  parseData: function (data) {
    return Object.keys(data)
      .map((key) => {
        const encodedKey = encodeURIComponent(key);
        const encodedValue = encodeURIComponent(data[key]);
        return `${encodedKey}=${encodedValue}`;
      })
      .join('&');
  },

  getAuthURL: function () {
    return (
      this.url +
      queryString.stringify({
        response_type: this.response_type,
        client_id: this.client_id,
        scope: this.scope,
        redirect_uri: this.redirect_uri,
      })
    );
  },

  getScopedToken: async function (data) {
    const response = await fetch('https://accounts.spotify.com/api/token', {
      method: 'POST',
      headers: {
        'Content-Type': this.headers,
        Authorization:
          'Basic ' + btoa(this.client_id + ':' + this.client_secret),
      },
      body: this.parseData(data),
    });
    return response.json();
  },
};

export default spotifyAuthService;
