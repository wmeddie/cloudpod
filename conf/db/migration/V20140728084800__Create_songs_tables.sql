
CREATE TABLE songs (
  id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
  title character varying(255) NOT NULL,
  artist character varying(255) NOT NULL,
  album character varying(255) NOT NULL,
  length integer NOT NULL,
  url character varying(255) NOT NULL,
  created_at datetime NOT NULL,
  updated_at datetime NOT NULL,
  deleted_at datetime NULL
);

