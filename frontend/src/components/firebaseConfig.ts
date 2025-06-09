// src/firebaseConfig.ts

import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';

// Tvoje Firebase nastavitve (jih dobiš v Firebase Console → Project Settings → Your Apps)
const firebaseConfig = {
  apiKey: "AIzaSyAPAsL7ht50WmKaIGpp7VjIFUTTW_PHa0A",
  authDomain: "persona-animal-3b28b.firebaseapp.com",
  projectId: "persona-animal-3b28b",
  storageBucket: "persona-animal-3b28b.firebasestorage.app",
  messagingSenderId: "1097385101879",
  appId: "1:1097385101879:web:d7ad16cd8380ebcf0b8db2",
  measurementId: "G-GEKTD652SJ"
};

// Inicializiramo Firebase aplikacijo
const app = initializeApp(firebaseConfig);

// Izvozimo auth instanco, da jo lahko uporabiš drugje
export const auth = getAuth(app);