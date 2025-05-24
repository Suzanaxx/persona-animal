import { useEffect, useState } from 'react';

interface Animal {
    id: string;
    name: string;
    imageUrl: string;
}

interface AnimalSelectorProps {
    onSelectionComplete: (animalId: string) => void;
}

const API_BASE_URL = 'http://localhost:8080';

export const AnimalSelector = ({ onSelectionComplete }: AnimalSelectorProps) => {
    const [animals, setAnimals] = useState<Animal[]>([]);
    const [currentPair, setCurrentPair] = useState<Animal[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [clickCount, setClickCount] = useState(0);

    useEffect(() => {
        const fetchAnimals = async () => {
            try {
                const response = await fetch(`${API_BASE_URL}/api/animals`);
                const data = await response.json();
                const shuffled = data.sort(() => Math.random() - 0.5);
                setAnimals(shuffled);
                setCurrentPair(shuffled.slice(0, 2));
            } catch (err) {
                setError('Napaka pri nalaganju živali ' + err);
            } finally {
                setLoading(false);
            }
        };

        fetchAnimals();
    }, []);

    const handleSelect = (selectedAnimal: Animal) => {
        const newClickCount = clickCount + 1;
        setClickCount(newClickCount);

        // Odstrani izbrano žival in trenutno prikazane iz seznama
        const remaining = animals.filter(a => !currentPair.some(c => c.id === a.id));

        // Izberi novi par, če je dovolj živali
        if (newClickCount === 10) {
            // Po 10. kliku zaključimo in pokličemo onSelectionComplete
            onSelectionComplete(selectedAnimal.id);
        } else {
            // Izberi nov par iz preostalih (najprej dodamo izbrano žival nazaj med ostale)
            // Da ne zmanjka prehitro, ampak po želji lahko ne dodajamo nazaj.
            const updatedAnimals = [...remaining, selectedAnimal];
            const shuffled = updatedAnimals.sort(() => Math.random() - 0.5);

            setAnimals(shuffled);

            const newPair = shuffled.slice(0, 2);
            setCurrentPair(newPair);
        }
    };

    if (loading) return <div>Nalagam živali...</div>;
    if (error) return <div className="error">{error}</div>;

    return (
        <div className="animal-selector">
            <h2>Izberi med:</h2>
            <div className="animal-pair">
                {currentPair.map(animal => (
                    <button
                        key={animal.id}
                        onClick={() => handleSelect(animal)}
                        className="animal-card"
                    >
                        <img
                            src={`${API_BASE_URL}${animal.imageUrl}`}
                            alt={animal.name}
                            className="animal-image"
                        />
                        <span className="animal-name">{animal.name}</span>
                    </button>
                ))}
            </div>
            <div>Izbrano: {clickCount} / 10</div>
        </div>
    );
};
