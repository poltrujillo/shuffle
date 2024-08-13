const url = process.env.NEXT_PUBLIC_BACKEND_URL;
const baseUrl = `${url}/api/games`;

const gameService = {
  saveGame: (gameId, userId) => {
    try {
      return fetch(`${baseUrl}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ game_modes_id: gameId, user_id: [userId] }),
      });
    } catch (error) {
      console.error('There was a problem with the fetch operation: ', error);
      throw error;
    }
  },
};

export default gameService;
