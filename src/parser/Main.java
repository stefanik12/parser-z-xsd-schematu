/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

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
    private static final String inputPath = "src/input/";
    private static final String schema = "schema2.xsd";
    private static final String XML = "library.xml";
    private static Marshaler marshaler;

    public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        System.out.println("Schema Binder running with input schema: " + schema);
        System.out.println("_______________________________________________");
        File schemaFile = new File(inputPath + schema);
        binder = new Binder(schemaFile);
        Collection collection = binder.bind();
        
        if (collection != null) {
            System.out.println(System.lineSeparator() + "Variable initialisation from " + XML + " started");

            marshaler = new Marshaler(new File(inputPath + XML), collection);
            if (marshaler.marshal()) {
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
