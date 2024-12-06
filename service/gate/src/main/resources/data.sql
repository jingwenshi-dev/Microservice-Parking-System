INSERT INTO LOT_OCCUPANCY (LOT_ID, TIMESTAMP, CURRENT_OCCUPANCY)
VALUES (1, '2024-12-01T08:00:00', 15),
       (1, '2024-12-01T09:00:00', 20),
       (1, '2024-12-01T10:00:00', 25),
       (2, '2024-12-01T08:00:00', 5),
       (2, '2024-12-01T09:00:00', 10),
       (3, '2024-12-01T08:00:00', 0),
       (3, '2024-12-01T09:00:00', 3),
       (4, '2024-12-01T08:00:00', 50),
       (4, '2024-12-01T09:00:00', 48),
       (4, '2024-12-01T10:00:00', 45);

INSERT INTO PARKING_LOTS (LOT_ID, LOT_NAME, TOTAL_SPOTS, VISITOR_ALLOWED, HOURLY_RATE, LOCATION)
VALUES (1, 'Lot A', 100, true, 2.50, '123 Main St, City A'),
       (2, 'Lot B', 200, false, 3.00, '456 Elm St, City B'),
       (3, 'Lot C', 150, true, 1.75, '789 Maple Ave, City C'),
       (4, 'Lot D', 300, true, 2.00, '321 Oak Rd, City D'),
       (5, 'Lot E', 250, false, 4.00, '654 Pine Ln, City E'),
       (6, 'Lot F', 180, true, 1.50, '987 Cedar Blvd, City F');