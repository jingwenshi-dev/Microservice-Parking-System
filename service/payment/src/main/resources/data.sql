-- Users Table
INSERT INTO
    users (USER_ID, FIRST_NAME, LAST_NAME, EMAIL, USER_TYPE, TOTAL_FINE)
VALUES
    (1, 'John', 'Doe', 'john.doe@example.com', 'student', 0),
    (2, 'Jane', 'Smith', 'jane.smith@example.com', 'faculty', 15),
    (3, 'Alice', 'Johnson', 'alice.johnson@example.com', 'staff', 0);

-- ParkingPermits Table
INSERT INTO
    parking_permits (TRANSPONDER_NUMBER, VALID_FROM, VALID_UNTIL, LICENSE_PLATE, PAYMENT_METHOD, PAYROLL_NUM)
VALUES
    ('TRN001', '2024-01-01', '2024-12-31', 'ABC123', 'direct', NULL),
    ('TRN002', '2024-01-01', '2024-06-30', 'DEF456', 'payroll', 101);

-- ParkingLots Table
INSERT INTO
    parking_lots (LOT_ID, LOT_NAME, TOTAL_SPOTS, VISITOR_ALLOWED, HOURLY_RATE, LOCATION)
VALUES
    (1, 'Lot A', 100, TRUE, 5.0, 'North Campus'),
    (2, 'Lot B', 50, FALSE, 3.0, 'East Campus'),
    (3, 'Lot C', 75, TRUE, 4.0, 'West Campus');

-- Visitor Table
INSERT INTO
    visitor (LICENSE_PLATE, VOUCHER, ENTRY_TIME, LOT_ID)
VALUES
    ('XYZ789', 'VOUCHER001', '2024-03-01 10:30:00', 1),
    ('GHI789', NULL, '2024-03-02 09:00:00', 3);

-- Voucher Table
INSERT INTO
    voucher (VOUCHER, VALID_FROM, VALID_UNTIL)
VALUES
    ('VOUCHER001', '2024-03-01', '2024-03-05'),
    ('VOUCHER002', '2024-03-02', '2024-03-07');

-- ParkingViolations Table
INSERT INTO
    parking_violations (VIOLATION_ID, VIOLATION_TIME, LICENSE_PLATE, FINE_AMOUNT, IS_PAID, OFFICER_ID, LOT_ID)
VALUES
    (1, '2024-03-01 13:00:00', 'ABC123', 50.0, FALSE, 2, 1),
    (2, '2024-03-02 10:00:00', 'XYZ789', 75.0, TRUE, 1, 3);

-- LotOccupancy Table
INSERT INTO
    lot_occupancy (LOT_ID, TIMESTAMP, CURRENT_OCCUPANCY)
VALUES
    (1, '2024-03-01 10:00:00', 30),
    (1, '2024-03-01 11:00:00', 45),
    (2, '2024-03-02 09:00:00', 20);
