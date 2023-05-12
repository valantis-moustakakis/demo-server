drop table if exists demo_app_user;
drop table if exists demo_user_reports;

create table demo_app_user (
	user_id bigint not null auto_increment,
	email varchar(255) primary key,
	user_pass varchar(255) not null,
	enabled boolean not null,
	account_not_expired boolean not null,
	credentials_not_expired boolean not null,
	account_non_locked boolean not null,
	unique(user_id)
);

create table demo_user_reports (
	report_id bigint primary key auto_increment,
	user_email varchar(255) not null,
	severity varchar(255) not null,
	description varchar(255) not null,
	latitude float(13,10) not null,
	longitude float(13,10) not null,
	report_date date not null
);