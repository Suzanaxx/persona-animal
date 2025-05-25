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
                const data: Animal[] = await res.json();
                const shuffled = data.sort(() => Math.random() - 0.5);
                setPool(shuffled.slice(2));
                setCurrentPair(shuffled.slice(0, 2));
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
            if (newPool.length < 2) {
                setError('Ni dovolj živali za nadaljevanje.');
                return;
            }
            const shuffled = newPool.sort(() => Math.random() - 0.5);
            setCurrentPair(shuffled.slice(0, 2));
            setPool(shuffled.slice(2));
        }
    };

    const handleReset = () => {
        setClickCount(0);
        setSelectedAnimal(null);
        setTraits([]);
        setError('');
        setLoading(true);
        (async () => {
            try {
                const res = await fetch(`${API_BASE_URL}/api/animals`);
                const data: Animal[] = await res.json();
                const shuffled = data.sort(() => Math.random() - 0.5);
                setPool(shuffled.slice(2));
                setCurrentPair(shuffled.slice(0, 2));
            } catch (err) {
                setError('Napaka pri nalaganju živali ' + err);
            } finally {
                setLoading(false);
            }
        })();
    };

    const handleSaveAssessment = async () => {
        if (!selectedAnimal) return;

        try {
            const res = await fetch(`${API_BASE_URL}/api/self-assessments`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify({
                    animalId: selectedAnimal.id,
                }),
            });

            if (!res.ok) throw new Error('Napaka pri shranjevanju samoocenitve.');

            alert('Samoocenitev uspešno shranjena!');
        } catch (err) {
            alert('Napaka: ' + err);
        }
    };

    if (loading) return <div>Nalagam živali...</div>;
    if (error) return <div className="error">{error}</div>;

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
                        <div style={{ marginTop: '1rem' }}>
                            <button onClick={handleSaveAssessment} style={{ marginRight: '1rem' }}>
                                Shrani samoocenitev
                            </button>
                            <button onClick={handleReset}>
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
                            style={{ width: '500px', height: '300px', objectFit: 'cover' }}
                        />
                        <span className="animal-name">{animal.name}</span>
                    </button>
                ))}
            </div>
            <div>Izbrano: {clickCount} / {MAX_KLIKOV}</div>
        </div>
    );
};
