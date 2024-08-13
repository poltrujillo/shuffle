import singlePlayer from './../../public/img/main/singleplayer.webp';
import leaderboard from './../../public/img/main/leaderboard.webp';

const gamesData = [
  {
    id: 1,
    alt: 'Singleplayer Gamemode',
    src: singlePlayer,
    gameName: 'Singleplayer',
    btn: 'Play',
    route: '/singleplayer',
  },
  {
    id: 3,
    alt: 'Leaderboard',
    src: leaderboard,
    gameName: 'Rankings',
    btn: 'View',
    route: '/rankings',
  },
];

export default gamesData;
