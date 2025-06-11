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
    const fixImageUrl = (url: string | undefined) => {
      if (!url) return null;
      if (url.startsWith('/images/')) return url;
      return `/images/animals/${url}`;
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
    <div className="compatibility-result bg-gray-800 text-white rounded-2xl shadow-xl p-8 max-w-2xl mx-auto mt-10">
      <h2 className="text-3xl font-bold text-pink-400 mb-2 text-center">
        Ujemanje: {result.compatibilityPercent.toFixed(2)}%
      </h2>

      {/* Sliki obeh živali */}
      <div className="flex justify-center items-center gap-8 mt-6">
        {selfImageUrl && (
          <div className="flex flex-col items-center">
            <img
              src={selfImageUrl}
              alt="Vaša žival"
              className="w-24 h-24 rounded-full object-cover border-2 border-[#3b7d5c]"
            />
            <p className="mt-2 text-sm text-gray-400">Vaša žival</p>
          </div>
        )}
        {otherAnimalImageUrl && (
          <div className="flex flex-col items-center">
            <img
              src={otherAnimalImageUrl}
              alt="Druga žival"
              className="w-24 h-24 rounded-full object-cover border-2 border-[#3b7d5c]"
            />
            <p className="mt-2 text-sm text-gray-400">Druga žival</p>
          </div>
        )}
      </div>

      <h3 className="text-xl font-semibold text-white text-center mt-6 mb-2">
        {result.categoryName}
      </h3>
      <p className="text-gray-300 leading-relaxed text-center">{result.categoryDescription}</p>
    </div>
  );
};
