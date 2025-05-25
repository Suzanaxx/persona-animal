import { useState } from 'react';

export const SelectorHistory = () => {
    const [activeTab, setActiveTab] = useState<'samoocenitev' | 'druge'>('samoocenitev');

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
                        <p>Tu bo prikazana zgodovina tvojih samoocenitev...</p>
                        {/* Kasneje sem tukaj naložiš podatke iz backend-a */}
                    </div>
                )}
                {activeTab === 'druge' && (
                    <div>
                        <h3>Zgodovina ocen drugih</h3>
                        <p>Tu bo prikazana zgodovina ocen drugih oseb...</p>
                        {/* Kasneje sem tukaj naložiš podatke iz backend-a */}
                    </div>
                )}
            </div>
        </div>
    );
};
