
create table useraccount (
   userid varchar(10) primary key,
   password varchar(15),
   username varchar(20),
   phoneno varchar(20),
   postcode varchar(7),
   address varchar(30),
   email varchar(50),
   birthday datetime
);
DROP TABLE sale;

CREATE TABLE sale (	--주문정보
	saleid int PRIMARY KEY, --주문번호
	userid varchar(10) NOT NULL,--주문고객ID
	saledate DATETIME,	--주문일자
	FOREIGN KEY (userid) REFERENCES useraccount (userid)
);
DROP TABLE saleitem;
CREATE TABLE saleitem ( --주문상품
	saleid int ,
	seq int ,
	itemid int NOT NULL,
	quantity int,
	PRIMARY KEY (saleid, seq),
	foreign KEY (saleid) REFERENCES sale(saleid),
	foreign KEY (itemid) REFERENCES item(id)
);

SELECT * FROM useraccount;
select * FROM sale;
SELECT * FROM saleitem;