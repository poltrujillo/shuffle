import { Providers } from './providers';
import './globals.css';
import Background from '@/components/Background';
import Image from 'next/image';
import shuffleLogo from './../../public/logo/mini-light.svg';
import { AuthProvider } from '@/context/AuthContext';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export const metadata = {
  title: 'Shuffle',
  description: 'Spotify based quiz game',
};

export default function RootLayout({ children }) {
  return (
    <html lang="en" className="dark">
      <body>
        <AuthProvider>
          <h1 className="text-5xl font-signature ml-2 fixed z-50 top-5 left-3">
            <a
              className="link-underline link-underline-black"
              href="/"
              rel="noreferrer"
            >
              <Image
                src={shuffleLogo}
                alt="Shuffle Logo"
                width={50}
                height={50}
              />
            </a>
          </h1>
          <div className="flex flex-col items-center">
            <Providers>{children}</Providers>
            <Background />
          </div>
        </AuthProvider>
        <ToastContainer />
      </body>
    </html>
  );
}
