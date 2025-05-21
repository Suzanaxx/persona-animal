-- Dodamo foreign key za target_user_id
ALTER TABLE rating_sessions
    ADD CONSTRAINT fk_target_user
        FOREIGN KEY (target_user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Dodamo manjkajoče constraints v session_steps (če niso že tam)
ALTER TABLE session_steps
    ADD CONSTRAINT fk_session_id FOREIGN KEY (session_id) REFERENCES rating_sessions(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_animal1_id FOREIGN KEY (animal1_id) REFERENCES animals(id),
    ADD CONSTRAINT fk_animal2_id FOREIGN KEY (animal2_id) REFERENCES animals(id),
    ADD CONSTRAINT fk_chosen_animal_id FOREIGN KEY (chosen_animal_id) REFERENCES animals(id);