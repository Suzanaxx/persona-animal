import { useEffect, useState } from 'react';
import { getAuth } from 'firebase/auth';

interface AssessmentHistoryItem {
  animalName: string;
  imageUrl: string;
  date: string;
  personName?: string; // Dodano za ocene drugih
}

const API_BASE_URL = 'https://backend-wqgy.onrender.com';

export const SelectorHistory = () => {
  const [activeTab, setActiveTab] = useState<'samoocenitev' | 'druge'>('samoocenitev');
  const [selfAssessments, setSelfAssessments] = useState<AssessmentHistoryItem[]>([]);
  const [otherAssessments, setOtherAssessments] = useState<AssessmentHistoryItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadHistory = async () => {
      setLoading(true);
      setError(null);
      const auth = getAuth();
      const token = await auth.currentUser?.getIdToken();
      if (!token) {
        setError('Prosimo, prijavite se za ogled zgodovine.');
        setLoading(false);
        return;
      }

      const endpoint =
        activeTab === 'samoocenitev'
          ? '/api/history/self-assessments'
          : '/api/history/other-assessments';

      try {
        const res = await fetch(`${API_BASE_URL}${endpoint}`, {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
          credentials: 'include',
        });
        if (!res.ok) throw new Error(`HTTP ${res.status}: ${await res.text()}`);
        const data: AssessmentHistoryItem[] = await res.json();
        if (activeTab === 'samoocenitev') {
          setSelfAssessments(data);
        } else {
          setOtherAssessments(data);
        }
      } catch (err) {
        setError('Napaka pri nalaganju zgodovine: ' + (err as Error).message);
      } finally {
        setLoading(false);
      }
    };

    loadHistory();
  }, [activeTab]);

  const renderAssessmentList = (items: AssessmentHistoryItem[]) => {
    if (loading) return <p className="loading">Nalagam...</p>;
    if (error) return <p className="error">{error}</p>;
    if (items.length === 0) return <p className="empty">Ni shranjenih podatkov.</p>;

    return (
      <ul className="history-list">
        {items.map((item, index) => (
          <li key={index} className="history-item">
            <img
              src={`${API_BASE_URL}${item.imageUrl}`}
              alt={item.animalName}
              className="history-image"
            />
            <div className="history-info">
              <div className="animal-name">{item.animalName}</div>
              {item.personName && <div>{`Oseba: ${item.personName}`}</div>}
              <div className="history-date">{new Date(item.date).toLocaleString('sl-SI')}</div>
            </div>
          </li>
        ))}
      </ul>
    );
  };

  return (
    <div className="selector-history">
      <h2 className="section-title">Zgodovina ocenitev</h2>

      <div className="history-buttons">
        <button
          className={`tab-button ${activeTab === 'samoocenitev' ? 'active' : ''}`}
          onClick={() => setActiveTab('samoocenitev')}
        >
          Samoocenitve
        </button>
        <button
          className={`tab-button ${activeTab === 'druge' ? 'active' : ''}`}
          onClick={() => setActiveTab('druge')}
        >
          Ocene drugih
        </button>
      </div>

      <div className="history-content">
        {activeTab === 'samoocenitev' && (
          <>
            <h3 className="subsection-title">Tvoje samoocenitve</h3>
            {renderAssessmentList(selfAssessments)}
          </>
        )}

        {activeTab === 'druge' && (
          <>
            <h3 className="subsection-title">Ocene drugih oseb</h3>
            {renderAssessmentList(otherAssessments)}
          </>
        )}
      </div>
    </div>
  );
};