'use client';

import Link from 'next/link';
import React, { useEffect, useState } from 'react';
import { FaBars, FaTimes } from 'react-icons/fa';
import { useAuth } from '@/context/AuthContext';
import Image from 'next/image';
import { ScaleLoader } from 'react-spinners';
import Swal from 'sweetalert2';
import { removeItem } from '@/utils/localStorage';

const Navbar = () => {
  const [nav, setNav] = useState(false);
  const { user, logout } = useAuth();
  const [isLoading, setIsLoading] = useState(true);
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {
    if (user !== null) {
      setIsAdmin(user.user.is_admin);
      setIsLoading(false);
    }
  }, [user]);

  const links = [
    { id: 1, link: '/', name: 'Home' },
    { id: 2, link: '/about', name: 'About' },
    { id: 3, link: '/help', name: 'Help' },
  ];

  if (isLoading) {
    return (
      <div className="flex justify-center items-center w-full h-20 px-4 text-white bg-black fixed nav">
        <span>
          <ScaleLoader color="#ffffff" />
        </span>
      </div>
    );
  }

  return (
    <div className="flex justify-end items-center w-full h-20 px-4 text-white bg-black fixed nav">
      <ul className="hidden md:flex items-center justify-center">
        {links.map(({ id, link, name }) => (
          <li
            key={id}
            className="nav-links px-4 cursor-pointer capitalize font-medium text-gray-500 hover:scale-105 hover:text-white duration-200"
          >
            <Link href={link}>{name}</Link>
          </li>
        ))}
        {user && (
          <>
            {isAdmin ? (
              <li className="nav-links px-4 cursor-pointer capitalize font-medium text-gray-500 hover:scale-105 hover:text-white duration-200">
                <Link href="/admin">Admin</Link>
              </li>
            ) : (
              ''
            )}
            <li
              className="nav-links px-4 cursor-pointer capitalize font-medium text-gray-500 hover:scale-105 hover:text-white duration-200"
              onClick={() => {
                Swal.fire({
                  title: 'Are you sure?',
                  text: 'You are about to logout',
                  icon: 'warning',
                  showCancelButton: true,
                  confirmButtonText: 'Yes',
                  cancelButtonText: 'No',
                }).then((result) => {
                  if (result.isConfirmed) {
                    logout();
                    removeItem('spotifyToken');
                  }
                });
              }}
            >
              Logout
            </li>
            <li className="px-4 cursor-pointer text-4xl flex flex-row items-center justify-center">
              <Image
                src={
                  user && user.user && user.user.image
                    ? `${process.env.NEXT_PUBLIC_BACKEND_URL}/${user.user.image}`
                    : '/img/default-user.png'
                }
                alt="User profile image"
                width={40}
                height={40}
                className="rounded-full h-10 w-10 aspect-square"
                unoptimized
              />
              <Link href={`/user`} className="ml-2 text-slate-50 text-sm">
                {user.user.username}
              </Link>
            </li>
          </>
        )}
      </ul>

      <div
        onClick={() => setNav(!nav)}
        className="cursor-pointer pr-4 z-10 text-gray-500 md:hidden"
      >
        {nav ? <FaTimes size={30} /> : <FaBars size={30} />}
      </div>

      {nav && (
        <ul className="flex flex-col justify-center items-center absolute top-0 left-0 w-full h-screen bg-gradient-to-b from-black to-gray-800 text-gray-500">
          {links.map(({ id, link, name }) => (
            <li
              key={id}
              className="px-4 cursor-pointer capitalize py-6 text-4xl"
            >
              <Link onClick={() => setNav(!nav)} href={link}>
                {name}
              </Link>
            </li>
          ))}
          {user && (
            <>
              <li
                className="px-4 cursor-pointer capitalize py-6 text-4xl"
                onClick={() => {
                  Swal.fire({
                    title: 'Are you sure?',
                    text: 'You are about to logout',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Yes',
                    cancelButtonText: 'No',
                  }).then((result) => {
                    if (result.isConfirmed) {
                      logout();
                      setNav(false);
                    }
                  });
                }}
              >
                Logout
              </li>
              <li className="px-4 cursor-pointer capitalize py-6 text-4xl">
                <Image
                  src={user.image || '/img/default-user.png'}
                  width={40}
                  height={40}
                  alt="User Image"
                  className="h-10 w-10 rounded-full"
                />
                <span className="ml-2 text-slate-50">{user.user.username}</span>
              </li>
            </>
          )}
        </ul>
      )}
    </div>
  );
};

export default Navbar;
