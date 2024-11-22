-- Users Table
INSERT INTO Users (user_id, first_name, last_name, email, user_type, campus_id, total_outstanding_amount)
VALUES (1, 'John', 'Doe', 'john.doe@example.com', 'student', 'S12345', 0.0),
       (2, 'Jane', 'Smith', 'jane.smith@example.com', 'faculty', 'F67890', 15.0),
       (3, 'Alice', 'Johnson', 'alice.johnson@example.com', 'staff', 'ST54321', 0.0);

-- Vehicles Table
INSERT INTO Vehicles (license_plate, user_type, user_id)
VALUES ('ABC123', 'permit', 1),
       ('XYZ789', 'visitor', NULL),
       ('DEF456', 'permit', 2);

-- ParkingPermits Table
INSERT INTO ParkingPermits (permit_id, transponder_number, valid_from, valid_until, user_id, payment_method)
VALUES (1, 'TRN001', '2024-01-01', '2024-12-31', 1, 'direct'),
       (2, 'TRN002', '2024-01-01', '2024-06-30', 2, 'payroll');

-- ParkingLots Table
INSERT INTO ParkingLots (lot_id, lot_name, total_spots, visitor_allowed, hourly_rate, location)
VALUES (1, 'Lot A', 100, TRUE, 5.0, 'North Campus'),
       (2, 'Lot B', 50, FALSE, 3.0, 'East Campus'),
       (3, 'Lot C', 75, TRUE, 4.0, 'West Campus');

-- VisitorPasses Table
INSERT INTO VisitorPasses (pass_id, qr_code, issued_at, valid_until, issued_by, is_used)
VALUES (1, 'QR001', '2024-03-01 10:00:00', '2024-03-01 18:00:00', 3, FALSE),
       (2, 'QR002', '2024-03-02 09:00:00', '2024-03-02 17:00:00', 3, TRUE);

-- VisitorParking Table
INSERT INTO VisitorParking (transaction_id, license_plate, entry_time, exit_time, qr_code, amount, payment_type, lot_id,
                            pass_id)
VALUES (1, 'XYZ789', '2024-03-01 10:30:00', '2024-03-01 12:30:00', 'QR001', 10.0, 'voucher', 1, 1),
       (2, 'DEF456', '2024-03-02 09:15:00', '2024-03-02 11:45:00', NULL, 15.0, 'credit', 2, NULL);

-- ParkingViolations Table
INSERT INTO ParkingViolations (violation_id, violation_time, license_plate, fine_amount, is_paid, officer_id, lot_id)
VALUES (1, '2024-03-01 13:00:00', 'ABC123', 50.0, FALSE, 2, 1),
       (2, '2024-03-02 10:00:00', 'XYZ789', 75.0, TRUE, 1, 3);

-- LotOccupancy Table
INSERT INTO LotOccupancy (lot_id, timestamp, current_occupancy)
VALUES (1, '2024-03-01 10:00:00', 30),
       (1, '2024-03-01 11:00:00', 45),
       (2, '2024-03-02 09:00:00', 20);