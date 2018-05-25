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


public class QueryD2R {

	public static void main(String[] args) throws FileNotFoundException {
		Model model =  ModelFactory.createOntologyModel(); // creates an in-memory Jena Model
		
		// abrir el archivo con la ontologï¿½a
		InputStream in = FileManager.get().open( "src/owl/EPS.owl" );
		model.read(in, null, "TURTLE"); // parses an InputStream assuming RDF in Turtle format

		
		String queryString =        
				"PREFIX eps: <http://localhost:2020/resource/vocab/>" +
        		"SELECT * WHERE { " +
        					"SERVICE <http://52.67.23.207:2020/sparql> {" +
        						"		?eps eps:EPS_ideps ?id_eps;" +
        						"    	eps:EPS_cÃ³digo ?codigo;" +
        						"		eps:EPS_eps ?nombre;" +
        						"		eps:EPS_fechacorte ?fecha_corte;" +
        						"		eps:EPS_linkfuente ?link;" +
        						"		eps:EPS_nomcategorias ?nomcategorias;" +
        						"		eps:EPS_nomespecifique ?nomespecifique;" +
        						"		eps:EPS_nomfuente ?nomfuente;" +
        						"		eps:EPS_nomindicador ?nomindicador;" +
        						"		eps:EPS_nomservicio ?nomservicio;" +
        						"		eps:EPS_nomunidad ?nomunidad;" +
        						"		eps:EPS_resultado ?resultado." +
        					"}" +	
        		"}";
        Query query = QueryFactory.create(queryString); //Crear un objeto para consulta
        QueryExecution qexec = QueryExecutionFactory.create(query, model); // ejecutar la consulta SPARQL
        try {
            ResultSet results = qexec.execSelect();
            while ( results.hasNext() ) {
                QuerySolution soln = results.nextSolution();
                
                String recurso = "http://www.EPSColombia.org#"+ soln.getLiteral("codigo") + "-" + soln.getLiteral("id_eps");
                Property eps = model.getProperty("http://www.EPSColombia.org#eps");
                Property codigo = model.getProperty("http://www.EPSColombia.org#codigo");
                Property fecha_corte = model.getProperty("http://www.EPSColombia.org#fecha_corte");
                Property id_eps = model.getProperty("http://www.EPSColombia.org#ideps");
                Property linkfuente = model.getProperty("http://www.EPSColombia.org#linkfuente");
                Property nomCategorias = model.getProperty("http://www.EPSColombia.org#nomcategorias");
                Property nomEspecifique = model.getProperty("http://www.EPSColombia.org#nomespecifique");
                Property nomFuente = model.getProperty("http://www.EPSColombia.org#nomfuente");
                Property nomIndicador = model.getProperty("http://www.EPSColombia.org#nomindicador");
                Property nomServicio = model.getProperty("http://www.EPSColombia.org#nomservicio");
                Property nomUnidad = model.getProperty("http://www.EPSColombia.org#nomunidad");
                Property resultado = model.getProperty("http://www.EPSColombia.org#resultado");
                
                
                model.createResource(recurso)
                	.addProperty(RDF.type, model.getResource("http://www.EPSColombia.org#EPS"))
                	.addProperty(eps, soln.getLiteral("nombre"))
                	.addProperty(codigo, soln.getLiteral("codigo"))
                	.addProperty(fecha_corte, soln.getLiteral("fecha_corte"))
                	.addProperty(id_eps, soln.getLiteral("id_eps"))
                	.addProperty(linkfuente, soln.getLiteral("link"))
                	.addProperty(nomCategorias, soln.getLiteral("nomcategorias"))
                	.addProperty(nomEspecifique, soln.getLiteral("nomespecifique"))
                	.addProperty(nomFuente, soln.getLiteral("nomfuente"))
                	.addProperty(nomIndicador, soln.getLiteral("nomindicador"))
                	.addProperty(nomServicio, soln.getLiteral("nomservicio"))
                	.addProperty(nomUnidad, soln.getLiteral("nomunidad"))
                	.addProperty(resultado, soln.getLiteral("resultado"));
                
            }        
            OutputStream output = new FileOutputStream("src/owl/Salida.owl");
            model.write(output, "TURTLE");
        } finally {
            qexec.close();
        }
		
	}

}
