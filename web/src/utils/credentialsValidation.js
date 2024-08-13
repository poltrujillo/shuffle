export function validateCredentialsLogin(email, password) {
  let emailMessage = '';
  let passwordMessage = '';

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (emailRegex.test(email)) {
    emailMessage = '';
  } else {
    emailMessage = 'Email is invalid. Please enter a valid email address.';
  }

  const passwordRegex =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
  if (passwordRegex.test(password)) {
    passwordMessage = '';
  } else {
    passwordMessage =
      'Password is invalid. It must contain at least one uppercase letter, one lowercase letter, one number, one special character, and be at least 8 characters long.';
  }

  return { emailMessage, passwordMessage };
}

export function validateCredentialsRegister(email, password, confirmPassword) {
  let emailMessage = '';
  let passwordMessage = '';
  let confirmPasswordMessage = '';

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (emailRegex.test(email)) {
    emailMessage = '';
  } else {
    emailMessage = 'Email is invalid. Please enter a valid email address.';
  }

  const passwordRegex =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
  if (passwordRegex.test(password)) {
    passwordMessage = '';
  } else {
    passwordMessage =
      'Password is invalid. It must contain at least one uppercase letter, one lowercase letter, one number, one special character, and be at least 8 characters long.';
  }

  if (password === confirmPassword) {
    confirmPasswordMessage = '';
  } else {
    confirmPasswordMessage = 'Passwords do not match.';
  }

  return { emailMessage, passwordMessage, confirmPasswordMessage };
}
