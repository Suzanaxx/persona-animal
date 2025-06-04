// src/App.tsx

import React, { useState, useEffect } from 'react';
import './App.css';
import { AnimalSelector } from './components/AnimalSelector';
import { Compatibility } from './components/Compatibility';
import { SelectorHistory } from './components/SelectorHistory';
import Auth from './components/Auth'; // prijava/registracija

type Page = 'domov' | 'samoocenitev' | 'oceni' | 'zgodovina' | 'prijava';
type Kategorija = 'živali' | 'rastline' | 'vozila';

interface UserInfo {
  id: string;
  username: string;
}

export const App = () => {
  const [activePage, setActivePage] = useState<Page>('domov');
  const [selectedCategory, setSelectedCategory] = useState<Kategorija | null>(null);
  const [menuOpen, setMenuOpen] = useState(false);

  // State za prijavljenega uporabnika
  const [user, setUser] = useState<UserInfo | null>(null);

  // State za sporočilo (info)
  const [infoMessage, setInfoMessage] = useState<string | null>(null);

  // Ob zagonu aplikacije preverimo localStorage, če je uporabnik že prijavljen
  useEffect(() => {
    const stored = localStorage.getItem('user');
    if (stored) {
      try {
        setUser(JSON.parse(stored));
      } catch {
        localStorage.removeItem('user');
      }
    }
  }, []);

  // Funkcija za prikaz sporočila in samodejni izbris po 3 sekundah
  const showInfoMessage = (msg: string) => {
    setInfoMessage(msg);
    setTimeout(() => setInfoMessage(null), 3000);
  };

  // Ko se uporabnik uspešno prijavi/registrira, pokličemo to funkcijo
  const handleLoginSuccess = (userInfo: UserInfo) => {
    setUser(userInfo);
    localStorage.setItem('user', JSON.stringify(userInfo));
    setActivePage('domov');
  };

  // Odjava
  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('user');
    setActivePage('domov');
  };

  const handleCategorySelect = (kategorija: Kategorija) => {
    setSelectedCategory(kategorija);
    setActivePage('samoocenitev');
    setMenuOpen(false);
  };

  // Funkcija za gumb Zgodovina z obravnavo prijave
  const handleZgodovinaClick = () => {
    if (!user) {
      showInfoMessage('Prosim prijavite se za možnost zgodovine.');
      setMenuOpen(false);
      return;
    }
    setActivePage('zgodovina');
    setMenuOpen(false);
  };

  // Funkcija za gumb Oceni drugega z obravnavo prijave
  const handleOceniClick = () => {
    if (!user) {
      showInfoMessage('Za ocenjevanje drugih se moraš najprej prijaviti.');
      setMenuOpen(false);
      return;
    }
    setActivePage('oceni');
    setMenuOpen(false);
  };

  const renderCategoryCards = () => (
    <div className="category-select">
      <h2>Izberi kategorijo za svojo samooceno</h2>
      <div className="card-container">
        <CategoryCard name="Živali" image="zivali.jpg" onClick={() => handleCategorySelect('živali')} />
        <CategoryCard name="Rastline" image="rastline.jpg" onClick={() => handleCategorySelect('rastline')} />
        <CategoryCard name="Prevozna sredstva" image="vozila.jpg" onClick={() => handleCategorySelect('vozila')} />
      </div>
    </div>
  );

  return (
    <div className="app">
      {/* Prikaz sporočila */}
      {infoMessage && (
        <div className="info-message">
          {infoMessage}
        </div>
      )}

      {/* Hamburger / meni gumb */}
      <div className="hamburger-container">
        <button className="hamburger" onClick={() => setMenuOpen(!menuOpen)}>
          ☰
        </button>
      </div>

      <div className="layout">
        {/* Navigacija */}
        <nav className={`menu ${menuOpen ? 'open' : ''}`}>
          <ul>
            <li>
              <button
                className={activePage === 'domov' ? 'menu-btn active' : 'menu-btn'}
                onClick={() => {
                  setActivePage('domov');
                  setMenuOpen(false);
                }}
              >
                Domov
              </button>
            </li>
            <li>
              <button
                className={activePage === 'oceni' ? 'menu-btn active' : 'menu-btn'}
                onClick={handleOceniClick}
              >
                Oceni drugega
              </button>
            </li>
            <li>
              <button
                className={activePage === 'zgodovina' ? 'menu-btn active' : 'menu-btn'}
                onClick={handleZgodovinaClick}
              >
                Zgodovina
              </button>
            </li>
          </ul>

          {/* Desni del navigacije: profil / prijava */}
          <div className="profile-section">
            {user ? (
              <button onClick={handleLogout} className="logout-button">
                👤 {user.username} (Odjava)
              </button>
            ) : (
              <button
                onClick={() => {
                  setActivePage('prijava');
                  setMenuOpen(false);
                }}
                className="login-button"
              >
                Prijava
              </button>
            )}
          </div>
        </nav>

        {/* Glavni del */}
        <main className="content">
          {activePage === 'domov' && renderCategoryCards()}

          {activePage === 'samoocenitev' && (
            <>
              {selectedCategory ? (
                // Tukaj zdaj posredujemo user kot prop
                <AnimalSelector category={selectedCategory} user={user} />
              ) : (
                <p>Najprej izberi kategorijo na strani Domov.</p>
              )}
            </>
          )}

          {activePage === 'oceni' && (
            <>
              {!user ? (
                <p className="must-login-msg">Za ocenjevanje drugih se moraš najprej prijaviti.</p>
              ) : (
                <Compatibility />
              )}
            </>
          )}

          {activePage === 'zgodovina' && (
            <>
              {!user ? (
                <p className="must-login-msg">Za ogled zgodovine se moraš najprej prijaviti.</p>
              ) : (
                <SelectorHistory />
              )}
            </>
          )}

          {activePage === 'prijava' && (
            // Prikaže komponento Auth, ki ob uspehu kliče handleLoginSuccess
            <Auth onLoginSuccess={handleLoginSuccess} />
          )}
        </main>
      </div>
    </div>
  );
};

interface CategoryCardProps {
  name: string;
  image: string;
  onClick: () => void;
}

const CategoryCard = ({ name, image, onClick }: CategoryCardProps) => (
  <div className="category-card" onClick={onClick}>
    <img src={`/${image}`} alt={name} className="category-image" />
    <h3>{name}</h3>
  </div>
);

export default App;
