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
    onSelectionComplete: (animalId: string) => void;
}

const API_BASE_URL = 'http://localhost:8080';
const MAX_KLIKOV = 5;

export const AnimalSelector = ({ onSelectionComplete }: AnimalSelectorProps) => {
    const [pool, setPool] = useState<Animal[]>([]);            // vse živali, ki še niso bile prikazane
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
                const data: Animal[] = await res.json();
                const shuffled = data.sort(() => Math.random() - 0.5);
                setPool(shuffled);
                setCurrentPair(shuffled.slice(0, 2));
                // odstranimo te dve iz poola
                setPool(shuffled.slice(2));
            } catch (err) {
                setError('Napaka pri nalaganju živali ' + err);
            } finally {
                setLoading(false);
            }
        })();
    }, []);

    const handleSelect = async (animal: Animal) => {
        if (loadingTraits) return;

        const newClickCount = clickCount + 1;
        setClickCount(newClickCount);

        // pri vsakem kliku odstranimo obe živali iz currentPair iz poola (če še obstajata)
        const newPool = pool.filter(a => !currentPair.some(c => c.id === a.id));
        setPool(newPool);

        if (newClickCount === MAX_KLIKOV) {
            setSelectedAnimal(animal);
            setLoadingTraits(true);
            try {
                const res = await fetch(`${API_BASE_URL}/api/animal_traits/traits/${animal.id}`);
                if (!res.ok) throw new Error('Napaka pri nalaganju lastnosti');
                const data: AnimalTraitDTO[] = await res.json();
                setTraits(data);
                onSelectionComplete(animal.id);
            } catch (err) {
                setError(String(err));
            } finally {
                setLoadingTraits(false);
            }
        } else {
            // izberemo naslednji par iz newPool
            if (newPool.length < 2) {
                setError('Ni dovolj živali za nadaljevanje.');
                return;
            }
            const shuffled = newPool.sort(() => Math.random() - 0.5);
            setCurrentPair(shuffled.slice(0, 2));
            // pool posodobimo brez teh dveh
            setPool(shuffled.slice(2));
        }
    };

    const handleReset = () => {
        // resetiramo vse
        setClickCount(0);
        setSelectedAnimal(null);
        setTraits([]);
        setError('');
        setLoading(true);
        // ponovno naložimo in zmešamo pool
        (async () => {
            try {
                const res = await fetch(`${API_BASE_URL}/api/animals`);
                const data: Animal[] = await res.json();
                const shuffled = data.sort(() => Math.random() - 0.5);
                setPool(shuffled);
                setCurrentPair(shuffled.slice(0, 2));
                setPool(shuffled.slice(2));
            } catch (err) {
                setError('Napaka pri nalaganju živali ' + err);
            } finally {
                setLoading(false);
            }
        })();
    };

    if (loading) return <div>Nalagam živali...</div>;
    if (error)   return <div className="error">{error}</div>;

    if (selectedAnimal) {
        return (
            <div className="animal-detail">
                <h2>{selectedAnimal.name}</h2>
                <img
                    src={`${API_BASE_URL}${selectedAnimal.imageUrl}`}
                    alt={selectedAnimal.name}
                    style={{ width: '300px' }}
                />
                {loadingTraits ? (
                    <p>Nalagam lastnosti...</p>
                ) : (
                    <>
                        <div className="traits-container">
                            <div className="traits-column">
                                <h3>Dobre lastnosti</h3>
                                <ul>
                                    {traits.filter(t => t.positive).map(t => (
                                        <li key={t.traitId}>{t.description}</li>
                                    ))}
                                </ul>
                            </div>
                            <div className="traits-column">
                                <h3>Slabe lastnosti</h3>
                                <ul>
                                    {traits.filter(t => !t.positive).map(t => (
                                        <li key={t.traitId}>{t.description}</li>
                                    ))}
                                </ul>
                            </div>
                        </div>
                        <button onClick={handleReset}>Poskusi ponovno</button>
                    </>
                )}
            </div>
        );
    }

    return (
        <div className="animal-selector">
            <h2>Izberi med:</h2>
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
                            style={{ width: '300px', height : '300px', objectFit: 'cover' }}
                        />
                        <span className="animal-name">{animal.name}</span>
                    </button>
                ))}
            </div>
            <div>Izbrano: {clickCount} / {MAX_KLIKOV}</div>
        </div>
    );
};
