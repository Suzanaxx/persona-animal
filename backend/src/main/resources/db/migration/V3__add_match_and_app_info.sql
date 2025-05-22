-- V3__refactor_match_results.sql

-- Ustvarimo tabelo za shranjevanje rezultatov ujemanj med uporabniki
CREATE TABLE match_results (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Uporabnik, ki je opravil ocenjevanje druge osebe
                               user_id UUID NOT NULL,

    -- Referenca na uporabnikovo zadnjo samoocenitev
                               self_session_id UUID NOT NULL,

    -- Referenca na sejo, ki ocenjuje drugo osebo
                               other_session_id UUID NOT NULL,

    -- Številčna ocena ujemanja (0–100)
                               match_score NUMERIC NOT NULL CHECK (match_score >= 0 AND match_score <= 100),

    -- Opis ujemanja, ki se lahko prikaže v aplikaciji
                               explanation TEXT,

                               created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),

    -- Unikaten par self + other session (da se enaka primerjava ne podvaja)
                               UNIQUE (self_session_id, other_session_id),

    -- FOREIGN KEYS
                               CONSTRAINT fk_match_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               CONSTRAINT fk_match_self FOREIGN KEY (self_session_id) REFERENCES rating_sessions(id) ON DELETE RESTRICT,
                               CONSTRAINT fk_match_other FOREIGN KEY (other_session_id) REFERENCES rating_sessions(id) ON DELETE RESTRICT
);

-- Indeks za hitrejše iskanje po uporabniku
CREATE INDEX idx_match_results_user_id ON match_results(user_id);



-- Tabela za shranjevanje informacij o aplikaciji (F9)
CREATE TABLE app_info (
                          id SERIAL PRIMARY KEY,

    -- Enolična oznaka (npr. 'main')
                          code TEXT UNIQUE NOT NULL,

    -- Verzija aplikacije
                          version TEXT NOT NULL,

    -- Kratek opis aplikacije
                          description TEXT NOT NULL,

    -- Povezava na avtorske pravice in portal
                          copyright_url TEXT,

    -- Seznam sodelujočih (JSON ali preprost seznam)
                          contributors TEXT NOT NULL,

    -- Čas zadnje posodobitve
                          updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);
