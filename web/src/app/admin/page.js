'use client';

import Image from 'next/image';
import React, { useEffect, useState } from 'react';
import { AiFillDelete } from 'react-icons/ai';
import { FaStar } from 'react-icons/fa';
import { FaRegStar } from 'react-icons/fa';
import userService from '@/services/userService';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/context/AuthContext';
import { toast } from 'react-toastify';
import { ScaleLoader } from 'react-spinners';

const Dashboard = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const [usersPerPage] = useState(5);
  const [users, setUsers] = useState([]);
  const [isFetching, setIsFetching] = useState(false);
  const [isGivingAdmin, setIsGivingAdmin] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const { user, isAdmin, loading } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (loading) {
      return;
    }

    if (!isAdmin()) {
      console.log('User is not an admin', user, isAdmin());
      router.push('/');
      return;
    }

    let isCancelled = false;

    const fetchUsers = async () => {
      setIsFetching(true);
      try {
        const fetchedUsers = await userService.getUsers();
        if (!isCancelled) {
          setUsers(fetchedUsers);
        }
      } catch (error) {
        if (!isCancelled) {
          console.error('Error fetching users:', error);
        }
      }
      if (!isCancelled) {
        setIsFetching(false);
      }
    };

    fetchUsers();

    return () => {
      isCancelled = true;
    };
  }, [user, isAdmin, router, loading]);

  const indexOfLastUser = currentPage * usersPerPage;
  const indexOfFirstUser = indexOfLastUser - usersPerPage;
  const currentUsers = users.slice(indexOfFirstUser, indexOfLastUser);

  const getImageSrc = (userImage) => {
    if (userImage) {
      return `http://localhost:8000/${userImage}`;
    }
    return '/img/default-user.png';
  };

  const handleImageError = (e) => {
    if (!e.target.src.endsWith('/img/default-user.png')) {
      e.target.src = '/img/default-user.png';
    }
    e.target.onError = null;
  };

  const handleDeleteUser = async (userId) => {
    setIsDeleting(true);
    try {
      await userService.deleteUser(userId);
      const updatedUsers = users.filter((user) => user.id !== userId);
      setUsers(updatedUsers);
      toast.success('User deleted successfully');
    } catch (error) {
      console.error('Error deleting user:', error);
      toast.error('Error deleting user');
    }
    setIsDeleting(false);
  };

  const handleAdminUser = async (userId) => {
    setIsGivingAdmin(true);
    try {
      await userService.updateAdmin(userId);
      const updatedUsers = users.map((user) => {
        if (user.id === userId) {
          return { ...user, is_admin: true };
        }
        return user;
      });
      setUsers(updatedUsers);
      toast.success('User is now an admin');
    } catch (error) {
      console.error('Error updating user:', error);
      toast.error('Error updating user');
    }
    setIsGivingAdmin(false);
  };

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  if (isFetching) {
    return (
      <div className="h-screen flex justify-center items-center">
        <ScaleLoader color="#ffffff" />
      </div>
    );
  }

  if (isDeleting) {
    return (
      <div className="h-screen flex justify-center items-center">
        <ScaleLoader color="#ffffff" />
      </div>
    );
  }

  if (isGivingAdmin) {
    return (
      <div className="h-screen flex justify-center items-center">
        <ScaleLoader color="#ffffff" />
      </div>
    );
  }

  return (
    <div className="min-h-screen p-5 w-screen">
      <div className="container w-2/3 mx-auto">
        <h2 className="text-2xl font-semibold leading-tight text-center mb-4">
          Dashboard
        </h2>
        <div className="overflow-x-auto mt-6">
          <table className="min-w-full bg-white table-fixed">
            <thead>
              <tr className="border-b">
                <th className="w-1/3 px-5 py-3 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                  User
                </th>
                <th className="w-1/3 px-5 py-3 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                  Email
                </th>
                <th className="w-1/3 px-5 py-3 border-b-2 border-gray-200 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody>
              {currentUsers.map((user) => (
                <tr key={user.id} className="border-b">
                  <td className="px-5 py-2 border-gray-200 text-sm">
                    <div className="flex items-center">
                      <div className="flex-shrink-0">
                        <Image
                          src={getImageSrc(user.image)}
                          alt="User profile image"
                          width={40}
                          height={40}
                          className="rounded-full"
                          onError={handleImageError}
                        />
                      </div>
                      <div className="ml-3">
                        <p className="text-gray-900 whitespace-no-wrap">
                          {user.username}
                        </p>
                        <p className="text-xs text-gray-600">
                          Created: {user.created_at}
                        </p>
                        <p className="text-xs text-gray-600">
                          Updated: {user.updated_at}
                        </p>
                      </div>
                    </div>
                  </td>
                  <td className="px-5 py-5 border-gray-200 text-sm">
                    <p className="text-gray-900 whitespace-no-wrap">
                      {user.email}
                    </p>
                  </td>
                  <td className="px-5 py-5 border-gray-200 text-sm flex justify-start items-center">
                    <button
                      onClick={() => handleAdminUser(user.id)}
                      className="p-2 rounded text-blue-500 hover:bg-blue-100"
                    >
                      {user.is_admin ? (
                        <FaStar size={20} />
                      ) : (
                        <FaRegStar size={20} />
                      )}
                    </button>
                    <button
                      onClick={() => handleDeleteUser(user.id)}
                      className="ml-4 p-2 rounded text-red-500 hover:bg-red-100"
                    >
                      <AiFillDelete size={20} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <div className="px-5 py-5 bg-white border-t flex flex-col xs:flex-row items-center xs:justify-between">
            <span className="text-xs xs:text-sm text-gray-900">
              Showing {indexOfFirstUser + 1} to {indexOfLastUser} of{' '}
              {users.length} Entries
            </span>
            <div className="inline-flex mt-2 xs:mt-0">
              {Array.from(
                { length: Math.ceil(users.length / usersPerPage) },
                (_, i) => i + 1
              ).map((number) => (
                <button
                  key={number}
                  onClick={() => paginate(number)}
                  className={`text-sm mx-1 bg-gray-300 hover:bg-gray-400 text-gray-800 font-semibold py-2 px-4 rounded-full ${
                    currentPage === number ? 'bg-blue-500 text-white' : ''
                  }`}
                >
                  {number}
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
