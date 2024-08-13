/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '8000',
        pathname: '**',
      },
      {
        protocol: 'https',
        hostname: '*.scdn.co',
        pathname: '**',
      },
      {
        protocol: 'https',
        hostname: '*.spotifycdn.com',
        pathname: '**',
      },
    ], // Add this line to configure the external hostname
  },

  // Uncomment and use the following if you have redirects
  // async redirects() {
  //   return [
  //     {
  //       source: '/singleplayer/auth',
  //       destination: '/singleplayer',
  //       permanent: true,
  //     },
  //   ];
  // },
};

export default nextConfig;
