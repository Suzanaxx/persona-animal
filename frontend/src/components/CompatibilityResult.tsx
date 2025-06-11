import { useEffect, useState } from 'react';
import { getAuth } from 'firebase/auth';

interface SelfFullDTO {
  historyId: number;
  animalId: number;
  imageUrl: string;
  date: string;
}

interface CompatibilityResultDTO {
  compatibilityPercent: number;
  categoryName: string;
  categoryDescription: string;
}

interface CompatibilityResultProps {
  otherAnimalId: number; // animalId osebe, ki jo je uporabnik pravkar ocenil
}

export const CompatibilityResult = ({ otherAnimalId }: CompatibilityResultProps) => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [result, setResult] = useState<CompatibilityResultDTO | null>(null);
  const [selfImageUrl, setSelfImageUrl] = useState<string | null>(null);
  const [otherAnimalImageUrl, setOtherAnimalImageUrl] = useState<string | null>(null);

  useEffect(() => {
    const BACKEND_URL = 'https://backend-wqgy.onrender.com';

    const fixImageUrl = (url: string | undefined) => {
      if (!url) return null;
      if (url.startsWith('http')) return url; // če je že popoln URL, vrni takoj
      if (url.startsWith('/images/')) return `${BACKEND_URL}${url}`; // dodaj domeno pred /images
      // Če pa je samo ime datoteke, dodaj celotno pot
      return `${BACKEND_URL}/images/animals/${url}`;
    };

    const fetchCompatibility = async () => {
      try {
        const auth = getAuth();
        const token = await auth.currentUser?.getIdToken();
        if (!token) throw new Error('Uporabnik ni prijavljen.');

        const headers = {
          'Authorization': `Bearer ${token}`,
        };

        // 1. Samoocena
        const resSelf = await fetch(
          'https://backend-wqgy.onrender.com/api/history/self-assessments-full',
          {
            headers,
            credentials: 'include',
          }
        );
        if (!resSelf.ok) throw new Error('Napaka pri nalaganju samoocenitev');
        const selfList: SelfFullDTO[] = await resSelf.json();
        if (!selfList.length) throw new Error('Nimate še nobene samoocenitve.');

        const latest = selfList[selfList.length - 1];
        const selfAnimalId = latest.animalId;
        setSelfImageUrl(fixImageUrl(latest.imageUrl));

        // 2. Slika druge živali
        const resOther = await fetch(
          `https://backend-wqgy.onrender.com/api/animals/${otherAnimalId}`,
          {
            headers,
            credentials: 'include',
          }
        );
        if (resOther.ok) {
          const otherAnimal = await resOther.json();
          if (otherAnimal.imageUrl) setOtherAnimalImageUrl(fixImageUrl(otherAnimal.imageUrl));
        }

        // 3. Kompatibilnost
        const resComp = await fetch(
          `https://backend-wqgy.onrender.com/api/compatibility?animal1=${selfAnimalId}&animal2=${otherAnimalId}`,
          {
            headers,
            credentials: 'include',
          }
        );
        if (!resComp.ok) throw new Error('Napaka pri izračunu kompatibilnosti');

        const compData: CompatibilityResultDTO = await resComp.json();
        setResult(compData);
      } catch (err: any) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchCompatibility();
  }, [otherAnimalId]);

  if (loading)
    return (
      <div className="text-center text-white mt-10 text-lg animate-pulse">
        Pripravljam primerjavo ujemanja …
      </div>
    );

  if (error)
    return (
      <div className="text-red-400 bg-red-100 border border-red-300 px-4 py-3 rounded-md mt-6 text-center max-w-lg mx-auto">
        Napaka: {error}
      </div>
    );

  if (!result) return null;

  return (
  <div className="compatibility-container max-w-3xl mx-auto mt-10 bg-white rounded-xl shadow-lg p-6">
    <h2 className="text-3xl font-bold text-center text-[var(--primary)] mb-6">
      Ujemanje: {result.compatibilityPercent.toFixed(2)}%
    </h2>

    <div className="animal-pair justify-center gap-12 mb-6">
      {selfImageUrl && (
        <div className="compatibility-card">
          <img src={selfImageUrl} alt="Vaša žival" />
          <div className="animal-name">Vaša persona</div>
        </div>
      )}
      {otherAnimalImageUrl && (
        <div className="compatibility-card">
          <img src={otherAnimalImageUrl} alt="Druga žival" />
          <div className="animal-name">Presona druge osebe</div>
        </div>
      )}
    </div>

    <section className="text-center">
      <h3 className='text-title' >
        {result.categoryName}
      </h3>
      <p className="intro-text">
        {result.categoryDescription}
      </p>
    </section>
  </div>
);
};
