-- La base 'materiadb' ya se creó por el environment del compose
-- Ahora creamos la de inscripciones
CREATE DATABASE inscripciondb;

-- Creamos el usuario específico para inscripciones
CREATE USER inscripcion_user WITH PASSWORD 'inscripcion_pass';

-- Le damos todos los permisos sobre su base de datos
GRANT ALL PRIVILEGES ON DATABASE inscripciondb TO inscripcion_user;