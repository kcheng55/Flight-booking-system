-- isnsert relevent infomration to create a new flight 
DELIMITER //

CREATE PROCEDURE insert_flight_details(
    IN origin_airport_code CHAR(3),
    IN destination_airport_code CHAR(3),
    IN date_time_origin TIMESTAMP,
    IN tail_number CHAR(6)
)
BEGIN
    DECLARE origin_latitude INT;
    DECLARE origin_longtitude INT;
    DECLARE destination_latitude INT;
    DECLARE destination_longtitude INT;
    DECLARE distance INT;
    DECLARE destination TIMESTAMP;
    DECLARE duration TIME;
    DECLARE speed INT;
    DECLARE available_capacity INT;
    Declare price int;

    SELECT latitude, longtitude INTO origin_latitude, origin_longtitude
    FROM airport
    WHERE airport_code = origin_airport_code;

    SELECT latitude, longtitude INTO destination_latitude, destination_longtitude
    FROM airport
    WHERE airport_code = destination_airport_code;

    SELECT cruising_speed INTO speed
    FROM manufacturer
    LEFT JOIN airplane ON manufacturer.manufacturer_id = airplane.airplane_model
    WHERE tail_num = tail_number;

    SELECT max_capacity INTO available_capacity
    FROM manufacturer
    LEFT JOIN airplane ON airplane.airplane_model = manufacturer.manufacturer_id
    WHERE tail_num = tail_number;

    -- Calculate distance in miles using Haversine formula
    -- 6371 for miles and 5432 for killometers
    SET distance = ROUND(6371 * ACOS(
        COS(RADIANS(origin_latitude)) *
        COS(RADIANS(destination_latitude)) *
        COS(RADIANS(destination_longtitude) - RADIANS(origin_longtitude)) +
        SIN(RADIANS(origin_latitude)) * SIN(RADIANS(destination_latitude))
    ));

    SET duration = SEC_TO_TIME(ROUND((distance / speed) * 60 * 60));

    SET destination = date_time_origin + INTERVAL duration SECOND;
    
    set price = distance * 0.5;

    INSERT INTO flight_details (

        date_time_origin,
        date_time_destination,
        duration,
        distance,
        destination_airport_code,
        origin_airport_code,
        tail_num,
        available_capacity,
        seat_price

    )
    VALUES (

        date_time_origin,
        destination, -- this is destination time
        duration,
        distance,
        destination_airport_code,
        origin_airport_code,
        tail_number,
        available_capacity,
        price
        );
END//

DELIMITER ;

-- -------------------------------------------------------------------------------------------------------------------

DELIMITER //

CREATE PROCEDURE search_flights(
  IN origin_city VARCHAR(255),
  IN destination_city VARCHAR(255),
  IN flight_date DATE
)
BEGIN
  SELECT 
    fd.flight_num,
    fd.date_time_origin,
    fd.date_time_destination,
    fd.duration,
    fd.distance,
    fd.seat_price,
    a1.city_name AS origin_city,
    a2.city_name AS destination_city
  FROM
    flight_details fd
    INNER JOIN airport a1 ON fd.origin_airport_code = a1.airport_code
    INNER JOIN airport a2 ON fd.destination_airport_code = a2.airport_code
  WHERE
    a1.city_name = origin_city
    AND a2.city_name = destination_city
    AND DATE(fd.date_time_origin) = flight_date;
END //

DELIMITER ;

-- ------------------------------------------------------------------------------------

DELIMITER //

CREATE PROCEDURE book_flight(
  IN p_first_name VARCHAR(255),
  IN p_last_name VARCHAR(255),
  IN p_user_id VARCHAR(255),
  IN p_passport_id VARCHAR(20),
  IN p_age SMALLINT,
  IN p_email VARCHAR(255),
  IN p_user_password VARCHAR(255),
  IN p_security_question VARCHAR(255),
  IN p_phone_number CHAR(10),
  IN p_payment_method ENUM('visa', 'master card', 'paypal', 'bitcoin', 'monero'),
  IN p_bank_name VARCHAR(255),
  IN p_amount_paid DOUBLE,
  IN p_flight_num INT
)
BEGIN
  DECLARE v_seat_price INT;
  DECLARE v_total_payment DOUBLE;
  DECLARE v_passenger_id INT;
  DECLARE v_seat_num INT;
  
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    -- Rollback transaction
    ROLLBACK;
    RESIGNAL;
  END;
  
  -- Verify that the flight exists and retrieve the seat price
  SELECT seat_price INTO v_seat_price FROM flight_details WHERE flight_num = p_flight_num;
  
  IF v_seat_price IS NULL THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid flight number';
  END IF;
  
  -- Calculate the total payment
  SET v_total_payment = v_seat_price;
  
  -- Verify that the payment amount matches the seat price
  IF v_total_payment <> p_amount_paid THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid payment amount';
  END IF;
  
  -- make sure that all info or no info is submited
  START TRANSACTION;
  
  -- Insert passenger info
  INSERT INTO passenger (first_name, last_name, passport_id, age)
  VALUES (p_first_name, p_last_name, p_passport_id, p_age);
  
  SET v_passenger_id = LAST_INSERT_ID();
  
  -- Insert user account info
  INSERT INTO user_account (user_id, passenger_id, email, user_password, security_question, phone_number)
  VALUES (p_user_id, v_passenger_id, p_email, p_user_password, p_security_question, p_phone_number);
  
  -- Insert payment info
  INSERT INTO payment (passenger_id, payment_method, bank_name, amount_paid)
  VALUES (v_passenger_id, p_payment_method, p_bank_name, p_amount_paid);
  
  -- Get the available seat number
  SELECT available_capacity INTO v_seat_num FROM flight_details WHERE flight_num = p_flight_num;
  
  -- Insert itinerary with seat number
  INSERT INTO itinerary (seat_num, flight_num, passenger_id)
  VALUES (v_seat_num, p_flight_num, v_passenger_id);
  
  -- Decrement available capacity
  UPDATE flight_details
  SET available_capacity = available_capacity - 1
  WHERE flight_num = p_flight_num;
  
  -- Commit transaction
  COMMIT;
END //

DELIMITER ;


