import { useEffect, useState } from 'react';

interface AssessmentHistoryItem {
  animalName: string;
  imageUrl: string;
  date: string;
}

const API_BASE_URL = 'http://localhost:8080';

export const SelectorHistory = () => {
  const [activeTab, setActiveTab] = useState<'samoocenitev' | 'druge'>('samoocenitev');
  const [selfAssessments, setSelfAssessments] = useState<AssessmentHistoryItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (activeTab === 'samoocenitev') {
      setLoading(true);
      setError('');

      fetch(`${API_BASE_URL}/api/history/self-assessments`, {
        credentials: "include",
      })
        .then(res => {
          if (!res.ok) throw new Error(`HTTP ${res.status}: napaka pri nalaganju zgodovine.`);
          return res.json();
        })
        .then((data: AssessmentHistoryItem[]) => {
          setSelfAssessments(data);
        })
        .catch(err => {
          setError(String(err));
        })
        .finally(() => {
          setLoading(false);
        });
    }
  }, [activeTab]);

  return (
    <div className="selector-history">
      <h2>Zgodovina ocenitev</h2>
      <div className="history-buttons">
        <button
          className={activeTab === 'samoocenitev' ? 'active' : ''}
          onClick={() => setActiveTab('samoocenitev')}
        >
          Samoocenitve
        </button>
        <button
          className={activeTab === 'druge' ? 'active' : ''}
          onClick={() => setActiveTab('druge')}
        >
          Ocene drugih
        </button>
      </div>

      <div className="history-content">
        {activeTab === 'samoocenitev' && (
          <div>
            <h3>Zgodovina samoocenitev</h3>
            {loading && <p>Nalagam...</p>}
            {error && <p className="error">{error}</p>}
            {!loading && selfAssessments.length === 0 && <p>Ni shranjenih samoocenitev.</p>}
            <ul className="history-list">
              {selfAssessments.map((item, index) => (
                <li key={index} className="history-item">
                  <img
                    src={`${API_BASE_URL}${item.imageUrl}`}
                    alt={item.animalName}
                    style={{ width: '100px', height: '80px', objectFit: 'cover' }}
                  />
                  <div>
                    <strong>{item.animalName}</strong>
                    <br />
                    <small>{new Date(item.date).toLocaleString()}</small>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        )}

        {activeTab === 'druge' && (
          <div>
            <h3>Zgodovina ocen drugih</h3>
            <p>Tu bo prikazana zgodovina ocen drugih oseb...</p>
            {/* Kasneje lahko naložiš podatke iz endpointa /api/history/other-assessments */}
          </div>
        )}
      </div>
    </div>
  );
};