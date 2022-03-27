/* H2Dialect */
create table RegistrationInfo (
 	ID bigint not null,
	ADDRESS_ONE varchar(255),
    ADDRESS_TWO varchar(255),
    CITY varchar(255),
    EMAIL varchar(255) not null,
    FIRST_NAME varchar(255) not null,
    LAST_NAME varchar(255) not null,
    PASSWORD varchar(255) not null,
    REG_APP_NAME varchar(255) not null,
    STATE varchar(255),
    USER_NAME varchar(255) not null,
    ZIP varchar(255),
    primary key (ID)
);

create index IDX_EMAIL_APP_NAME on RegistrationInfo (EMAIL, REG_APP_NAME);

alter table RegistrationInfo
    drop constraint if exists UK7p6f6mhiq0t64kuiyochoovf1;

alter table RegistrationInfo
    add constraint UK7p6f6mhiq0t64kuiyochoovf1 unique (EMAIL, REG_APP_NAME);
    
create sequence hibernate_sequence start with 1 increment by 1;
