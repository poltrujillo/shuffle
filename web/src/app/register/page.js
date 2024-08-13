'use client';

import React, { useState } from 'react';
import { Input, Button } from '@nextui-org/react';
import Image from 'next/image';
import logo from './../../../public/logo/LightLogo.svg';
import Link from 'next/link';
import { toast } from 'react-toastify';
import { validateCredentialsRegister } from '@/utils/credentialsValidation';
import { useAuth } from '@/context/AuthContext';
import { ScaleLoader } from 'react-spinners';

const Register = () => {
  const { register } = useAuth();
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleRegister = async () => {
    setIsLoading(true);
    const { emailMessage, passwordMessage, confirmPasswordMessage } =
      validateCredentialsRegister(email, password, confirmPassword);

    if (emailMessage || passwordMessage || confirmPasswordMessage) {
      toast.error(emailMessage || passwordMessage || confirmPasswordMessage);
      setIsLoading(false);
    } else {
      try {
        const user = await register(username, email, password);
        toast.success('Registered successfully', {
          autoclose: 500,
        });
      } catch (error) {
        toast.error('Failed to register, please try again.');
        console.error('Register error:', error);
      } finally {
        setIsLoading(false);
      }
    }
  };

  return (
    <div className="h-screen w-screen flex flex-row justify-center items-center gap-20">
      <form className="flex flex-col gap-4 w-4/12">
        <Image
          src={logo}
          alt="Logo image for the login page"
          width={250}
          height={250}
          className="mx-auto mb-5"
        />
        <Input
          type="text"
          label="Username"
          onChange={(e) => setUsername(e.target.value)}
        />
        <Input
          type="email"
          label="Email"
          onChange={(e) => setEmail(e.target.value)}
        />
        <Input
          type="password"
          label="Password"
          onChange={(e) => setPassword(e.target.value)}
        />
        <Input
          type="password"
          label="Confirm Password"
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
        <Button
          onPress={handleRegister}
          className="bg-gradient-to-tr from-purple-500 to-pink-700 text-white shadow-lg"
          disabled={isLoading}
        >
          {isLoading ? <ScaleLoader color="#ffffff" /> : 'Register'}
        </Button>
        <div className="w-full text-center">
          Already have an account?{' '}
          <Link className="underline font-bold" href="/login">
            Log in
          </Link>
        </div>
      </form>
    </div>
  );
};

export default Register;
