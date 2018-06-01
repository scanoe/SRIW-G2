package razonador;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.reasoner.ValidityReport.Report;
import org.apache.jena.util.FileManager;
import org.apache.jena.rdf.model.ModelFactory;
import razonador.razonador;



public class razonador {
	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		Model model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open( "src/owl/Salida.owl" );
		model.read(in,null,"TURTLE");
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();//.bindSchema(model.getGraph());
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		ValidityReport validityReport = inf.validate();
		
        if ( !validityReport.isValid() ) {
        	System.out.println("Inconsistent");
            Iterator<Report> iter = validityReport.getReports();
            while ( iter.hasNext() ) {
            	Report report = iter.next();
            	System.out.println(report);
            }        	
        } else {
        	System.out.println("Valid");
        	OutputStream output = new FileOutputStream("src/owl/Salida.owl");
            inf.write(output, "TURTLE");
        }

        
        
		
		
		
		
		
	}

}
