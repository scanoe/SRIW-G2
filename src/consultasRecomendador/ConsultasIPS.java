package consultasRecomendador;

import java.io.InputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

public class ConsultasIPS {

	public static void mostrarConsulta(String queryString, Model model) {
        Query query = QueryFactory.create(queryString); //Crear un objeto para consulta
        QueryExecution qexec = QueryExecutionFactory.create(query, model); // ejecutar la consulta SPARQL
        try {
            ResultSetRewindable results = ResultSetFactory.makeRewindable(qexec.execSelect());
            ResultSetFormatter.out(System.out, results);
            System.out.println();
            results.reset();
        } finally {
            qexec.close();
        }
	}
	public static void main(String[] args) {
		
		Model model =  ModelFactory.createOntologyModel(); // creates an in-memory Jena Model
		// abrir el archivo con la ontolog�a
		InputStream in = FileManager.get().open( "src/owl/Salida.owl" );
		model.read(in, null, "TURTLE"); // parses an InputStream assuming RDF in Turtle format
        
		System.out.println("La 40 IPS donde los usuarios están más satisfechos");
        String queryString = 
                "PREFIX : <http://www.EPSColombia.org#> " +
        		"SELECT ?ips ?municipio ?porcentaje WHERE { " +
        		     "?recurso :ips ?ips;" +
        		     ":nomindicador ?indicador;" +
        		     ":municipio ?municipio;" +
        		     ":resultado  ?porcentaje." +
        		     "FILTER REGEX(?indicador, 'Usuarios satisfechos con el servicio prestado por la IPS')" +
        		"} ORDER BY DESC(?porcentaje)" + 
        		" LIMIT 40"; 
        
    	mostrarConsulta(queryString,model);   
    	 	
		System.out.println("La 10 IPS donde los usuarios están menos satisfechos");
        queryString = 
                "PREFIX : <http://www.EPSColombia.org#> " +
        		"SELECT ?ips ?municipio ?porcentaje WHERE { " +
        		     "?recurso :ips ?ips;" +
        		     ":nomindicador ?indicador;" +
        		     ":municipio ?municipio;" +
        		     ":resultado  ?porcentaje." +
        		     "FILTER REGEX(?indicador, 'Usuarios satisfechos con el servicio prestado por la IPS')" +
        		"} ORDER BY ASC(?porcentaje)" + 
        		" LIMIT 10"; 
        
    	mostrarConsulta(queryString,model);    
   
		System.out.println("Promedio de satisfacción de usuarios por municipio");
        queryString = 
                "PREFIX : <http://www.EPSColombia.org#> " +
        		"SELECT (AVG(?porcentaje) AS ?promedio) ?municipio WHERE { " +
        		     "?recurso :ips ?ips;" +
        		     ":nomindicador ?indicador;" +
        		     ":municipio ?municipio;" +
        		     ":resultado  ?porcentaje." +
        		     "FILTER REGEX(?indicador, 'Usuarios satisfechos con el servicio prestado por la IPS')" +
        		"} GROUP BY(?municipio)" +
        		"ORDER BY ASC(?promedio)"; 
        
    	mostrarConsulta(queryString,model);    

	}

}
