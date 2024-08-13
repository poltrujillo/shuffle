'use client';

import React, { useState, useEffect } from 'react';
import Link from 'next/link';
import { useAuth } from '@/context/AuthContext';
import { getItem } from '@/utils/localStorage';
import Swal from 'sweetalert2';
import userService from '@/services/userService';
import { ScaleLoader } from 'react-spinners';
import { useRouter } from 'next/navigation';

const Password = () => {
  const router = useRouter();
  const { user } = useAuth();
  const [oldPassword, setOldPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [authUser, setAuthUser] = useState(user);
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  useEffect(() => {
    if (user) {
      getItem('token');
      setAuthUser(user);
    }
  }, [user]);

  const handleClick = async (e) => {
    e.preventDefault();
    setLoading(true);
    if (password !== confirmPassword) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Passwords do not match!',
      });
      setLoading(false);
      return;
    }

    try {
      const response = await userService.updatePassword(authUser.user.id, {
        old_password: oldPassword,
        password: password,
      });
      if (!response) {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Failed to update password',
        });
      } else {
        Swal.fire({
          title: 'Loading...',
          allowOutsideClick: false,
          onBeforeOpen: () => {
            Swal.showLoading();
          },
        });
        Swal.fire({
          icon: 'success',
          title: 'Password updated successfully',
        }).then((result) => {
          if (result.isConfirmed) {
            router.push('/user');
          }
        });
      }
    } catch (error) {
      console.error('Error updating password:', error);
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Failed to update password',
      });
    }
    setLoading(false);
  };

  if (loading) {
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
          Change Password
        </h1>
        <div className="md:flex md:items-center mb-6">
          <div className="md:w-1/3">
            <label
              className="block text-gray-500 font-bold md:text-right mb-1 md:mb-0 pr-4"
              htmlFor="old_password"
            >
              Old Password
            </label>
          </div>
          <div className="md:w-2/3">
            <input
              onChange={(e) => setOldPassword(e.target.value)}
              className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4 text-gray-700 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"
              type="password"
              id="old_password"
              name="old_password"
              value={oldPassword}
            />
          </div>
        </div>
        <div className="md:flex md:items-center mb-6">
          <div className="md:w-1/3">
            <label
              className="block text-gray-500 font-bold md:text-right mb-1 md:mb-0 pr-4"
              htmlFor="password"
            >
              New Password
            </label>
          </div>
          <div className="md:w-2/3">
            <input
              onChange={(e) => setPassword(e.target.value)}
              className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4 text-gray-700 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"
              type="password"
              id="password"
              name="password"
              value={password}
            />
          </div>
        </div>
        <div className="md:flex md:items-center mb-6">
          <div className="md:w-1/3">
            <label
              className="block text-gray-500 font-bold md:text-right mb-1 md:mb-0 pr-4"
              htmlFor="confirm_password"
            >
              Confirm New Password
            </label>
          </div>
          <div className="md:w-2/3">
            <input
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4 text-gray-700 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"
              type="password"
              id="confirm_password"
              name="confirm_password"
              value={confirmPassword}
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

export default Password;
