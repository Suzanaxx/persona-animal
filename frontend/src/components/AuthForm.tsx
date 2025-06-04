import React, { useState } from 'react';
import { getAuth, createUserWithEmailAndPassword, signInWithEmailAndPassword } from 'firebase/auth';
import type { UserCredential } from "firebase/auth";

interface AuthFormProps {
  onLoginSuccess: (user: UserCredential) => void;
}

const AuthForm: React.FC<AuthFormProps> = ({ onLoginSuccess }) => {
  const [isRegistering, setIsRegistering] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [error, setError] = useState<string | null>(null);
  const auth = getAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    try {
      if (isRegistering) {
        // Registracija v Firebase
        const userCredential = await createUserWithEmailAndPassword(auth, email, password);

        // Pošlji dodatne podatke na backend (npr. name)
        await fetch('/users', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            firebaseUid: userCredential.user.uid,
            username: email,
            name,
          }),
        });

        onLoginSuccess(userCredential);
      } else {
        // Prijava v Firebase
        const userCredential = await signInWithEmailAndPassword(auth, email, password);
        onLoginSuccess(userCredential);
      }
    } catch (err: any) {
      setError(err.message);
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: 'auto' }}>
      <h2>{isRegistering ? 'Registracija' : 'Prijava'}</h2>
      <form onSubmit={handleSubmit}>
        {isRegistering && (
          <div>
            <label>Ime:</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>
        )}

        <div>
          <label>Email:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>

        <div>
          <label>Geslo:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            minLength={6}
          />
        </div>

        {error && <p style={{ color: 'red' }}>{error}</p>}

        <button type="submit">{isRegistering ? 'Registriraj se' : 'Prijavi se'}</button>
      </form>

      <p style={{ marginTop: 10 }}>
        {isRegistering ? 'Imaš že račun?' : 'Še nimaš računa?'}{' '}
        <button onClick={() => setIsRegistering(!isRegistering)} style={{ cursor: 'pointer', color: 'blue', background: 'none', border: 'none' }}>
          {isRegistering ? 'Prijavi se' : 'Registriraj se'}
        </button>
      </p>
    </div>
  );
};

export default AuthForm;
