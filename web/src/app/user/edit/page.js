'use client';

import React, { useState, useEffect } from 'react';
import { useAuth } from '@/context/AuthContext';
import userService from '@/services/userService';
import { toast } from 'react-toastify';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { ScaleLoader } from 'react-spinners';

const EditUser = () => {
  const router = useRouter();
  const { user } = useAuth();
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user) {
      setUsername(user.user.username);
      setEmail(user.user.email);
    }
  }, [user]);

  const handleClick = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await userService.updateUser({ username, email }, user.user.id);
      toast.success('User updated successfully');
      router.push('/user');
    } catch (error) {
      console.error('There was a problem updating the user: ', error);
      toast.error('Failed to update user.');
    }
    setLoading(false);
  };

  if (!user || loading) {
    return (
      <div className="h-screen flex justify-center items-center">
        <ScaleLoader color="#ffffff" />
      </div>
    );
  }

  return (
    <div className="flex justify-center mt-10">
      <form className="w-full max-w-sm">
        <h1 className="text-2xl font-semibold leading-tight text-center mb-4">
          Edit User Profile
        </h1>
        <div className="md:flex md:items-center mb-6">
          <div className="md:w-1/3">
            <label
              className="block text-gray-500 font-bold md:text-right mb-1 md:mb-0 pr-4"
              htmlFor="username"
            >
              Username
            </label>
          </div>
          <div className="md:w-2/3">
            <input
              onChange={(e) => setUsername(e.target.value)}
              className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4 text-gray-700 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"
              type="text"
              id="username"
              name="username"
              value={username}
            />
          </div>
        </div>
        <div className="md:flex md:items-center mb-6">
          <div className="md:w-1/3">
            <label
              className="block text-gray-500 font-bold md:text-right mb-1 md:mb-0 pr-4"
              htmlFor="email"
            >
              Email
            </label>
          </div>
          <div className="md:w-2/3">
            <input
              onChange={(e) => setEmail(e.target.value)}
              className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4 text-gray-700 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"
              type="email"
              id="email"
              name="email"
              value={email}
            />
          </div>
        </div>

        <div className="md:flex md:items-center">
          <div className="md:w-1/3"></div>
          <div className="md:w-2/3">
            <Link
              href="/user"
              className="shadow bg-gray-500 hover:bg-gray-400 focus:shadow-outline focus:outline-none text-white font-bold py-2 px-4 rounded mr-4 transition-all"
            >
              Cancel
            </Link>
            <button
              className="shadow bg-purple-500 hover:bg-purple-400 focus:shadow-outline focus:outline-none text-white font-bold py-2 px-4 rounded transition-all"
              type="submit"
              onClick={handleClick}
            >
              Save
            </button>
          </div>
        </div>
      </form>
    </div>
  );
};

export default EditUser;
