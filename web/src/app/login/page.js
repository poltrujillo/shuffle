'use client';

import React, { useState } from 'react';
import { Input, Button } from '@nextui-org/react';
import Image from 'next/image';
import logo from './../../../public/logo/LightLogo.svg';
import Link from 'next/link';
import { validateCredentialsLogin } from '@/utils/credentialsValidation';
import { useAuth } from '@/context/AuthContext';
import { toast } from 'react-toastify';
import { ScaleLoader } from 'react-spinners';

const Login = () => {
  const { login } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleLogin = async () => {
    setIsLoading(true);
    const { emailMessage, passwordMessage } = validateCredentialsLogin(
      email,
      password
    );

    if (emailMessage || passwordMessage) {
      toast.error(emailMessage || passwordMessage);
      setIsLoading(false);
    } else {
      try {
        await login(email, password);
        toast.success('Logged in successfully', {
          autoclose: 500,
        });
      } catch (error) {
        toast.error('Failed to log in, please try again.');
        console.error('Login error:', error);
      } finally {
        setIsLoading(false);
      }
    }
  };

  return (
    <div className="h-screen w-screen flex flex-row justify-center items-center gap-20">
      <form
        className="flex flex-col gap-4 w-4/12"
        onSubmit={(e) => e.preventDefault()}
      >
        <Image
          src={logo}
          alt="Logo image for the login page"
          width={250}
          height={250}
          className="mx-auto mb-5"
        />
        <Input
          type="email"
          label="Email"
          onChange={(e) => setEmail(e.target.value)}
          value={email}
          disabled={isLoading}
        />
        <Input
          type="password"
          label="Password"
          onChange={(e) => setPassword(e.target.value)}
          value={password}
          disabled={isLoading}
        />
        <div className="w-full text-right">
          Forgot your{' '}
          <Link className="underline font-bold" href="/forgot-password">
            password
          </Link>
          ?
        </div>
        <Button
          onPress={handleLogin}
          className="bg-gradient-to-tr from-purple-500 to-pink-700 text-white shadow-lg"
          disabled={isLoading}
        >
          {isLoading ? <ScaleLoader color="#ffffff" /> : 'Login'}
        </Button>
        <div className="w-full text-center">
          Don&apos;t have an account?{' '}
          <Link className="underline font-bold" href="/register">
            Register
          </Link>
        </div>
      </form>
    </div>
  );
};

export default Login;
