import React from 'react';
import GithubLogo from './icons/GithubLogo';
import Link from 'next/link';

const Footer = () => {
  return (
    <footer
      className="bg-white rounded-lg shadow m-4 dark:bg-slate-900 absolute bottom-0 w-10/12 opacity-75
    "
    >
      <div className="w-full mx-auto max-w-screen-xl p-3 md:flex md:items-center md:justify-between">
        <span className="text-sm text-gray-500 sm:text-center dark:text-gray-400 flex flex-row items-center gap-2">
          © 2024 <p href="https://shuffle.com/">Shuffle™.</p>All Rights
          Reserved.
        </span>
        <ul className="flex flex-wrap items-center mt-3 text-sm font-medium text-gray-500 dark:text-gray-400 sm:mt-0">
          <li>
            <Link
              href="https://github.com/"
              className="hover:underline me-4 md:me-6 flex flex-row gap-2 items-center"
            >
              Github
              <span>
                <GithubLogo />
              </span>
            </Link>
          </li>
        </ul>
      </div>
    </footer>
  );
};

export default Footer;
