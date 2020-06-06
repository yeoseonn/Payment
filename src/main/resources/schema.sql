CREATE TABLE pay_cur_info(
id INT(11) auto_increment,
payment_id VARCHAR(20) NOT NULL,
pay_type VARCHAR(10) NOT NULL,
cur_vat INT(11),
cur_pay_amount INT(11),
plan_month VARCHAR(2),
created DATETIME DEFAULT CURRENT_TIMESTAMP ,
updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY(id)
);

CREATE TABLE pay_req_info(
id INT(11) auto_increment,
payment_id VARCHAR(20),
origin_payment_id VARCHAR(20),
pay_type VARCHAR(10) NOT NULL,
vat INT(11),
pay_amount INT(11) NOT NULL,
plan_month VARCHAR(2),
processed_data VARCHAR(450),
created DATETIME DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(id)
);