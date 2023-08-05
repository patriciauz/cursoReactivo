

CREATE TABLE   IF NOT EXISTS programa (
     id INT NOT NULL AUTO_INCREMENT,
     nombre VARCHAR2(150),
    PRIMARY KEY (id));

CREATE TABLE  IF NOT EXISTS curso (
     id INT NOT NULL AUTO_INCREMENT,
     nombre VARCHAR2(150),
    PRIMARY KEY (id));

CREATE TABLE  IF NOT EXISTS estudiante (
    id INT NOT NULL AUTO_INCREMENT,
    identificacion int not null,
    nombre_Completo VARCHAR2(150),
    id_Programa NUMERIC(16,2),
    PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS nota (
     id INT NOT NULL AUTO_INCREMENT,
     valor NUMERIC(16,2),
     id_Curso NUMERIC(16,2),
     id_Estudiante NUMERIC(16,2),
    PRIMARY KEY (id));
