INSERT INTO roles (name) VALUES
                             ('ADMIN'),
                             ('UTILISATEUR');

INSERT INTO book_categories (name) VALUES
  ('Roman'),
  ('Informatique'),
  ('Sciences'),
  ('Histoire'),
  ('Biographie'),
  ('Philosophie'),
  ('Art'),
  ('Economie'),
  ('Psychologie'),  
  ('Littérature classique'),
  ('Jeunesse / Fantastique'),
  ('Langues');

INSERT INTO loan_policies (
    id, allow_prolongation, allow_reservation, loan_duration_days,
    loan_type, max_loans, max_prolongations, penalty_days_per_late_day, user_profil, max_reservation
) VALUES
(1, true, true, 5, 'DOMICILE', 2, 1, 5, 'ETUDIANT', 1),
(2, true, true, 10, 'DOMICILE', 3, 2, 5, 'PROFESSEUR', 2),
(3, true, true, 12, 'DOMICILE', 4, 3, 5, 'PROFESSIONNEL', 3);

INSERT INTO languages (name) VALUES
                                 ('Francais'),
                                 ('Anglais'),
                                 ('Malagasy');

INSERT INTO holidays (date, name) VALUES
('2025-07-17', 'Jour férié 17 juillet'),
('2025-07-19', 'Jour férié 19 juillet'),
('2025-08-15', 'Assomption'),
('2025-11-01', 'Toussaint'),
('2025-12-25', 'Noël');
