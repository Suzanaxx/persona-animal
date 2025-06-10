import React, { useState, useEffect } from 'react';
import './App.css';
import { AnimalSelector } from './components/AnimalSelector';
import { Compatibility } from './components/Compatibility';
import { SelectorHistory } from './components/SelectorHistory';
import Auth from './components/Auth';
import { Toaster } from 'sonner';
import 'react-toastify/dist/ReactToastify.css';

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
  const [logoutOpen, setLogoutOpen] = useState(false); // Novo stanje za dropdown

  const [user, setUser] = useState<UserInfo | null>(null);
  const [infoMessage, setInfoMessage] = useState<string | null>(null);

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

  const showInfoMessage = (msg: string) => {
    setInfoMessage(msg);
    setTimeout(() => setInfoMessage(null), 3000);
  };

  const handleLoginSuccess = (userInfo: UserInfo) => {
    setUser(userInfo);
    localStorage.setItem('user', JSON.stringify(userInfo));
    setActivePage('domov');
  };

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('user');
    setActivePage('domov');
    setLogoutOpen(false); // Zapri dropdown po odjavi
  };

  const handleCategorySelect = (kategorija: Kategorija) => {
    setSelectedCategory(kategorija);
    setActivePage('samoocenitev');
    setMenuOpen(false);
  };

  const handleZgodovinaClick = () => {
    if (!user) {
      showInfoMessage('Prosim prijavite se za možnost zgodovine.');
      setMenuOpen(false);
      return;
    }
    setActivePage('zgodovina');
    setMenuOpen(false);
  };

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
      <Toaster />
      {infoMessage && <div className="info-message">{infoMessage}</div>}
      <div className="hamburger-container">
        <button className="hamburger" onClick={() => setMenuOpen(!menuOpen)}>
          ☰
        </button>
      </div>
      <div className="layout">
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
          <div className="profile-section">
            {user ? (
              <div className="profile-dropdown">
                <span className="username" onClick={() => setLogoutOpen(!logoutOpen)}>
                  👤 {user.username}
                </span>
                {logoutOpen && (
                  <button onClick={handleLogout} className="logout-btn">
                    Odjava
                  </button>
                )}
              </div>
            ) : (
              <button
                onClick={() => {
                  setActivePage('prijava');
                  setMenuOpen(false);
                }}
                className="menu-btn"
              >
                Prijava
              </button>
            )}
          </div>
        </nav>
        <main className="content">
          {activePage === 'domov' && renderCategoryCards()}
          {activePage === 'samoocenitev' && (
            <>
              {selectedCategory ? (
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
          {activePage === 'prijava' && <Auth onLoginSuccess={handleLoginSuccess} />}
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