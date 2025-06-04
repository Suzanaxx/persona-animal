import React, { useState } from 'react';
import './Auth.css';
import { auth } from './firebaseConfig';  // Uvozimo auth iz firebaseConfig.ts
import {
  createUserWithEmailAndPassword,
  signInWithEmailAndPassword,
  type UserCredential
} from 'firebase/auth';

interface AuthProps {
  onLoginSuccess: (userInfo: { id: string; username: string }) => void;
}

const Auth: React.FC<AuthProps> = ({ onLoginSuccess }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isRegistering, setIsRegistering] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    try {
      let userCred: UserCredential;

      if (isRegistering) {
        // Registracija
        userCred = await createUserWithEmailAndPassword(auth, email, password);
      } else {
        // Prijava
        userCred = await signInWithEmailAndPassword(auth, email, password);
      }

      // Po uspehu prijave/registracije pošljemo nazaj ID in email
      const firebaseUser = userCred.user;
      onLoginSuccess({
        id: firebaseUser.uid,
        username: firebaseUser.email || firebaseUser.uid,
      });
    } catch (err: any) {
      setError(err.message);
    }
  };

  return (
    <div className="auth-container">
      <h2>{isRegistering ? 'Registracija' : 'Prijava'}</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            placeholder="Vpiši email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label>Geslo:</label>
          <input
            type="password"
            placeholder="Vpiši geslo"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            minLength={6}
          />
        </div>

        {error && <p className="error">{error}</p>}

        <button type="submit" className="submit-button">
          {isRegistering ? 'Registriraj se' : 'Prijavi se'}
        </button>
      </form>

      <p className="toggle-text">
        {isRegistering ? 'Imaš že račun?' : 'Še nimaš računa?'}
        <button
          className="toggle-button"
          onClick={() => {
            setError(null);
            setIsRegistering(!isRegistering);
          }}
        >
          {isRegistering ? ' Prijavi se' : ' Registriraj se'}
        </button>
      </p>
    </div>
  );
};

export default Auth;
