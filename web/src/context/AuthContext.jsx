'use client';

import React, { createContext, useContext, useState, useEffect } from 'react';
import AuthService from '@/services/AuthService';
import { useRouter } from 'next/navigation';
import { getItem, saveUniqueItem } from '@/utils/localStorage';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const authService = new AuthService();
  const router = useRouter();

  useEffect(() => {
    const token = getItem('token');
    setLoading(true);
    if (token) {
      authService
        .getUser(token)
        .then((fetchedUser) => {
          setUser(fetchedUser);
          setLoading(false);
        })
        .catch(() => {
          setUser(null);
          setLoading(false);
          router.push('/login');
        });
    } else {
      console.log('No token found, redirecting...');
      setLoading(false);
      router.push('/login');
    }
  }, []);

  const login = async (username, password) => {
    try {
      const userData = await authService.login(username, password);
      saveUniqueItem('token', userData.token);
      setUser(userData);
      userData.is_admin ? router.push('/admin') : router.push('/');
    } catch (error) {
      console.error('Login failed:', error);
      throw error;
    }
  };

  const register = async (username, email, password) => {
    try {
      const userData = await authService.register(username, email, password);
      saveUniqueItem('token', userData.token);
      setUser(userData);
      router.push('/');
    } catch (error) {
      console.error('Register failed:', error);
      throw error;
    }
  };

  const logout = async () => {
    try {
      await authService.logout();
      setUser(null);
      router.push('/login');
    } catch (error) {
      console.error('Logout failed:', error);
    }
  };

  const isAuthenticated = () => !!user;
  const isAdmin = () => !!user && user.user.is_admin;

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        login,
        logout,
        isAuthenticated,
        register,
        isAdmin,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export function useAuth() {
  return useContext(AuthContext);
}
