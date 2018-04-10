
<?php
/* Como el dataset esta en formato xml, haciendo uso de xampp en htdocs se crea una ruta la cual contiene este
script(nombrarlo como index.php) y el dataset EPS.xml*/
$conn = mysqli_connect("localhost", "root", "", "EPS");
mysqli_set_charset($conn,"utf8");

$affectedRow = 0;

$xml = simplexml_load_file("EPS.xml") or die("Error: Cannot create object");

foreach ($xml->children() as $row) {

    $codigo = $row->codigo;
    $ideps = $row->ideps;
    $eps = $row->eps;
    $nomcategorias = $row->nomcategorias;
    $nomservicio = $row->nomservicio;
    $nomespecifique = $row->nomespecifique;
    $nomindicador = $row->nomindicador;
    $resultado = $row->resultado;
    $nomunidad = $row->nomunidad;
    $nomfuente = $row->nomfuente;
    $fechacorte = $row->fecha_corte;
    $linkfuente = $row->linkfuente;

    $sql = "INSERT INTO EPS VALUES ('" . $codigo . "','" . $ideps . "','" . $eps . "','" . $nomcategorias . "','" . 
    $nomservicio . "','" . $nomespecifique  . "','" . $nomindicador . "','" . $resultado . "','" . $nomunidad . "','"
    . $nomfuente . "','" . $fechacorte . "','" . $linkfuente . "')";
    
    $result = mysqli_query($conn, $sql);
    
    if (! empty($result)) {
        $affectedRow ++;
    } else {
        $error_message = mysqli_error($conn) . "\n";
    }
}
?>
<h2>Insert XML Data to MySql Table Output</h2>
<?php
if ($affectedRow > 0) {
    $message = $affectedRow . " records inserted";
} else {
    $message = "No records inserted";
}
echo $message;
?>