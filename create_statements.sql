create schema airline_tickets;
use airline_tickets;
    create table passenger(
        passenger_id int primary key auto_increment,
        first_name varchar(255) not null,
        last_name varchar(255) not null,
        passport_id VARCHAR(20) not null,
        age smallint not null     
    );
	
	create table manufacturer(
		manufacturer_id int primary key,
        manufacturer_name varchar(255) not null,
        airplane_type varchar(255) NOT NULL,
        max_capacity INT UNSIGNED NOT NULL,
        cruising_speed int not null
    );
    /*
	create table staff(
        employee_id int PRIMARY KEY,
		employee_role enum('Pilot', 'flight attendant') NOT NULL,
        first_name VARCHAR(255) NOT NULL,
		last_name VARCHAR(255) NOT NULL
    );
    */
	create table airplane(
        tail_num char(6) PRIMARY KEY,
        airplane_model int not null,
        current_location char(3) not null,
        foreign key (airplane_model) references manufacturer(manufacturer_id)
    );
    
	create table user_account(
        user_id varchar(255) primary key,
        passenger_id int not null, 
        email varchar(255) unique not null ,
        user_password varchar(255) CHECK (length(user_password) >= 20),
        security_question varchar(255),
        phone_number char(10) UNIQUE NOT NULL,
        foreign key (passenger_id) references passenger(passenger_id) on delete cascade on update cascade
    );

	create table payment(
		payment_id INT auto_increment,
        passenger_id INT NOT NULL UNIQUE,
        payment_method enum('visa', 'master card', 'paypal', 'bitcoin', 'monero') not null, 
        bank_name varchar(255)NOT NULL,
        amount_paid double NOT NULL,
        primary key (payment_id, passenger_id),
        FOREIGN KEY (passenger_id) REFERENCES passenger(passenger_id) ON UPDATE CASCADE ON DELETE CASCADE
    );
    
	create table airport(
        airport_code char(3) PRIMARY KEY,
        city_name varchar(255) not null,
        longtitude int not null,
        latitude int not null,
		temperature DECIMAL(3, 1) NOT NULL,
		wind_speed DECIMAL(3, 1) NOT NULL,
		weather ENUM ('hailing', 'sunny', 'overcast', 'raining', 'snowing')
    );

    create table flight_details(
        flight_num int primary key auto_increment,
        date_time_origin TIMESTAMP NOT NULL, 
		date_time_destination TIMESTAMP NOT NULL,  
        duration time not null,
		distance int not null,
        destination_airport_code CHAR(3) NOT NULL, 
        origin_airport_code CHAR(3) NOT NULL,
        tail_num char(6),
		available_capacity INT UNSIGNED NOT NULL,
        flight_status enum('delayed', 'on-time', 'canceled', 'N/A') not null default 'on-time',
        seat_price int not null, 
        constraint not_the_same_time CHECK (date_time_origin <> date_time_destination),
        constraint departure_before_arrival CHECK (date_time_origin < date_time_destination),
        constraint not_the_same_airport CHECK (origin_airport_code <> destination_airport_code),
        Foreign Key (destination_airport_code) REFERENCES airport(airport_code),
        Foreign Key (origin_airport_code) REFERENCES airport(airport_code),
        Foreign Key (tail_num) REFERENCES airplane(tail_num)
    );

/*
    create table staff_schedule(
        staff_id  int,
        tail_num char(6),
        Foreign Key (staff_id) REFERENCES staff(employee_id),
        Foreign Key (tail_num) REFERENCES airplane(tail_num)
    );
    */
    
	create table itinerary(
        itinerary_id int primary key auto_increment,
        seat_num int,
        flight_num int,
        passenger_id int,
		foreign key (flight_num) references flight_details(flight_num),
        foreign key (passenger_id) references passenger(passenger_id)
    ); 