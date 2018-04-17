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
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;

public class QueryMunicipiosDBPedia {

	public static void main(String[] args) throws FileNotFoundException {
Model model = ModelFactory.createDefaultModel(); // creates an in-memory Jena Model
		
		// abrir el archivo con la ontologia
		InputStream in = FileManager.get().open( "src/owl/departamentos-dbpedia.owl" );
		model.read(in, null, "TURTLE"); // parses an InputStream assuming RDF in Turtle format
		
		String queryString =        
				"PREFIX ips: <http://www.EPSColombia.org#>" +
        		"SELECT DISTINCT ?municipios ?departamento WHERE { " +  
        		"    ?ips ips:departamento ?departamento;" +
    			"    ips:municipio ?municipios." +	
        		"}";
		Query query = QueryFactory.create(queryString); //Crear un objeto para consulta
        QueryExecution qexec = QueryExecutionFactory.create(query, model); // ejecutar la consulta SPARQL
        try {            
            ResultSet results = qexec.execSelect();
            while ( results.hasNext() ) {
            	QuerySolution soln = results.nextSolution();
            	Property municipio = model.getProperty("http://www.EPSColombia.org#Municipio");
            	String departamentoRecurso = "http://www.EPSColombia.org#" + soln.getLiteral("departamento").toString().replaceAll("\\s", "");
            	Property departamento = model.getProperty("http://www.EPSColombia.org#Departamento");
            	Property contenidoPor = model.getProperty("http://www.EPSColombia.org#Contenido_Por");
            	String nombreRecurso = "http://www.EPSColombia.org#"+ soln.getLiteral("municipios").toString().replaceAll("\\s", "");
            	model.createResource(nombreRecurso)
            		.addProperty(RDF.type, municipio)
            		.addProperty(contenidoPor, model.getResource(departamentoRecurso));
            		
            }
            OutputStream output = new FileOutputStream("src/owl/municipios.owl");
            model.write(output, "TURTLE");
        } finally {
            qexec.close();
        }
        
	}
}
