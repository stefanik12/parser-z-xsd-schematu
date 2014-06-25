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
public class MainBind {
    static NewBinder binder;
    static String schemaPath = "src/parser/testXSD.xsd";
    
    public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
        System.out.println("hey there");
        File schemaFile = new File(schemaPath);
        binder = new NewBinder(schemaFile);
        binder.bind();
        
    }
}
