-- 用户表
INSERT INTO
    users (USER_ID, FIRST_NAME, LAST_NAME, EMAIL, USER_TYPE, TOTAL_FINE)
VALUES
    -- 用户ID, 名, 姓, 邮箱, 用户类型（学生、员工、访客）, 总罚款金额
    (1, 'John', 'Doe', 'john.doe@example.com', 'student', 0),
    (2, 'Jane', 'Smith', 'jane.smith@example.com', 'staff', 15),
    (3, 'Alice', 'Johnson', 'alice.johnson@example.com', 'visitor', 0);

-- 停车许可证表
INSERT INTO
    parking_permits (TRANSPONDER_NUMBER, VALID_FROM, VALID_UNTIL, LICENSE_PLATE, PAYMENT_METHOD, PAYROLL_NUM)
VALUES
    -- 转发器编号, 有效期起始时间, 有效期结束时间, 车牌号, 支付方式（直接支付/工资扣除）, 工资编号（可选）
    ('TRN001', '2024-01-01', '2024-12-31', 'ABC123', 'direct', NULL),
    ('TRN002', '2024-01-01', '2024-06-30', 'DEF456', 'payroll', 101);

-- 停车场表
INSERT INTO
    parking_lots (LOT_ID, LOT_NAME, TOTAL_SPOTS, VISITOR_ALLOWED, HOURLY_RATE, LOCATION)
VALUES
    -- 停车场ID, 停车场名称, 总车位数, 是否允许访客, 每小时收费, 停车场位置
    (1, 'Lot A', 100, TRUE, 5.0, 'North Campus'),
    (2, 'Lot B', 50, FALSE, 3.0, 'East Campus'),
    (3, 'Lot C', 75, TRUE, 4.0, 'West Campus');

-- 访客表
INSERT INTO
    visitor (LICENSE_PLATE, VOUCHER, ENTRY_TIME, LOT_ID)
VALUES
    -- 车牌号, 访客凭证（可选）, 进入时间, 停车场ID
    ('XYZ789', 'VOUCHER001', '2024-03-01 10:30:00', 1),
    ('GHI789', NULL, '2024-03-02 09:00:00', 3);

-- 凭证表
INSERT INTO
    voucher (VOUCHER, VALID_FROM, VALID_UNTIL)
VALUES
    -- 凭证编号, 有效期起始时间, 有效期结束时间
    ('VOUCHER001', '2024-03-01', '2024-03-05'),
    ('VOUCHER002', '2024-03-02', '2024-03-07');

-- 停车违规表
INSERT INTO
    parking_violations (VIOLATION_ID, VIOLATION_TIME, LICENSE_PLATE, FINE_AMOUNT, IS_PAID, OFFICER_ID, LOT_ID)
VALUES
    -- 违规ID, 违规时间, 车牌号, 罚款金额, 是否支付, 执法人员ID, 停车场ID
    (1, '2024-03-01 13:00:00', 'ABC123', 50.0, FALSE, 2, 1),
    (2, '2024-03-02 10:00:00', 'XYZ789', 75.0, TRUE, 1, 3);

-- 停车场占用表
INSERT INTO
    lot_occupancy (LOT_ID, TIMESTAMP, CURRENT_OCCUPANCY)
VALUES
    -- 停车场ID, 时间戳, 当前占用车位数
    (1, '2024-03-01 10:00:00', 30),
    (1, '2024-03-01 11:00:00', 45),
    (2, '2024-03-02 09:00:00', 20);
