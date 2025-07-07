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
    loan_type, max_loans, max_prolongations, penalty_days_per_late_day, user_profil
) VALUES
(1, true, true, 5, 'DOMICILE', 1, 1, 5, 'ETUDIANT'),
(2, true, true, 10, 'DOMICILE', 2, 2, 5, 'PROFESSEUR'),
(3, true, true, 12, 'DOMICILE', 3, 3, 5, 'PROFESSIONNEL');

INSERT INTO languages (name) VALUES
                                 ('Francais'),
                                 ('Anglais'),
                                 ('Malagasy');



UPDATE users
SET active = true
WHERE email IN (
  'sarah@gmail.com'
);

UPDATE loan_policies
SET max_loans = 2
WHERE id = 1;

UPDATE loan_policies
SET max_loans = 3
WHERE id = 2;

UPDATE loan_policies
SET max_loans = 4
WHERE id = 3;