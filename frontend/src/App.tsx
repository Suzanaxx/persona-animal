import { useState } from 'react';
import './App.css';
import { AnimalSelector } from "./components/AnimalSelector";
import { Compatibility } from "./components/Compatibility";
import { SelectorHistory } from "./components/SelectorHistory";

type Page = 'domov' | 'samoocenitev' | 'oceni' | 'zgodovina';
type Kategorija = 'živali' | 'rastline' | 'vozila';

export const App = () => {
  const [activePage, setActivePage] = useState<Page>('domov');
  const [selectedCategory, setSelectedCategory] = useState<Kategorija | null>(null);
  const [menuOpen, setMenuOpen] = useState(false);

  const handleCategorySelect = (kategorija: Kategorija) => {
    setSelectedCategory(kategorija);
    setActivePage('samoocenitev');
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
                onClick={() => {
                  setActivePage('oceni');
                  setMenuOpen(false);
                }}
              >
                Oceni drugega
              </button>
            </li>
            <li>
              <button
                className={activePage === 'zgodovina' ? 'menu-btn active' : 'menu-btn'}
                onClick={() => {
                  setActivePage('zgodovina');
                  setMenuOpen(false);
                }}
              >
                Zgodovina
              </button>
            </li>
          </ul>
        </nav>

        <main className="content">
          {activePage === 'domov' && renderCategoryCards()}
          {activePage === 'samoocenitev' && selectedCategory && (
            <AnimalSelector
              category={selectedCategory}
              onSelectionComplete={() => {}}
            />
          )}
          {activePage === 'oceni' && <Compatibility />}
          {activePage === 'zgodovina' && <SelectorHistory />}
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
    <img src={`/${image}`} alt={name} />
    <h3>{name}</h3>
  </div>
);

export default App;
