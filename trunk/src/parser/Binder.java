/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parser;

import java.io.IOException;
import java.net.URI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 *
 * @author Sven Relovsky
 */
public class Binder {
    private Document doc;
    private XPath xpath;
    
    /**
    * Constructor creating an instance of this class of given URI
     * @param uri
    */
    public Binder(URI uri) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory domFac = DocumentBuilderFactory.newInstance();
        domFac.setNamespaceAware(true);
        DocumentBuilder builder = domFac.newDocumentBuilder();
        doc = builder.parse(uri.toString());
        
        XPathFactory factory = XPathFactory.newInstance();
        xpath = factory.newXPath();
               
        
    }
    
    
    
}
