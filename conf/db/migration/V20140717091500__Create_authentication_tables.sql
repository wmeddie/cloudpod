ALTER SCHEMA DEFAULT CHARACTER SET utf8mb4  DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE TABLE logins (
  id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
  email character varying(64) NOT NULL,
  password character varying(128) NULL,
  salt character varying(64) NULL,
  difficulty integer NULL,
  provider character varying(64) NULL,
  token character varying(255) NULL,
  created_at datetime NOT NULL,
  updated_at datetime NOT NULL,
  deleted_at datetime NULL,
  UNIQUE (email, deleted_at)
);

