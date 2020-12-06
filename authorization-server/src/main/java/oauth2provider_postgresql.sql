-- oauth2.tbl_users definition

-- Drop table

-- DROP TABLE tbl_users;

CREATE TABLE tbl_users (
	userid varchar(20) NOT NULL,
	password varchar(20) NOT NULL,
	username varchar(40) NULL,
	userno numeric(19) NULL,
	CONSTRAINT pk_users PRIMARY KEY (userid),
	CONSTRAINT tbl_users_userno_key UNIQUE (userno)
);


-- oauth2.tbl_client definition

-- Drop table

-- DROP TABLE tbl_client;

CREATE TABLE tbl_client (
	client_id varchar(100) NOT NULL,
	client_secret varchar(100) NOT NULL,
	userid varchar(20) NOT NULL,
	client_name varchar(300) NOT NULL,
	description varchar(400) NOT NULL,
	client_url varchar(300) NOT NULL,
	client_type varchar(20) NOT NULL,
	scope varchar(300) NULL,
	redirect_uri varchar(400) NOT NULL,
	regdate date NULL DEFAULT now(),
	CONSTRAINT pk_client PRIMARY KEY (client_id),
	CONSTRAINT fk_client_users FOREIGN KEY (userid) REFERENCES oauth2.tbl_users(userid)
);


-- oauth2.tbl_token definition

-- Drop table

-- DROP TABLE tbl_token;

CREATE TABLE tbl_token (
	client_id varchar(100) NOT NULL,
	userid varchar(20) NOT NULL,
	access_token varchar(200) NOT NULL,
	refresh_token varchar(200) NULL,
	token_type varchar(30) NULL,
	scope varchar(100) NULL,
	code varchar(200) NULL,
	state varchar(100) NULL,
	client_type varchar(20) NULL,
	created_at numeric(30) NULL,
	created_rt numeric(30) NULL,
	expires_in numeric(30) NULL,
	CONSTRAINT pk_token PRIMARY KEY (client_id, userid, access_token),
	CONSTRAINT tbl_token_access_token_key UNIQUE (access_token),
	CONSTRAINT tbl_token_refresh_token_key UNIQUE (refresh_token),
	CONSTRAINT fk_token_client FOREIGN KEY (client_id) REFERENCES oauth2.tbl_client(client_id),
	CONSTRAINT fk_token_users FOREIGN KEY (userid) REFERENCES oauth2.tbl_users(userid)
);

INSERT INTO tbl_users (userid, password, username, userno) 
  values ('gdhong', 'gdhong', '홍길동', 1000001);
INSERT INTO tbl_users (userid, password, username, userno) 
  values ('arnold', 'arnold', '아놀드', 1000002);
INSERT INTO tbl_users (userid, password, username, userno) 
  values ('t1000', 't1000', 'T-1000', 1000003);
commit;

INSERT INTO tbl_client (client_id,client_secret,userid,client_name,description,client_url,client_type,"scope",redirect_uri,regdate) VALUES ('9980228a-1fd8-4501-be77-ce8e98eed18c','8117a5d75e9909eb7858b5638803d72c707fb744','gdhong','TestApp1','TestApp1 입니다.','http://localhost:8000','W','reademail,sendemail,readboard,personalinfo,calendar','http://localhost:8000/oauth2client/callback.jsp','2020-11-27');
INSERT INTO tbl_client (client_id,client_secret,userid,client_name,description,client_url,client_type,"scope",redirect_uri,regdate) VALUES ('e2e1c234-aa02-4bd2-ae75-329edac4c1bb','566d730e9a37fb1edad370f7f93ac1ce24b99644','gdhong','UserAgent Test App','UserAgent Test App 입니다.','http://localhost:8000','M','calendar,personalinfo,readboard','http://localhost:8000/oauth2client_agentflow/callback.jsp','2020-11-27');
commit;

CREATE OR REPLACE FUNCTION get_timestamp(ts timestamp without time zone)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$ 
 declare totalms   numeric(19); 
 begin 
	select extract( day from diff )*24*60*60*1000 +
            extract( hour from diff )*60*60*1000 +
            extract( minute from diff )*60*1000 +
            round(extract( second from diff )*1000) into totalms
     from (select ts - TIMESTAMP'1970-01-01 00:00:00 +0:00' as diff ) dd;
 	 return totalms; 
 end; 
 $function$
;

