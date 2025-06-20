import { useEffect, useState } from 'react';
import { getAuth } from 'firebase/auth';

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

export type Kategorija = 'živali' | 'rastline' | 'vozila';

interface AnimalSelectorProps {
  category: Kategorija;
  user: { id: string; username: string } | null;
}

const API_BASE_URL = 'https://backend-wqgy.onrender.com';
const MAX_KLIKOV = 5;

export const AnimalSelector = ({ category, user }: AnimalSelectorProps) => {
  const [pool, setPool] = useState<Animal[]>([]);
  const [currentPair, setCurrentPair] = useState<Animal[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [clickCount, setClickCount] = useState(0);
  const [selectedAnimal, setSelectedAnimal] = useState<Animal | null>(null);
  const [traits, setTraits] = useState<AnimalTraitDTO[]>([]);
  const [loadingTraits, setLoadingTraits] = useState(false);
  const [saveAlert, setSaveAlert] = useState<string | null>(null);
  const [saveSuccess, setSaveSuccess] = useState<string | null>(null);

  const loadAnimals = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`${API_BASE_URL}/api/animals?category=${category}`, {
        credentials: 'include', // Omogoči cookies za avtentikacijo
      });
      if (!res.ok) throw new Error(`Napaka ${res.status}: ${await res.text()}`);
      const data: Animal[] = await res.json();
      const shuffled = data.sort(() => Math.random() - 0.5);
      setPool(shuffled.slice(2));
      setCurrentPair(shuffled.slice(0, 2));
    } catch (err) {
      setError('Napaka pri nalaganju živali: ' + (err as Error).message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAnimals();
    setClickCount(0);
    setSelectedAnimal(null);
    setTraits([]);
    setSaveAlert(null);
    setSaveSuccess(null);
  }, [category]);

  const handleSelect = async (animal: Animal) => {
    if (loadingTraits) return;

    const newClickCount = clickCount + 1;
    setClickCount(newClickCount);

    if (newClickCount === MAX_KLIKOV) {
      setSelectedAnimal(animal);
      setLoadingTraits(true);
      try {
        const res = await fetch(`${API_BASE_URL}/api/animal_traits/traits/${animal.id}`, {
          credentials: 'include',
        });
        if (!res.ok) throw new Error(`Napaka ${res.status}: ${await res.text()}`);
        const data: AnimalTraitDTO[] = await res.json();
        setTraits(data);
      } catch (err) {
        setError('Napaka pri nalaganju lastnosti: ' + (err as Error).message);
      } finally {
        setLoadingTraits(false);
      }
    } else {
      const newPool = pool.filter(a => !currentPair.some(c => c.id === a.id));
      const shuffled = newPool.sort(() => Math.random() - 0.5);
      setCurrentPair(shuffled.slice(0, 2));
      setPool(shuffled.slice(2));
    }
  };

  const handleReset = () => {
    setClickCount(0);
    setSelectedAnimal(null);
    setTraits([]);
    setSaveAlert(null);
    setSaveSuccess(null);
    loadAnimals();
  };

  const handleSaveAssessment = async () => {
    if (!selectedAnimal) return;

    if (!user) {
      setSaveAlert('Prosim prijavite se za možnost hranjenja zgodovine.');
      setSaveSuccess(null);
      return;
    }

    const auth = getAuth();
    const token = await auth.currentUser?.getIdToken();
    if (!token) {
      setSaveAlert('Napaka pri pridobivanju avtentikacijskega tokena.');
      setSaveSuccess(null);
      return;
    }

    try {
      console.log("Saving animal ID:", selectedAnimal.id, typeof selectedAnimal.id); // Debug
      const response = await fetch(`${API_BASE_URL}/api/history/self-assessment`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        credentials: 'include',
        body: JSON.stringify({
          animalId: Number(selectedAnimal.id) // Send only animalId
        }),
      });

      if (!response.ok) {
        const errorText = await response.text();
        console.error("Server error:", errorText); // Debug
        throw new Error(`HTTP ${response.status}: ${errorText}`);
      }

      setSaveSuccess('Samoocenitev uspešno shranjena v zgodovino!');
      setSaveAlert(null);
    } catch (err) {
      console.error("Save error:", err); // Debug
      setSaveAlert('Napaka pri shranjevanju samoocenitve: ' + (err as Error).message);
      setSaveSuccess(null);
    }
  };

  if (loading) {
    return (
      <div className="animal-selector">
        <p className="loading">Nalagam...</p>
      </div>
    );
  }
  if (error) {
    return <div className="error">{error}</div>;
  }

  if (selectedAnimal) {
    return (
      <div className="animal-detail">
        {saveAlert && <div className="alert alert-error">{saveAlert}</div>}
        {saveSuccess && <div className="alert alert-success">{saveSuccess}</div>}

        <div className="animal-summary">
          <h2>
            Izbrali ste: <span className="highlighted">{selectedAnimal.name}</span>
          </h2>
          <img
            src={`${API_BASE_URL}${selectedAnimal.imageUrl}`}
            alt={selectedAnimal.name}
            className="animal-image-large"
          />
        </div>
        {loadingTraits ? (
          <p className="loading">Nalagam lastnosti...</p>
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
                  ),
                }).map((_, i) => (
                  <tr key={i}>
                    <td style={{ color: 'green' }}>
                      {traits.filter(t => t.positive)[i]?.description || ''}
                    </td>
                    <td style={{ color: 'red' }}>
                      {traits.filter(t => !t.positive)[i]?.description || ''}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div className="action-buttons">
              <button className="primary" onClick={handleSaveAssessment}>
                Shrani samoocenitev
              </button>
              <button className="secondary" onClick={handleReset}>
                Poskusi ponovno
              </button>
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
    </div>
  );
};