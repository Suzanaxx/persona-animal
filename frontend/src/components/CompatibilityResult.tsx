import { useEffect, useState } from 'react';

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
  // animalId osebe, ki jo je uporabnik pravkar ocenil
  otherAnimalId: number;
}

export const CompatibilityResult = ({ otherAnimalId }: CompatibilityResultProps) => {
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [result, setResult] = useState<CompatibilityResultDTO | null>(null);

  useEffect(() => {
    const fetchCompatibility = async () => {
      try {
        // 1) Pridobiš seznam vseh samoocenitev (z animalId, imageUrl, date)
        const resSelf = await fetch(
          'http://localhost:8080/api/history/self-assessments-full',
          { credentials: 'include' }
        );
        if (!resSelf.ok) {
          throw new Error(`Napaka ${resSelf.status} pri nalaganju samoocenitev`);
        }
        const selfList: SelfFullDTO[] = await resSelf.json();

        if (!selfList.length) {
          throw new Error('Nimate še nobene samoocenitve. Najprej opravite samooceno.');
        }

        // vzemi zadnjo samoocenitev
        const latest = selfList[selfList.length - 1];
        const selfAnimalId = latest.animalId;

        // 2) Pokliči compatibility endpoint
        const resComp = await fetch(
          `http://localhost:8080/api/compatibility?animal1=${selfAnimalId}&animal2=${otherAnimalId}`,
          { credentials: 'include' }
        );
        if (!resComp.ok) {
          throw new Error(`Napaka ${resComp.status} pri izračunu kompatibilnosti`);
        }
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

  if (loading) return <p>Pripravljam primerjavo ujemanja …</p>;
  if (error) return <p className="error">Napaka: {error}</p>;
  if (!result) return null;

  return (
    <div className="compatibility-result">
      <h2>Ujemanje: {result.compatibilityPercent.toFixed(2)} %</h2>
      <h3>{result.categoryName}</h3>
      <p>{result.categoryDescription}</p>
    </div>
  );
};