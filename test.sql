create table User (
	id INT NOT NULL AUTO_INCREMENT,
    name varchar(20),
    phone varchar(20),
    PRIMARY KEY(id)
);
 
 create table user_detail (
	id INT NOT NULL,
    address varchar(20),
    birth varchar(20),
    gender varchar(20),
    symptom1 varchar(20),
    symptom2 varchar(20),
    symptom3 varchar(20),
    symptom4 varchar(20),
    etc varchar(45),
    PRIMARY KEY(id)
);

create table reserve (
	id INT NOT NULL,
    hospital varchar(20),
    date varchar(20),
    status varchar(20),
    PRIMARY KEY(id)
);

create table manager_result (
	user_id INT NOT NULL,
    result varchar(45),
    PRIMARY KEY(user_id)
);