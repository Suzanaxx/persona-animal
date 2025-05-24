import { useState } from 'react';
import './App.css';
import {AnimalSelector} from "./components/AnimalSelector.tsx";
import {Compatibility} from "./components/Compatibility.tsx";
import {SelectorHistory} from "./components/SelectorHistory.tsx";

export const App = () => {
    const [activePage, setActivePage] = useState<'samoocenitev' | 'oceni' | 'zgodovina'>('samoocenitev');

    return (
        <div className="app">
            <div className="layout">
                <nav className="menu">
                    <ul>
                        <li
                            className={activePage === 'samoocenitev' ? 'active' : ''}
                            onClick={() => setActivePage('samoocenitev')}
                        >
                            Samoocenitev
                        </li>
                        <li
                            className={activePage === 'oceni' ? 'active' : ''}
                            onClick={() => setActivePage('oceni')}
                        >
                            Oceni drugega
                        </li>
                        <li
                            className={activePage === 'zgodovina' ? 'active' : ''}
                            onClick={() => setActivePage('zgodovina')}
                        >
                            Zgodovina
                        </li>
                    </ul>
                </nav>

                <main className="content">
                    {activePage === 'samoocenitev' && <AnimalSelector onSelectionComplete={() => {}} />}
                    {activePage === 'oceni' && <Compatibility />}
                    {activePage === 'zgodovina' && <SelectorHistory />}
                </main>
            </div>
        </div>
    );
};

export default App;
