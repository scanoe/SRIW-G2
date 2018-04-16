package consultasRecomendador;

import java.io.InputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;


public class consultasEPS {

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
		// TODO Auto-generated method stub
		Model model =  ModelFactory.createOntologyModel(); // creates an in-memory Jena Model
		
		// abrir el archivo con la ontolog�a
		InputStream in = FileManager.get().open( "src/owl/ips-virtuoso.owl" );
		model.read(in, null, "TURTLE"); // parses an InputStream assuming RDF in Turtle format
        
		System.out.println("Las 10 EPS con menor tiempo en asignar citas médicas");
        String queryString = 
                "PREFIX : <http://www.EPSColombia.org#> " +
        		"SELECT DISTINCT ?eps ?tiempoDias WHERE { " +
        		     "?recurso :eps ?eps;" +
        		     ":nomservicio ?servicio;" +
        		     ":resultado  ?tiempoDias." +
        		     "FILTER REGEX(?servicio, 'Citas médicas')" +
        		"} ORDER BY ASC(?tiempoDias)" + 
        		" LIMIT 10"; 
        
    	mostrarConsulta(queryString,model);
    	
    	
    	System.out.println("Las 50 EPS con menor tiempo en realizar autorizaciones");
        queryString = 
                "PREFIX : <http://www.EPSColombia.org#> " +
        		"SELECT ?eps ?tiempoDias WHERE { " +
        		     "?recurso :eps ?eps;" +
        		     ":nomservicio ?servicio;" +
        		     ":resultado  ?tiempoDias." +
        		     "FILTER REGEX(?servicio, 'Autorizaciones')" +
        		"} ORDER BY ASC(?tiempoDias)" + 
        		" LIMIT 50"; 
        
    	mostrarConsulta(queryString,model);
    	
    	
    	System.out.println("Las 20 EPS con menor tiempo en realizar autorizaciones para cirujia de revascularización");
        queryString = 
                "PREFIX : <http://www.EPSColombia.org#> " +
        		"SELECT ?eps ?tiempoDias WHERE { " +
        		     "?recurso :eps ?eps;" +
        		     ":nomindicador ?indicador;" +
        		     ":resultado  ?tiempoDias." +
        		     "FILTER REGEX(?indicador, 'Tiempo promedio de espera  para la autorización de Cirugía de revascularización')" +
        		"} ORDER BY ASC(?tiempoDias)" + 
        		" LIMIT 20"; 
        
    	mostrarConsulta(queryString,model);
    }
	
}
