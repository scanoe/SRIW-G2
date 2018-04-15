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
//import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
//import org.apache.jena.rdf.model.RDFNode;
//import org.apache.jena.rdf.model.Resource;
//import org.apache.jena.rdf.model.Statement;
//import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
//import org.apache.jena.vocabulary.VCARD;

public class EjemploAPI_03 {

	public static void main(String[] args) throws FileNotFoundException {
		Model model = ModelFactory.createDefaultModel(); // creates an in-memory Jena Model
		
		// abrir el archivo con la ontolog�a
		InputStream in = FileManager.get().open( "src/owl/EPS.owl" );
		model.read(in, null, "TURTLE"); // parses an InputStream assuming RDF in Turtle format

		
		String queryString =        
				"PREFIX eps: <http://localhost:2020/resource/vocab/>" +
        		"SELECT * WHERE { " +
        					"SERVICE <http://52.67.23.207:2020/sparql> {" +
        						"		?eps eps:EPS_ideps ?id_eps;" +
        						"    	eps:EPS_código ?codigo;" +
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
                //Literal name = soln.getLiteral("EPS_ideps");
                
                String recurso = "http://www.EPSColombia.org#"+ soln.getLiteral("codigo") + "-" + soln.getLiteral("id_eps");
                //System.out.println(model.getProperty("eps"));
                Property eps = model.getProperty("eps");
                Property codigo = model.getProperty("codigo");
                Property fecha_corte = model.getProperty("fecha_corte");
                Property id_eps = model.getProperty("id_eps");
                Property linkfuente = model.getProperty("link");
                Property nomCategorias = model.getProperty("nomcategorias");
                Property nomEspecifique = model.getProperty("nomespecifique");
                Property nomFuente = model.getProperty("nomfuente");
                Property nomIndicador = model.getProperty("nomindicador");
                Property nomServicio = model.getProperty("nomservicio");
                Property nomUnidad = model.getProperty("nomunidad");
                Property resultado = model.getProperty("resultado");
                
                model.createResource(recurso)
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
                               
                
                //System.out.println("http://www.EPSColombia.org#"+ soln.getLiteral("codigo") + "-" + soln.getLiteral("id_eps"));
            }        
            OutputStream output = new FileOutputStream("src/owl/EPS-D2R.owl");
            model.write(output, "TURTLE");
        } finally {
            qexec.close();
        }
		
        /*StmtIterator iter = model.listStatements();
        try {
            while ( iter.hasNext() ) {
                Statement stmt = iter.next();
                
                Resource s = stmt.getSubject();
                Resource p = stmt.getPredicate();
                RDFNode o = stmt.getObject();
                
                if ( s.isURIResource() ) {
                    System.out.print("URI");
                } else if ( s.isAnon() ) {
                    System.out.print("blank");
                }
                
                if ( p.isURIResource() ) 
                    System.out.print(" URI ");
                
                if ( o.isURIResource() ) {
                    System.out.print("URI");
                } else if ( o.isAnon() ) {
                    System.out.print("blank");
                } else if ( o.isLiteral() ) {
                    System.out.print("literal");
                }
                
                System.out.println();                
            }
        } finally {
            if ( iter != null ) iter.close();
        }*/
	}

}
