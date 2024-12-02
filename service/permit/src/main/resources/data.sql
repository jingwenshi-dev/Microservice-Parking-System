-- Insert two sample users
INSERT INTO Users (first_name, last_name, email, user_type, total_outstanding_amount)
VALUES
    ('John', 'Doe', 'john.doe@example.com', 'STUDENT', 0.0),
    ('Jane', 'Smith', 'jane.smith@example.com', 'STAFF', 0.0);

-- Sample insert into Permit (user_id references the Users table)
INSERT INTO Permit (permit_id, transponder_number, valid_from, valid_until, user_id, lot_id)
VALUES
    (1, '97ae1bb0-b42a-41db-8cfc-fd2ba3bbf008', '2024-01-01T00:00:00', '2024-12-31T00:00:00', 1, 1),  -- user_id references the first user
    (2, '59116fd9-fb84-4abe-82f8-804bda22e6e0', '2024-01-01T00:00:00', '2024-12-31T00:00:00', 2, 2);  -- user_id references the second user