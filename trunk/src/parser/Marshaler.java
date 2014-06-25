/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class Marshaler {

    Document xml;
    String collectionName;
    Collection collection;

    private XPath xpath;
    private String expres;
    private final String prefix = "xsd";

    //1. load XML
    //input XML must be valid for the schema
    public Marshaler(File xmlFile, String collectionPath) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();

            xml = docBuilder.parse(xmlFile);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            System.out.println("Invalid XML file");
        }
        // other initialisation
        this.collectionName = collectionPath;
        xpath = XPathFactory.newInstance().newXPath();
    }

    //2. load colection
    public boolean marshal() throws XPathExpressionException {
        try {
            collection = (Collection) CollectionManager.selectFromFile("generated/" + collectionName);
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("collection " + collectionName + " loading failed");
        }
        List<String> XMLelements = collection.getNameList();

        //3. for each element in collection find element in XML and instantiate desired values 
        expres = "/descendant::*[local-name()='element']";
        NodeList XSDElements = (NodeList) xpath.evaluate(expres, xml, XPathConstants.NODESET);
        for(int i = 0;i<XSDElements.getLength(); i++){
            Node n = XSDElements.item(i);
            
        }
        
        //4. initialize data into generated classes
        
        
        //? generate Parser.java ? 
        
        return true;
    }

}
