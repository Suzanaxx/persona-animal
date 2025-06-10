import { useEffect, useState } from 'react';
import { CompatibilityResult } from './CompatibilityResult';

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

type Category = 'živali' | 'rastline' | 'vozila';

const API_BASE_URL = 'https://backend-wqgy.onrender.com';
const MAX_KLIKOV = 5;

export const Compatibility = () => {
  const [personName, setPersonName] = useState('');
  const [category, setCategory] = useState<Category | ''>('');
  const [step, setStep] = useState<'vnos' | 'ocenjevanje' | 'rezultat' | 'prikazi-ujemanje'>('vnos');

  const [pool, setPool] = useState<Animal[]>([]);
  const [currentPair, setCurrentPair] = useState<Animal[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [clickCount, setClickCount] = useState(0);
  const [selectedAnimal, setSelectedAnimal] = useState<Animal | null>(null);
  const [traits, setTraits] = useState<AnimalTraitDTO[]>([]);
  const [loadingTraits, setLoadingTraits] = useState(false);
  const [otherAnimalId, setOtherAnimalId] = useState<number | null>(null);

  useEffect(() => {
    if (step === 'ocenjevanje') {
      loadPool();
    }
  }, [step]);

  const loadPool = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await fetch(`${API_BASE_URL}/api/animals`);
      const data: Animal[] = await res.json();
      const shuffled = data.sort(() => Math.random() - 0.5);
      setCurrentPair(shuffled.slice(0, 2));
      setPool(shuffled.slice(2));
    } catch (err) {
      setError('Napaka pri nalaganju: ' + err);
    } finally {
      setLoading(false);
    }
  };

  const handleSelect = async (animal: Animal) => {
    if (loadingTraits) return;

    const newCount = clickCount + 1;
    setClickCount(newCount);

    const newPool = pool.filter(
      (a) => a.id !== animal.id && !currentPair.some((c) => c.id === a.id)
    );
    setPool(newPool);

    if (newCount === MAX_KLIKOV) {
      setSelectedAnimal(animal);
      setLoadingTraits(true);
      try {
        const res = await fetch(`${API_BASE_URL}/api/animal_traits/traits/${animal.id}`);
        const data: AnimalTraitDTO[] = await res.json();
        setTraits(data);
        setStep('rezultat');
      } catch (err) {
        setError('Napaka pri nalaganju lastnosti.');
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
    setStep('vnos');
    setPersonName('');
    setCategory('');
    setOtherAnimalId(null);
  };

  const handleSaveAssessment = async () => {
    if (!selectedAnimal || !personName.trim()) return;

    try {
      const response = await fetch(`${API_BASE_URL}/api/history/other-assessment`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({
          animalId: Number(selectedAnimal.id),
          personName: personName.trim(),
        }),
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }

      setOtherAnimalId(Number(selectedAnimal.id));
      alert('Ocenitev uspešno shranjena!');
    } catch (err) {
      alert('Napaka pri shranjevanju ocene druge osebe: ' + err);
    }
  };

  if (step === 'vnos') {
    return (
      <div className="input-screen">
        <div className="input-box">
          <h2>Vnesi ime osebe</h2>
          <input
            type="text"
            placeholder="Ime osebe"
            value={personName}
            onChange={(e) => setPersonName(e.target.value)}
          />
        </div>

        <div className="input-box">
          <h2>Izberi kategorijo</h2>
          <br />
          <div className="category-select">
            {[
              { key: 'živali', img: 'zivali.jpg' },
              { key: 'rastline', img: 'rastline.jpg' },
              { key: 'vozila', img: 'vozila.jpg' },
            ].map((k) => (
              <div
                key={k.key}
                className={`category-card ${category === k.key ? 'selected' : ''}`}
                onClick={() => setCategory(k.key as Category)}
              >
                <img src={`/${k.img}`} alt={k.key} />
                <span>{k.key.charAt(0).toUpperCase() + k.key.slice(1)}</span>
              </div>
            ))}
          </div>
        </div>

        <button
          className="primary"
          onClick={() =>
            personName && category && setStep('ocenjevanje')
          }
          disabled={!personName || !category}
        >
          Začni ocenjevanje
        </button>
      </div>
    );
  }

  if (step === 'rezultat' && selectedAnimal) {
    return (
      
      <div className="animal-detail">
         <h2>Izbrana osebnost za osebo {personName}: <span style={{ color: 'var(--primary)' }}>{selectedAnimal.name}</span></h2>

        <img
          src={`${API_BASE_URL}${selectedAnimal.imageUrl}`}
          className="animal-image-large"
          alt={selectedAnimal.name}
        />
        <p className="intro-text">
          Na podlagi izbranih slik smo ocenili, da <span className="highlighted">{personName}</span> najbolj ustreza osebnosti <span className="highlighted">{selectedAnimal.name}</span>.
          Vsaka izbira pove nekaj o načinu razmišljanja, čustvenih odzivih in socialnem vedenju.
        
       
          Spodaj si lahko ogledaš pozitivne in negativne lastnosti, ki smo jih zaznali pri tej osebnosti.  
          Če želiš preveriti, kako kompatibilna je ta oseba s tabo, klikni na gumb za primerjavo!
        </p>


        {loadingTraits ? (
          <div className="spinner"></div>
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
                    traits.filter((t) => t.positive).length,
                    traits.filter((t) => !t.positive).length
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
                Shrani ocenitev
              </button>

              <button className="secondary" onClick={handleReset}>
                Ocenjuj novo osebo
              </button>

              <button
                className="secondary"
                onClick={() => setStep('prikazi-ujemanje')}
                disabled={otherAnimalId === null}
              >
                Primerjaj ujemanje
              </button>
            </div>
          </>
        )}
      </div>
    );
  }

  if (step === 'prikazi-ujemanje' && otherAnimalId !== null) {
    return (
      <div style={{ padding: '1rem' }}>
        <CompatibilityResult otherAnimalId={otherAnimalId} />
        <button onClick={handleReset} style={{ marginTop: '1rem' }}>
          Poskusi znova
        </button>
      </div>
    );
  }

  return (
    <div className="animal-selector">
      <h2 className="section-title">
        Ocenjuješ osebo: <strong>{personName}</strong>
      </h2>
      <p style={{ textAlign: 'center' }}>
        Klikni na sliko, ki najbolje upodobi osebo, ki jo ocenjuješ
      </p>
      <div className="progress-container">
        <div
          className="progress-fill"
          style={{ width: `${(clickCount / MAX_KLIKOV) * 100}%` }}
        />
      </div>
      <div className="animal-pair">
        {currentPair.map((animal) => (
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