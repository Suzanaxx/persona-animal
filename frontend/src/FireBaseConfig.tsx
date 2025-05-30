// src/firebaseConfig.ts

import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider } from "firebase/auth";

const firebaseConfig = {
  apiKey: "TU_LEPI ŠE_SVOJO_apiKey",
  authDomain: "TU_LEPI ŠE_SVOJO_authDomain",
  projectId: "TU_LEPI ŠE_SVOJO_projectId",
  storageBucket: "TU_LEPI ŠE_SVOJO_storageBucket",
  messagingSenderId: "TU_LEPI ŠE_SVOJO_messagingSenderId",
  appId: "TU_LEPI ŠE_SVOJO_appId",
  measurementId: "TU_LEPI ŠE_SVOJO_measurementId"
};

// Inicializiramo Firebase App
const app = initializeApp(firebaseConfig);

// Po potrebi omogočimo še authentication (npr. Google)
export const auth = getAuth(app);
export const googleProvider = new GoogleAuthProvider();

