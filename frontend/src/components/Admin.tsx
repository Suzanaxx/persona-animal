import React, { useState } from 'react';

type Trait = {
  name: string;
  isPositive: boolean; // true = dober, false = slab
};

type Animal = {
  name: string;
  imageUrl: string;
  traits: Trait[];
};

type Category = {
  name: string;
  imageUrl: string;
  animals: Animal[];
};

const Admin = () => {
  const [categoryName, setCategoryName] = useState('');
  const [categoryImageUrl, setCategoryImageUrl] = useState('');

  const [categories, setCategories] = useState<Category[]>([]);

  // Za dodajanje osebka
  const [animalName, setAnimalName] = useState('');
  const [animalImageUrl, setAnimalImageUrl] = useState('');
  const [selectedCategoryIndex, setSelectedCategoryIndex] = useState<number | null>(null);

  // Za dodajanje traitov posameznemu osebku
  const [traitName, setTraitName] = useState('');
  const [traitIsPositive, setTraitIsPositive] = useState(true);
  const [selectedAnimalIndex, setSelectedAnimalIndex] = useState<number | null>(null);

  // Dodaj novo kategorijo
  const handleAddCategory = () => {
    if (!categoryName.trim()) return alert('Vnesi ime kategorije');
    setCategories([
      ...categories,
      { name: categoryName.trim(), imageUrl: categoryImageUrl.trim(), animals: [] },
    ]);
    setCategoryName('');
    setCategoryImageUrl('');
  };

  // Dodaj osebka v izbrano kategorijo
  const handleAddAnimal = () => {
    if (selectedCategoryIndex === null) return alert('Izberi kategorijo za dodajanje osebka');
    if (!animalName.trim()) return alert('Vnesi ime osebka');
    if (!animalImageUrl.trim()) return alert('Vnesi URL slike osebka');

    const newCategories = [...categories];
    newCategories[selectedCategoryIndex].animals.push({
      name: animalName.trim(),
      imageUrl: animalImageUrl.trim(),
      traits: [],
    });
    setCategories(newCategories);
    setAnimalName('');
    setAnimalImageUrl('');
  };

  // Dodaj trait v izbranega osebka
  const handleAddTrait = () => {
    if (selectedCategoryIndex === null || selectedAnimalIndex === null) return alert('Izberi kategorijo in osebka za dodajanje lastnosti');
    if (!traitName.trim()) return alert('Vnesi ime lastnosti');

    const newCategories = [...categories];
    const traits = newCategories[selectedCategoryIndex].animals[selectedAnimalIndex].traits;
    traits.push({ name: traitName.trim(), isPositive: traitIsPositive });
    setCategories(newCategories);
    setTraitName('');
    setTraitIsPositive(true);
  };

  // Shranjevanje (zaenkrat demo)
  const handleSave = () => {
    console.log('Shranjujem podatke:', categories);
    alert('Podatki shranjeni (demo).');

    // TODO: tukaj pokliči pravi API za shranjevanje

    // Lahko tudi počistiš, če želiš:
    // setCategories([]);
  };

  return (
    <div style={{ padding: '2rem', backgroundColor: '#e6ffee', borderRadius: '1rem', maxWidth: 800, margin: 'auto' }}>
      <h2 style={{ color: '#006633' }}>Admin nadzorna plošča</h2>

      <section style={{ marginBottom: '2rem' }}>
        <h3>Dodaj novo kategorijo</h3>
        <input
          value={categoryName}
          onChange={e => setCategoryName(e.target.value)}
          placeholder="Ime kategorije (npr. Rastline)"
        />
        <input
          value={categoryImageUrl}
          onChange={e => setCategoryImageUrl(e.target.value)}
          placeholder="URL slike kategorije"
          style={{ marginLeft: '1rem' }}
        />
        <button onClick={handleAddCategory} style={{ marginLeft: '1rem' }}>
          Dodaj kategorijo
        </button>
      </section>

      <section style={{ marginBottom: '2rem' }}>
        <h3>Dodaj osebka v kategorijo</h3>
        <select
          value={selectedCategoryIndex !== null ? selectedCategoryIndex : ''}
          onChange={e => setSelectedCategoryIndex(e.target.value === '' ? null : Number(e.target.value))}
        >
          <option value="">Izberi kategorijo</option>
          {categories.map((cat, i) => (
            <option key={i} value={i}>
              {cat.name}
            </option>
          ))}
        </select>
        <input
          value={animalName}
          onChange={e => setAnimalName(e.target.value)}
          placeholder="Ime osebka"
          style={{ marginLeft: '1rem' }}
        />
        <input
          value={animalImageUrl}
          onChange={e => setAnimalImageUrl(e.target.value)}
          placeholder="URL slike osebka"
          style={{ marginLeft: '1rem' }}
        />
        <button onClick={handleAddAnimal} style={{ marginLeft: '1rem' }}>
          Dodaj osebka
        </button>
      </section>

      <section style={{ marginBottom: '2rem' }}>
        <h3>Dodaj lastnost osebku</h3>
        <select
          value={selectedCategoryIndex !== null ? selectedCategoryIndex : ''}
          onChange={e => {
            setSelectedCategoryIndex(e.target.value === '' ? null : Number(e.target.value));
            setSelectedAnimalIndex(null);
          }}
        >
          <option value="">Izberi kategorijo</option>
          {categories.map((cat, i) => (
            <option key={i} value={i}>
              {cat.name}
            </option>
          ))}
        </select>

        <select
          value={selectedAnimalIndex !== null ? selectedAnimalIndex : ''}
          onChange={e => setSelectedAnimalIndex(e.target.value === '' ? null : Number(e.target.value))}
          disabled={selectedCategoryIndex === null}
          style={{ marginLeft: '1rem' }}
        >
          <option value="">Izberi osebka</option>
          {selectedCategoryIndex !== null &&
            categories[selectedCategoryIndex].animals.map((animal, i) => (
              <option key={i} value={i}>
                {animal.name}
              </option>
            ))}
        </select>

        <input
          value={traitName}
          onChange={e => setTraitName(e.target.value)}
          placeholder="Ime lastnosti"
          style={{ marginLeft: '1rem' }}
        />

        <label style={{ marginLeft: '1rem' }}>
          <input
            type="checkbox"
            checked={traitIsPositive}
            onChange={e => setTraitIsPositive(e.target.checked)}
          />{' '}
          Dobra lastnost
        </label>

        <button onClick={handleAddTrait} style={{ marginLeft: '1rem' }}>
          Dodaj lastnost
        </button>
      </section>

      <section>
        <h3>Trenutni podatki</h3>
        {categories.length === 0 && <p>Ni dodanih kategorij.</p>}
        <ul>
          {categories.map((cat, i) => (
            <li key={i}>
              <strong>{cat.name}</strong> <br />
              <img src={cat.imageUrl} alt={cat.name} style={{ maxWidth: 100, maxHeight: 100 }} />
              <ul>
                {cat.animals.map((animal, j) => (
                  <li key={j}>
                    <em>{animal.name}</em> <br />
                    <img src={animal.imageUrl} alt={animal.name} style={{ maxWidth: 80, maxHeight: 80 }} />
                    <ul>
                      {animal.traits.map((trait, k) => (
                        <li key={k} style={{ color: trait.isPositive ? 'green' : 'red' }}>
                          {trait.name} ({trait.isPositive ? 'dobra' : 'slaba'})
                        </li>
                      ))}
                    </ul>
                  </li>
                ))}
              </ul>
            </li>
          ))}
        </ul>
      </section>

      <button
        onClick={handleSave}
        style={{ marginTop: '2rem', backgroundColor: '#33cc66', padding: '0.5rem 1rem', borderRadius: '0.5rem', border: 'none', color: 'white' }}
      >
        Shrani vse podatke
      </button>
    </div>
  );
};

export default Admin;
