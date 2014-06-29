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

    private static NewBinder binder;
    private static String schemaPath = "src/input/testXSD.xsd";
    private static Marshaler marshaler;
    private static Collection collection = null;

    public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        System.out.println("hey there");
        File schemaFile = new File(schemaPath);
        binder = new NewBinder(schemaFile);
        collection = binder.bind();
        if (collection != null) {
            //iniitializations for Marshaler
            String xmlPath = "src/input/" + "testXML2.xml";

            String schemaName = schemaFile.getName().replace(".xsd", "");
            String collectionPath = "src/generated/" + schemaName + "Col" + ".bin";

            marshaler = new Marshaler(new File(xmlPath), collection);
            marshaler.marshal();
        }
    }
}
