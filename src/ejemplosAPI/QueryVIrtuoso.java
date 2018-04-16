package ejemplosAPI;

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

public class QueryVIrtuoso {

	public static void main(String[] args) throws FileNotFoundException {
		Model model = ModelFactory.createDefaultModel(); // creates an in-memory Jena Model
		
		// abrir el archivo con la ontologia
		InputStream in = FileManager.get().open( "src/owl/EPS.owl" );
		model.read(in, null, "TURTLE"); // parses an InputStream assuming RDF in Turtle format

		
		String queryString =        
				"PREFIX ips: <https://www.datos.gov.co/resource/thui-g47e/>" +
        		"SELECT * WHERE { " +
        					"SERVICE <http://52.67.23.207:8890/sparql> {" +
        						"		?ips ips:ips ?nombre;" +
        						"    	ips:departamento ?departamento;" +
        						"		ips:nomunidad ?nomunidad;" +
        						"		ips:enlace ?link;" +
        						"		ips:denominador ?denominador;" +
        						"		ips:nomespecifique ?nomespecifique;" +
        						"		ips:coddepartamento ?coddepartamento;" +
        						"		ips:municipio ?municipio;" +
        						"		ips:nomindicador ?nomindicador;" +
        						"		ips:numerador ?numerador;" +
        						"		ips:nomfuente ?nomfuente;" +
        						"		ips:codmunicipio ?codmunicipio;" +
        						"		ips:idips ?idips;" +
        						"		ips:nomservicio ?nomservicio;" +
        						"		ips:nomcategorias ?nomcategorias;" +
        						"		ips:resultado ?resultado." +
        					"}" +	
        		"} LIMIT 50";
        Query query = QueryFactory.create(queryString); //Crear un objeto para consulta
        QueryExecution qexec = QueryExecutionFactory.create(query, model); // ejecutar la consulta SPARQL
        try {
            ResultSet results = qexec.execSelect();
            while ( results.hasNext() ) {
                QuerySolution soln = results.nextSolution();
                
                String recurso = "http://www.EPSColombia.org#"+ soln.getLiteral("idips") + "-" + soln.getLiteral("nomservicio");
                Property nombre = model.getProperty("nombre");
                Property departamento  = model.getProperty("departamento");
                Property nomunidad = model.getProperty("nomunidad");
                Property link = model.getProperty("link");
                Property denominador = model.getProperty("denominador");
                Property nomespecifique = model.getProperty("nomespecifique");
                Property coddepartamento = model.getProperty("coddepartamento");
                Property municipio = model.getProperty("municipio");
                Property nomindicador = model.getProperty("nomindicador");
                Property numerador = model.getProperty("numerador");
                Property nomfuente = model.getProperty("nomfuente");
                Property codmunicipio = model.getProperty("codmunicipio");
                Property idips = model.getProperty("idips");
                Property nomservicio = model.getProperty("nomservicio");
                Property nomcategorias = model.getProperty("nomcategorias");
                Property resultado = model.getProperty("resultado");
                
                model.createResource(recurso)
                	.addProperty(nombre, soln.getLiteral("nombre"))
                	.addProperty(departamento , soln.getLiteral("departamento"))
                	.addProperty(nomunidad , soln.getLiteral("nomunidad"))
                	.addProperty(link, soln.getLiteral("link"))
                	.addProperty(denominador, soln.getLiteral("denominador"))
                	.addProperty(nomespecifique, soln.getLiteral("nomespecifique"))
                	.addProperty(coddepartamento, soln.getLiteral("coddepartamento"))
                	.addProperty(municipio, soln.getLiteral("municipio"))
                	.addProperty(nomindicador, soln.getLiteral("nomindicador"))
                	.addProperty(numerador, soln.getLiteral("numerador"))
                	.addProperty(nomfuente, soln.getLiteral("nomfuente"))
                	.addProperty(codmunicipio, soln.getLiteral("codmunicipio"))
                	.addProperty(idips, soln.getLiteral("idips"))
                	.addProperty(nomservicio, soln.getLiteral("nomservicio"))
                	.addProperty(nomcategorias, soln.getLiteral("nomcategorias"))
                	.addProperty(resultado, soln.getLiteral("resultado"));
                	
              
                	System.out.println(soln);
            }  
            //model.write(System.out, "TURTLE");
            OutputStream output = new FileOutputStream("src/owl/ips-virtuoso.owl");
            model.write(output, "TURTLE");
        } finally {
            qexec.close();
        }
		
	}

}
