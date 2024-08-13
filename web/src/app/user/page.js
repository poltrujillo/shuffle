'use client';

import React, { useState, useEffect } from 'react';
import Image from 'next/image';
import { useAuth } from '@/context/AuthContext';
import Link from 'next/link';
import userService from '@/services/userService';
import { getItem } from '@/utils/localStorage';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { ScaleLoader } from 'react-spinners';

const User = () => {
  const { user } = useAuth();
  const [authUser, setAuthUser] = useState(user);
  const [image, setImage] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user) {
      getItem('token');
      setAuthUser(user);
    }
  }, [user]);

  const handleImageChange = (e) => {
    setImage(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (image) {
      try {
        setLoading(true);
        const response = await userService.updateProfileImage(
          authUser.user.id,
          image
        );

        setAuthUser({ ...authUser, image: response.user.image });
        toast.success('Profile image updated successfully');
      } catch (error) {
        console.error('Error updating profile image:', error);
        toast.error('Failed to update profile image');
      } finally {
        setLoading(false);
      }
    }
  };

  if (!authUser) {
    return (
      <div className="h-screen flex justify-center items-center">
        <ScaleLoader color="#ffffff" />
      </div>
    );
  }

  return (
    <main className="flex justify-center items-center min-h-screen">
      <section className="rounded-lg p-8 text-white max-w-sm mx-auto">
        <h1 className="text-4xl font-bold text-center mb-6">User Profile</h1>
        <form
          onSubmit={handleSubmit}
          className="flex flex-col items-center gap-6"
        >
          <div className="w-32 h-32 relative mb-4">
            <Image
              src={
                authUser && authUser.user && authUser.user.image
                  ? `${process.env.NEXT_PUBLIC_BACKEND_URL}/${authUser.user.image}`
                  : '/img/default-user.png'
              }
              alt="User profile image"
              fill={true}
              className="rounded-full"
              unoptimized
            />
          </div>
          <input
            type="file"
            onChange={handleImageChange}
            className="file:py-2 file:px-4 file:rounded file:border-0 file:text-sm file:font-semibold file:bg-blue-500 file:text-white hover:file:bg-blue-600"
          />
          <button
            type="submit"
            className="py-2 px-4 bg-blue-500 text-white rounded hover:bg-blue-600 transition-all duration-300"
            disabled={loading}
          >
            {loading ? 'Updating...' : 'Update Image'}
          </button>
          <div className="text-lg space-y-2">
            <p>
              <span className="font-medium">Username:</span>{' '}
              <span className="font-bold text-purple-200">
                {user.user.username}
              </span>
            </p>
            <p>
              <span className="font-medium">Email:</span>
              <span className="font-bold text-purple-200">
                {' '}
                {user.user.email}
              </span>
            </p>
          </div>
          <div className="flex flex-row gap-3">
            <Link
              href={'/user/edit'}
              className="inline-block mt-4 bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600 transition-all duration-300"
            >
              Edit Profile
            </Link>
            <Link
              href={'/user/password'}
              className="inline-block mt-4 bg-slate-700 text-white py-2 px-4 rounded hover:bg-slate-600 transition-all duration-300"
            >
              Change password
            </Link>
          </div>
        </form>
      </section>
    </main>
  );
};

export default User;
