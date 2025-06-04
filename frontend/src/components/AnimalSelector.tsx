import { useEffect, useState } from 'react';

interface Animal {
  id: string;
  name: string;
  imageUrl: string;
}

interface AnimalTraitDTO {
  traitId: number;
  description: string;
  positive: boolean;
}

interface AnimalSelectorProps {
  category: 'živali' | 'rastline' | 'vozila';
  onSelectionComplete: (animalId: string) => void;
}

const API_BASE_URL = 'http://localhost:8080';
const MAX_KLIKOV = 5;

export const AnimalSelector = ({ category, onSelectionComplete }: AnimalSelectorProps) => {
  const [pool, setPool] = useState<Animal[]>([]);
  const [currentPair, setCurrentPair] = useState<Animal[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [clickCount, setClickCount] = useState(0);
  const [selectedAnimal, setSelectedAnimal] = useState<Animal | null>(null);
  const [traits, setTraits] = useState<AnimalTraitDTO[]>([]);
  const [loadingTraits, setLoadingTraits] = useState(false);

  useEffect(() => {
    (async () => {
      try {
        const res = await fetch(`${API_BASE_URL}/api/animals`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data: Animal[] = await res.json();
        const shuffled = data.sort(() => Math.random() - 0.5);
        setPool(shuffled.slice(2));
        setCurrentPair(shuffled.slice(0, 2));
      } catch (err) {
        setError('Napaka pri nalaganju živali: ' + err);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  const loadAnimals = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await fetch(`${API_BASE_URL}/api/animals?category=${category}`);
      if (!res.ok) throw new Error(`Napaka ${res.status}`);
      const data: Animal[] = await res.json();
      const shuffled = data.sort(() => Math.random() - 0.5);
      setPool(shuffled.slice(2));
      setCurrentPair(shuffled.slice(0, 2));
    } catch (err) {
      setError('Napaka pri nalaganju: ' + err);
    } finally {
      setLoading(false);
    }
  };

  const handleSelect = async (animal: Animal) => {
    if (loadingTraits) return;

    const newClickCount = clickCount + 1;
    setClickCount(newClickCount);

    const newPool = pool.filter(a => !currentPair.some(c => c.id === a.id));
    setPool(newPool);

    if (newClickCount === MAX_KLIKOV) {
      setSelectedAnimal(animal);
      setLoadingTraits(true);
      try {
        const res = await fetch(`${API_BASE_URL}/api/animal_traits/traits/${animal.id}`);
        if (!res.ok) throw new Error(`Napaka ${res.status}`);
        const data: AnimalTraitDTO[] = await res.json();
        setTraits(data);
        onSelectionComplete(animal.id);
      } catch (err) {
        setError(String(err));
      } finally {
        setLoadingTraits(false);
      }
    } else {
      const shuffled = newPool.sort(() => Math.random() - 0.5);
      setCurrentPair(shuffled.slice(0, 2));
      setPool(shuffled.slice(2));
    }
  };

  const handleReset = () => {
    setClickCount(0);
    setSelectedAnimal(null);
    setTraits([]);
    loadAnimals();
  };

    const handleSaveAssessment = async () => {
    if (!selectedAnimal) return;

    try {
      const response = await fetch(`${API_BASE_URL}/api/history/self-assessment`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",        // ← to vrne
        body: JSON.stringify({ animalId: Number(selectedAnimal.id) }),
      });

      if (!response.ok) {
        
        throw new Error(`HTTP ${response.status}`);
      }

      alert('Samoocenitev uspešno shranjena v zgodovino!');
    } catch (err) {
      alert('Napaka pri shranjevanju samoocenitve: ' + err);
    }
  };


  if (loading) return <div className="animal-selector"><p>Nalagam...</p></div>;
  if (error) return <div className="error">{error}</div>;

  if (selectedAnimal) {
    return (
      <div className="animal-detail">
        <div className="animal-summary">
          <h2>Izbrali ste: <span className="highlighted">{selectedAnimal.name}</span></h2>
          <img
            src={`${API_BASE_URL}${selectedAnimal.imageUrl}`}
            alt={selectedAnimal.name}
            className="animal-image-large"
          />
        </div>
        {loadingTraits ? (
          <p>Nalagam lastnosti...</p>
        ) : (
          <>
            <table className="traits-table-centered">
              <thead>
                <tr>
                  <th>Pozitivne lastnosti</th>
                  <th>Negativne lastnosti</th>
                </tr>
              </thead>
              <tbody>
                {Array.from({
                  length: Math.max(
                    traits.filter(t => t.positive).length,
                    traits.filter(t => !t.positive).length,
                  )
                }).map((_, index) => (
                  <tr key={index}>
                    <td>{traits.filter(t => t.positive)[index]?.description || ''}</td>
                    <td>{traits.filter(t => !t.positive)[index]?.description || ''}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div className="action-buttons">
              <button className="primary" onClick={handleSaveAssessment}>Shrani samoocenitev</button>
              <button className="secondary" onClick={handleReset}>Poskusi ponovno</button>
            </div>
          </>
        )}
      </div>
    );
  }

  return (
    <div className="animal-selector">
      <h2 className="section-title">Klikni na sliko, ki te najbolje upodobi:</h2>
      <div className="progress-container">
        <div
          className="progress-fill"
          style={{ width: `${(clickCount / MAX_KLIKOV) * 100}%` }}
        />
      </div>
      
      <div className="animal-pair">
        {currentPair.map(animal => (
          <button
            key={animal.id}
            onClick={() => handleSelect(animal)}
            className="animal-card"
            disabled={loadingTraits}
          >
            <img
              src={`${API_BASE_URL}${animal.imageUrl}`}
              alt={animal.name}
              className="animal-image"
            />
            
          </button>
        ))}
      </div>
      <div style={{ marginTop: '1rem', textAlign: 'center' }}>
      </div>
    </div>
  );
};
