-- Insert two sample users
INSERT INTO Users (first_name, last_name, email, user_type, total_outstanding_amount)
VALUES
    ('John', 'Doe', 'john.doe@example.com', 'STUDENT', 0.0),
    ('Jane', 'Smith', 'jane.smith@example.com', 'STAFF', 0.0);

-- Sample insert into Permit (user_id references the Users table)
INSERT INTO Permit (permit_id, transponder_number, valid_from, valid_until, user_id, lot_id)
VALUES
    (1, 'TN123456', '2024-01-01', '2024-12-31', 1, 1),  -- user_id references the first user
    (2, 'TN654321', '2024-01-01', '2024-12-31', 2, 2);  -- user_id references the second user