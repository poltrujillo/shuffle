// Import the functions you need from the SDKs you need
import { initializeApp } from 'firebase/app';
import { getFirestore } from 'firebase/firestore';
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional

const firebaseConfig = {
  apiKey: 'AIzaSyBeoT3pTKiMgvCqeQrJojYS516mT5sf-fM',
  authDomain: 'shuffle-60501.firebaseapp.com',
  projectId: 'shuffle-60501',
  storageBucket: 'shuffle-60501.appspot.com',
  messagingSenderId: '851000526656',
  appId: '1:851000526656:web:7977305ba7366b81235ca5',
  measurementId: 'G-8YHBFDMLR1',
};

// Initialize Firebase

const app = initializeApp(firebaseConfig);
const db = getFirestore(app);

export { app, db };
