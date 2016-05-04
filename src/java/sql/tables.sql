CREATE TABLE Booking (Booking_id int NOT NULL GENERATED ALWAYS AS IDENTITY        (START WITH 1, INCREMENT BY 1), member_id int NOT NULL, user_name varchar(255) NOT NULL, PRIMARY KEY (Booking_id)) ;
CREATE TABLE Facility (facility_id int NOT NULL GENERATED ALWAYS AS IDENTITY        (START WITH 1, INCREMENT BY 1), facility_name int, facility_fee decimal(19, 4) NOT NULL, booking_duration_unit int, PRIMARY KEY (facility_id)) ;
CREATE TABLE Member (member_id int NOT NULL GENERATED ALWAYS AS IDENTITY        (START WITH 1, INCREMENT BY 1), first_name varchar(50) NOT NULL, last_name varchar(50), DOB date, member_type varchar(20) NOT NULL CHECK(member_type in ('staff','student')), PRIMARY KEY (member_id)) ;
CREATE TABLE Room (facility_id int NOT NULL, Room_type varchar(20) NOT NULL CHECK(Room_type in ('lecture_theater','class_room','computer_lab')), Room_Capacity int NOT NULL, PRIMARY KEY (facility_id)) ;
CREATE TABLE Equipment (facility_id int NOT NULL, equipment_type varchar(20) NOT NULL CHECK(equipment_type in ('digital_camera','laptop','projector','external_hard_drive')), equipment_Bought_Date date NOT NULL, PRIMARY KEY (facility_id)) ;
CREATE TABLE Administrator (user_name varchar(255) NOT NULL, member_id int NOT NULL UNIQUE, password varchar(255) NOT NULL, PRIMARY KEY (user_name)) ;
CREATE TABLE Sub_Booking (sub_booking_id int NOT NULL GENERATED ALWAYS AS IDENTITY        (START WITH 1, INCREMENT BY 1), start_date date NOT NULL, end_date date NOT NULL, sub_booking_satuts varchar(20) NOT NULL CHECK(sub_booking_satuts in ('active','inactive')), Booking_id int NOT NULL, facility_id int NOT NULL, PRIMARY KEY (sub_booking_id)) ;
ALTER TABLE Room  ADD CONSTRAINT FKRoom129240 FOREIGN KEY (facility_id) REFERENCES Facility (facility_id);
ALTER TABLE Equipment  ADD CONSTRAINT FKEquipment326374 FOREIGN KEY (facility_id) REFERENCES Facility (facility_id);
ALTER TABLE Administrator  ADD CONSTRAINT FKAdmin655450 FOREIGN KEY (member_id) REFERENCES Member (member_id);
ALTER TABLE Booking  ADD CONSTRAINT FKBooking91104 FOREIGN KEY (member_id) REFERENCES Member (member_id);
ALTER TABLE Booking  ADD CONSTRAINT FKBooking831737 FOREIGN KEY (user_name) REFERENCES Administrator (user_name);
ALTER TABLE Sub_Booking  ADD CONSTRAINT FKSub_Bookin516859 FOREIGN KEY (Booking_id) REFERENCES Booking (Booking_id);
ALTER TABLE Sub_Booking  ADD CONSTRAINT FKSub_Bookin316460 FOREIGN KEY (facility_id) REFERENCES Facility (facility_id);

