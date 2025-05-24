-- KONJ (animal_id = 4)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 4, id FROM traits WHERE description IN (
                                               'Plemenitost',
                                               'Hitrost',
                                               'Moč',
                                               'Lepota',
                                               'Koristnost',
                                               'Razdražljivost',
                                               'Divjost',
                                               'Občutljivost'
    );

-- PES (animal_id = 12)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 12, id FROM traits WHERE description IN (
                                                'Zvestoba',
                                                'Zaščitništvo',
                                                'Učljivost',
                                                'Pripadnost',
                                                'Ljubosumnost',
                                                'Vodljivost (nekritična poslušnost)',
                                                'Popadljivost'
    );

-- SOVA (animal_id = 14)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 14, id FROM traits WHERE description IN (
                                                'Dar za opazovanje',
                                                'Resnost',
                                                'Modrost',
                                                'Previdnost',
                                                'Čuječnost',
                                                'Ponočevanje',
                                                'Neodkritost',
                                                'Zaspanost',
                                                'Lenobnost'
    );

-- MEDVED (animal_id = 8)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 8, id FROM traits WHERE description IN (
                                               'Moč',
                                               'Zaščitništvo',
                                               'Razigranost',
                                               'Dobrodušnost',
                                               'Grobost',
                                               'Godrnjavost',
                                               'Okrutnost'
    );

-- LEV (animal_id = 5)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 5, id FROM traits WHERE description IN (
                                               'Ponos',
                                               'Vsemogočnost',
                                               'Samozavest',
                                               'Veličastvenost',
                                               'Krvoločnost',
                                               'Vzvišenost',
                                               'Požrešnost'
    );

-- LISICA (animal_id = 6)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 6, id FROM traits WHERE description IN (
                                               'Zvitost',
                                               'Bistrost',
                                               'Prilagodljivost',
                                               'Preračunljivost',
                                               'Kradljivost',
                                               'Zahrbtnost'
    );

-- OPICA (animal_id = 10)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 10, id FROM traits WHERE description IN (
                                                'Nagajivost',
                                                'Veselje',
                                                'Zabavnost',
                                                'Inteligentnost',
                                                'Kradljivost',
                                                'Prepirljivost',
                                                'Požrešnost'
    );

-- ZAJEC (animal_id = 16)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 16, id FROM traits WHERE description IN (
                                                'Hitrost',
                                                'Ljubkost',
                                                'Previdnost',
                                                'Dobrosrčnost',
                                                'Plašnost',
                                                'Strahopetnost',
                                                'Prestrašenost'
    );

-- MAČKA (animal_id = 7)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 7, id FROM traits WHERE description IN (
                                               'Igrivost',
                                               'Svobodnost',
                                               'Okretnost',
                                               'Urejenost',
                                               'Ljubkost',
                                               'Zahrbtnost',
                                               'Popadljivost',
                                               'Kradljivost'
    );

-- GALEB (animal_id = 3)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 3, id FROM traits WHERE description IN (
                                               'Elegantnost',
                                               'Gibčnost',
                                               'Lepota',
                                               'Kradljivost',
                                               'Požrešenost',
                                               'Prepirljivost'
    );

-- MRAVLJA (animal_id = 9)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 9, id FROM traits WHERE description IN (
                                               'Pridnost',
                                               'Organiziranost',
                                               'Altruizem',
                                               'Sodelovalnost',
                                               'Bojevitost',
                                               'Trmoglavost',
                                               'Napadalnost'
    );

-- OREL (animal_id = 11)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 11, id FROM traits WHERE description IN (
                                                'Plemenitost',
                                                'Vzvišenost',
                                                'Ponos',
                                                'Pogum',
                                                'Neusmiljenost',
                                                'Krvoločnost',
                                                'Plenjenje',
                                                'Krutost'
    );

-- SLON (animal_id = 13)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 13, id FROM traits WHERE description IN (
                                                'Moč',
                                                'Delavnost',
                                                'Bistrost',
                                                'Nerodnost',
                                                'Svojeglavost',
                                                'Požrešnost',
                                                'Razdiralnost'
    );

-- SRNA (animal_id = 15)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 15, id FROM traits WHERE description IN (
                                                'Nedolžnost',
                                                'Prisrčnost',
                                                'Spretnost',
                                                'Ljubkost',
                                                'Gracioznost',
                                                'Plahost',
                                                'Nezaupljivost',
                                                'Občutljivost'
    );

-- ČEBELA (animal_id = 2)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 2, id FROM traits WHERE description IN (
                                               'Delavnost',
                                               'Koristnost',
                                               'Aktivnost',
                                               'Sodelovalnost',
                                               'Napadalnost',
                                               'Nadležnost'
    );

-- BIK (animal_id = 1)
INSERT INTO animal_traits (animal_id, trait_id)
SELECT 1, id FROM traits WHERE description IN (
                                               'Moč',
                                               'Borbenost',
                                               'Vztrajnost',
                                               'Energičnost',
                                               'Divjost',
                                               'Razdražljivost',
                                               'Trmavost',
                                               'Zaletavost',
                                               'Impulzivnost'
    );
