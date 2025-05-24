// src/components/AnimalSelector.tsx
import { useEffect, useState } from 'react';

interface Animal {
    id: string;
    name: string;
    imageUrl: string;
}
//
interface AnimalSelectorProps {
    onSelectionComplete: (animalId: string) => void;
}

export const AnimalSelector = ({ onSelectionComplete }: AnimalSelectorProps) => {
    const [animals, setAnimals] = useState<Animal[]>([]);
    const [currentPair, setCurrentPair] = useState<Animal[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchAnimals = async () => {
            try {
                const response = await fetch('/api/animals');
                const data = await response.json();
                const shuffled = data.sort(() => Math.random() - 0.5);
                setAnimals(shuffled);
                setCurrentPair(shuffled.slice(0, 2));
            } catch (err) {
                setError('Napaka pri nalaganju živali');
            } finally {
                setLoading(false);
            }
        };

        fetchAnimals();
    }, []);

    const handleSelect = async (selectedAnimal: Animal) => {
        try {
            // Shrani izbiro v bazo
            await fetch('/api/selections', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ animalId: selectedAnimal.id }),
            });

            // Generiraj nov par
            const remaining = animals.filter(a => a.id !== selectedAnimal.id);
            setAnimals(remaining);
            setCurrentPair([selectedAnimal, remaining[0]]);

            if (remaining.length === 0) {
                onSelectionComplete(selectedAnimal.id);
            }
        } catch (err) {
            setError('Napaka pri shranjevanju izbire');
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
                            src={animal.imageUrl}
                            alt={animal.name}
                            className="animal-image"
                        />
                        <span className="animal-name">{animal.name}</span>
                    </button>
                ))}
            </div>
        </div>
    );
};