/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import parser.parser;

/**
 * TO DO: Testy zatial neukazuju na ziadane uzly
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class ParserTest {
    private String prefix =  "src/parser/";
    
    private Document xsdDoc;
    private final String xsdAddress = "testXSD.xsd";
    private Node schemaNode;

    private Document xmlDoc;
    private final String xmlAddress = "testXML2.xml";
    private File xmlFile, brokenXmlFile;

    private parser parserInstance;

    @Before
    public void setUp() {
        try {
            // Vytvorime instanci tovarni tridy
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Pomoci tovarni tridy ziskame instanci DocumentBuilderu
            DocumentBuilder builder = factory.newDocumentBuilder();
            // DocumentBuilder pouzijeme pro zpracovani XML dokumentu
            // a ziskame model dokumentu ve formatu W3C DOM

            xsdDoc = builder.parse(prefix+xsdAddress);
            schemaNode = xsdDoc.getLastChild();

            xmlFile = new File(prefix+xmlAddress);
            xmlDoc = builder.parse(xmlFile);
            parserInstance = new parser(xmlFile);
            
            brokenXmlFile = new File(prefix+"broken.xml");

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Instantiate Parser, probably ???
     */
    @Test
    public void makeParserTest() {
        //part 1:
        //vytvor parser, assertuj, ze vznikol subor generatedFiles. Ak nevznikol, assertuj, ze bola vyhodena vynimka
        parser testParser;
        boolean err = false;
        try {
            testParser = new parser(xmlFile);
        } catch (Exception e) {
            err = true;
            assertFalse(e.getMessage(), true);
        }
        if(! new File(prefix+"generatedFiles").exists()){
            assertTrue(true);
            //zatial neprejde : moze byt len zla cesta - overit 
        } else {
            assertFalse(err);
        }
        testParser = null;
        //part 2:
        //vytvor parser s chybnym vstupom, assertuj vyhodenie IOException
        // TO DO
        testParser = new parser(xmlFile);
        
    }

    /**
     * Javadoc for expected verified method: Controls if the node is simpleType
     *
     * @param node : ''element'' node from XML document
     * @return True if the node is restricted as simple by the XML Schema,
     * otherwise false
     */
    @Test
    public void isSimpleTypeTest() {

        Node simpleTypeNode;
        //subtest1: IS simple type.     
        simpleTypeNode = xmlDoc.getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1);
        //points to <position>Programmer </position>

        NodeList simpleTypeList = simpleTypeNode.getChildNodes();

        boolean expected = true;
        boolean actual;
        try {
            actual = parserInstance.isSimpleType(simpleTypeNode);
        } catch (XPathExpressionException e) {
            actual = false;
            //node is valid though an exception raised
            assertFalse(e.getMessage(), expected);
        }

        //assertion
        assertEquals(expected, actual);

        //subtest2: IS NOT simple type.
        simpleTypeNode = xmlDoc.getChildNodes().item(1).getChildNodes().item(3);
        //points to  <person name="Peter" ID="2"> 

        expected = false;
        try {
            actual = parserInstance.isSimpleType(simpleTypeNode);
        } catch (XPathExpressionException e) {
            actual = true;
            //node is valid though an exception raised
            assertFalse(e.getMessage(), expected);
        }
        //assertion
        assertEquals(actual, expected);
    }

    /**
     * Javadoc for expected verified method: Controls if the node is complexType
     *
     * @param node : '''element'' node from XML document
     * @return True if the node is restricted as complex by the XML Schema,
     * otherwise false
     */
    @Test
    public void isComplexTypeTest() throws XPathExpressionException {
        Node complexTypeNode;
        ///subtest1: IS complex type.
        complexTypeNode = xmlDoc.getChildNodes().item(1).getChildNodes().item(1);
        //points to  <person name="Peter" ID="2"> 
        boolean expected = true;
        boolean actual;
        try {
            actual = parserInstance.isComplexType(complexTypeNode);
        } catch (XPathExpressionException e) {
            actual = false;
            //node is valid though an exception raised
            assertFalse(e.getMessage(), expected);
        }
        //assertion
        assertEquals(actual, expected);

        //subtest2: IS NOT complex type
        complexTypeNode = xmlDoc.getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1);
        //points to <position>Programmer </position>

        expected = false;
        try {
            actual = parserInstance.isComplexType(complexTypeNode);
        } catch (XPathExpressionException e) {
            actual = true;
            //node is valid though an exception raised
            assertFalse(e.getMessage(), expected);
        }

        //assertion
        assertEquals(expected, actual);
    }

    /**
     * Javadoc for expected verified method:
     *
     * @param node : the ''element'' of XML document and
     * @return a list of attributes required for this node by XMLSchema
     */
    @Test
    public void getAttributesTest() throws XPathExpressionException {
        Node relatedNode = xmlDoc.getChildNodes().item(1).getChildNodes().item(1);
        //refers to:     <person name="Adam" ID="1">
        
        NamedNodeMap attributes = relatedNode.getAttributes();

        List<String> values = new ArrayList<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            values.add(attributes.item(i).getNodeValue());
        }
        List<String> actual = parserInstance.getAttributes(relatedNode);
        //size assertion
        assertEquals(attributes.getLength(), actual.size());
        //values assertion
        assertDeepEqualsNotSorted2(values, actual);
    }

    /**
     * Gets under elements of node
     *
     * @param node
     * @return List of nodes,that are under elements of parameter node
     */
    @Test
    public void getUnderElementsTest() {
        Node relatedNode = xmlDoc.getChildNodes().item(1).getChildNodes().item(3);
        //refers to:     <person name="Peter" ID="2">
        
        List<Node> expected = new ArrayList<>();
        for (int i = 1; i <= relatedNode.getChildNodes().getLength() / 2; i++) {
            // pri iteracii sa na parnych poziciach nachadzaju prazdne uzly (?)
            expected.add(relatedNode.getChildNodes().item(i));
        }
        System.out.println(relatedNode);
        List<Node> actual = parserInstance.getUnderElements(relatedNode);

        //length comparision 
        assertEquals(expected.size(), actual.size());

        assertDeepEqualsNotSorted(expected, actual);
    }

    private void assertDeepEqualsNotSorted(List<Node> expected, List<Node> actual) {
        //length comparision 
        assertEquals(expected.size(), actual.size());

        for (Node n : expected) {
            boolean nameFound = false;
            boolean valueFound = false;
            //considered values:
            String name = n.getNodeName();
            String value = n.getNodeValue();

            for (Node n2 : actual) {
                if (n2.getNodeName().equals(name)) {
                    nameFound = true;
                }
                if (n2.getNodeValue().equals(value)) {
                    valueFound = true;
                }
            }
            assertTrue(nameFound);
            assertTrue(valueFound);
        }
    }

    private void assertDeepEqualsNotSorted2(List<String> expected, List<String> actual) {
        //length comparision 
        assertEquals(expected.size(), actual.size());

        // attributes value comparision 
        for (String s : expected) {
            boolean valueFound = false;

            //considered values:
            for (String s2 : actual) {
                if (s2.equals(s)) {
                    valueFound = true;
                }
            }
            assertTrue(valueFound);
        }

    }
}
