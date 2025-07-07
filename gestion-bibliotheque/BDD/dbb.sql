CREATE TABLE roles (
    id serial PRIMARY KEY,
    name varchar(50) NOT NULL UNIQUE
);

CREATE TABLE users (
    id bigserial PRIMARY KEY,
    active boolean NOT NULL,
    email varchar(255) NOT NULL UNIQUE,
    name varchar(255),
    password varchar(255),
    profile varchar(255) NOT NULL CHECK (profile IN ('ETUDIANT', 'PROFESSEUR', 'PROFESSIONNEL', 'ANONYME', 'BIBLIOTHECAIRE')),
    role_id integer NOT NULL,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE activity_logs (
    id bigserial PRIMARY KEY,
    action_type varchar(255),
    description varchar(255),
    timestamp timestamp(6),
    user_id bigint,
    CONSTRAINT fk_user_activity FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE book_categories (
    id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE
);

CREATE TABLE languages (
    id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE
);

CREATE TABLE books (
    id bigserial PRIMARY KEY,
    author varchar(255),
    isbn varchar(255),
    title varchar(255),
    category_id bigint,
    language_id bigint,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES book_categories(id),
    CONSTRAINT fk_language FOREIGN KEY (language_id) REFERENCES languages(id)
);

CREATE TABLE book_copies (
    id bigserial PRIMARY KEY,
    code varchar(255),
    status varchar(255) CHECK (status IN ('DISPONIBLE', 'EMPRUNTE', 'RESERVE')),
    book_id bigint,
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books(id)
);

CREATE TABLE holidays (
    id bigserial PRIMARY KEY,
    date date,
    name varchar(255)
);

CREATE TABLE loan_policies (
    id bigserial PRIMARY KEY,
    allow_prolongation boolean NOT NULL,
    allow_reservation boolean NOT NULL,
    loan_duration_days integer NOT NULL,
    loan_type varchar(255) NOT NULL CHECK (loan_type IN ('DOMICILE', 'SUR_PLACE')),
    max_loans integer NOT NULL,
    max_prolongations integer NOT NULL,
    penalty_days_per_late_day integer NOT NULL,
    user_profil varchar(255) NOT NULL CHECK (user_profil IN ('ETUDIANT', 'PROFESSEUR', 'PROFESSIONNEL', 'ANONYME', 'BIBLIOTHECAIRE'))
);

CREATE TABLE loans (
    id bigserial PRIMARY KEY,
    due_date date NOT NULL,
    extended boolean,
    loan_type varchar(255) NOT NULL CHECK (loan_type IN ('DOMICILE', 'SUR_PLACE')),
    return_date date,
    returned boolean,
    start_date date NOT NULL,
    book_copy_id bigint NOT NULL,
    user_id bigint,
    CONSTRAINT fk_book_copy FOREIGN KEY (book_copy_id) REFERENCES book_copies(id),
    CONSTRAINT fk_user_loan FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE penalties (
    id bigserial PRIMARY KEY,
    active boolean NOT NULL,
    days integer NOT NULL,
    end_date date,
    reason varchar(255),
    start_date date,
    user_id bigint,
    CONSTRAINT fk_user_penalty FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE reservations (
    id bigserial PRIMARY KEY,
    active boolean NOT NULL,
    available_from date,
    notified boolean NOT NULL,
    reservation_date date NOT NULL,
    status varchar(255) NOT NULL CHECK (status IN ('EN_ATTENTE', 'ACCEPTEE', 'ANNULEE', 'EXPIREE')),
    book_id bigint,
    user_id bigint,
    CONSTRAINT fk_user_reservation FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_book_reservation FOREIGN KEY (book_id) REFERENCES books(id)
);
