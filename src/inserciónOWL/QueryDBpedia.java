package inserciónOWL;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;

public class QueryDBpedia {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Model model = ModelFactory.createDefaultModel(); // creates an in-memory Jena Model
		
		// abrir el archivo con la ontologia
		InputStream in = FileManager.get().open( "src/owl/Salida.owl" );
		model.read(in, null, "TURTLE"); // parses an InputStream assuming RDF in Turtle format
		
		String queryString =   
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + 
				"PREFIX db: <http://dbpedia.org/>" +
				"PREFIX dbp: <http://dbpedia.org/property/>" + 
				"PREFIX dbo: <http://dbpedia.org/ontology/>" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
				"SELECT *" +
				"WHERE {"  +
						"SERVICE <http://es.dbpedia.org/sparql> {" +   
							"?recurso dbo:type <http://es.dbpedia.org/resource/Departamentos_de_Colombia>;" +
					             	"rdfs:label ?nombre." +
								"}" +   	
					"}"; 
		
        Query query = QueryFactory.create(queryString); //Crear un objeto para consulta
        QueryExecution qexec = QueryExecutionFactory.create(query, model); // ejecutar la consulta SPARQL
        try {
            ResultSet results = qexec.execSelect();
            while ( results.hasNext() ) {
                QuerySolution soln = results.nextSolution();
                String nomDepartamento = soln.getLiteral("nombre").toString();
                nomDepartamento = nomDepartamento.substring(0, nomDepartamento.length()-3);
                nomDepartamento = nomDepartamento.replaceAll("\\(Colombia\\)", "");
                
                String recurso = "http://www.EPSColombia.org#" + nomDepartamento.toString().replaceAll("\\s+","");
                Property nombre = model.getProperty("http://www.EPSColombia.org#departamento");
                
                model.createResource(recurso)
           		.addProperty(RDF.type, model.getResource("http://www.EPSColombia.org#Departamento"))
                .addProperty(nombre, nomDepartamento);
            }    
            OutputStream output = new FileOutputStream("src/owl/Salida.owl");
            model.write(output, "TURTLE");
        } finally {
            qexec.close();
        }
		
	}
}