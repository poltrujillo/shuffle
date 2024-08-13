'use server';

import { cookies } from 'next/headers';

const cookieService = {
  save: (key, value) => {
    if (typeof window === 'undefined') {
      return;
    }

    try {
      cookies.set(key, value);
    } catch (error) {
      console.error('Error saving to cookies', error);
    }
  },

  get: (key) => {
    if (typeof window === 'undefined') {
      return;
    }

    try {
      return cookies.get(key);
    } catch (error) {
      console.error('Error getting data from cookies', error);
    }
  },

  remove: (key) => {
    if (typeof window === 'undefined') {
      return;
    }

    try {
      cookies.remove(key);
    } catch (error) {
      console.error('Error removing data from cookies', error);
    }
  },
};

export default cookieService;
