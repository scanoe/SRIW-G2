/* De acuerdo al dataset EPS.xml se crea esta tabla desde phpmyadmin, se hace uso de xampp(MySQL y Apache)*/
CREATE TABLE EPS(
    código VARCHAR(5),
    ideps VARCHAR(6),
    eps VARCHAR(30),
    nomcategorias VARCHAR(20),
    nomservicio VARCHAR(20),
    nomespecifique VARCHAR(20),
    nomindicador VARCHAR(80),
    resultado DOUBLE(2,2),
    nomunidad VARCHAR(4),
    nomfuente VARCHAR(8),
    fechacorte DATE,
    linkfuente VARCHAR(80),
    PRIMARY KEY(código, ideps)
)