-- omogoči gen_random_uuid()
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. users
CREATE TABLE users (
                       id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       firebase_uid TEXT    NOT NULL UNIQUE,
                       username     TEXT    NOT NULL UNIQUE,
                       created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

-- 2. animals
CREATE TABLE animals (
                         id        SERIAL PRIMARY KEY,
                         name      TEXT   NOT NULL,
                         image_url TEXT   NOT NULL
);

-- 3. traits
CREATE TABLE traits (
                        id            SERIAL PRIMARY KEY,
                        description   TEXT NOT NULL,
                        is_positive   BOOLEAN NOT NULL
);

-- 4. animal_traits
CREATE TABLE animal_traits (
                               animal_id INT NOT NULL REFERENCES animals(id) ON DELETE CASCADE,
                               trait_id  INT NOT NULL REFERENCES traits(id)   ON DELETE CASCADE,
                               PRIMARY KEY (animal_id, trait_id)
);

-- 5. definicija enum tipa, če še ne obstaja
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'session_type') THEN
CREATE TYPE session_type AS ENUM ('self', 'other');
END IF;
END$$;

-- 6. rating_sessions
CREATE TABLE rating_sessions (
                                 id             UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
                                 user_id        UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                 target_user_id UUID         REFERENCES users(id),
                                 type           session_type NOT NULL,
                                 created_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

-- 7. session_steps
CREATE TABLE session_steps (
                               id               BIGSERIAL PRIMARY KEY,
                               session_id       UUID      NOT NULL REFERENCES rating_sessions(id) ON DELETE CASCADE,
                               step_number      INT       NOT NULL,
                               animal1_id       INT       NOT NULL REFERENCES animals(id),
                               animal2_id       INT       NOT NULL REFERENCES animals(id),
                               chosen_animal_id INT       NOT NULL REFERENCES animals(id),
                               CHECK (chosen_animal_id IN (animal1_id, animal2_id)),
                               UNIQUE (session_id, step_number)
);
