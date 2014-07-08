/*
Project for PV138 as tought on Faculty of Informatics on Masaryk University in 2014
 */
package source;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class Main {

    private static Binder binder;
    private static String inputPath = "D:/Codes/MU_scripty/Znacky/Projekt/trunk/src/input/";
    private static String schema = "complexSchema.xsd";
    private static String XML = "complexXML.xml";
    private static File outputDir;
    private static Marshaler marshaler;

    public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        File schemaFile = new File(inputPath + schema);
        File XMLFile = new File(inputPath + XML);
        
        
        if (args.length == 3) {
            if (args[0] != null && args[1] != null && args[2] != null) {
                schemaFile = new File(args[0]);
                schema = schemaFile.getName();

                XMLFile = new File(args[1]);
                XML = XMLFile.getName();

                outputDir = new File(args[2]+"/generated");
            }
        } else {
            System.out.println("NOTE: not all input files given: application runs in test mode with implicit input files. Output folder set to $Project/src/generated");
            outputDir = new File(new FileManager().getProjectHome()+"src/generated");
        }

        System.out.println("Schema Binder running with input schema: " + schema);
        System.out.println("_______________________________________________");
        
        Collection collection;
        /* uncomment code below and comment Binder usage to load collection from external binary
         - can be used if multiple XMLs is bound for one Scherma
            
         collection = (Collection) FileManager.selectFromFile(collectionPath);
         */
        binder = new Binder(schemaFile);
        collection = binder.bind(outputDir);

        if (collection != null) {
            System.out.println(System.lineSeparator() + "Variable initialisation from " + XML + " started");

            marshaler = new Marshaler(XMLFile, collection);
            if (marshaler.marshal(outputDir)) {
                System.out.println("_______________________________________________");
                System.out.println("Success.");
            } else {
                System.out.println("Error: data initialisation failed.");
            }
        } else {
            System.out.println("Error: Schema binding failed.");
        }
    }
}
