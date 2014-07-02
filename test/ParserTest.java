/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import parser.Binder;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class ParserTest {

    private final String prefix = "src/input/";

    private final String xsdAddress = "testXSD.xsd";
    InputSource xsdIS;

    private final String xmlAddress = "testXML2.xml";
    private File xmlFile, xsdFile;

    private Binder binderInstance;

    private XPath xpath;

    @Before
    public void setUp() {
        try {
            // Vytvorime instanci tovarni tridy
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Pomoci tovarni tridy ziskame instanci DocumentBuilderu
            DocumentBuilder builder = factory.newDocumentBuilder();
            // DocumentBuilder pouzijeme pro zpracovani XSD dokumentu
            // a ziskame model dokumentu ve formatu W3C DOM

            xsdFile = new File(prefix + xsdAddress);
            binderInstance = new Binder(xsdFile);

            //XPath intialisation block 
            xpath = XPathFactory.newInstance().newXPath();
            xsdIS = new InputSource(prefix + xsdAddress);

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Instantiate Parser
     */
    @Test
    public void makeParserTest() {
        Binder testParser;
        //part 1:
        //vytvor parser, pre validnu schemu. Nemoze byt vyhodena vynimka

        File correctSchema = new File(prefix + "schema2.xsd");
        try {
            testParser = new Binder(correctSchema);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            assertFalse(e.getMessage(), true);
        } catch (Exception e) {
            assertFalse("Unknown exception thrown: " + e.toString(), true);
        }

        //part 2:
        //vytvor parser s chybnou schemou. Assertuj vyhodenie vynimky
        File brokenSchema = new File(prefix + "broken.xsd");
        boolean error = true;
        try {
            testParser = new Binder(brokenSchema);
            error = false;
        } catch (Exception e) {
            //OK
        }
        assertTrue(error);
    }

    /**
     * Javadoc for expected verified method: Controls if the node is simpleType
     *
     * @param node : ''element'' node from XSD document
     * @return True if the node is restricted as simple by the XSD Schema,
     * otherwise false
     * @throws javax.xml.xpath.XPathExpressionException if evaluation fails
     */
    @Test
    public void isSimpleTypeTest() throws XPathExpressionException {

        Node simpleTypeNode;
        boolean expected;
        boolean actual;
        String xPath;
        //subtest1: IS NOT simple type.
        xPath = "/descendant::*[local-name()='element'][2]";
        simpleTypeNode = (Node) xpath.evaluate(xPath, xsdIS, XPathConstants.NODE);
        //points to   <xsd:element name="person">

        expected = false;
        try {
            actual = binderInstance.isSimpleType(simpleTypeNode);
        } catch (XPathExpressionException e) {
            actual = true;
            //node is valid though an exception raised
            assertFalse(e.getMessage(), expected);
        }
        //assertion
        assertEquals(actual, expected);

        //subtest2: IS simple type.     
        xPath = "/descendant::*[local-name()='element'][3]";
        simpleTypeNode = (Node) xpath.evaluate(xPath, xsdIS, XPathConstants.NODE);
        //points to                           <xsd:element name="position" type="xsd:string"/>

        expected = true;
        try {
            actual = binderInstance.isSimpleType(simpleTypeNode);
        } catch (XPathExpressionException e) {
            actual = false;
            //node is valid though an exception raised
            assertFalse(e.getMessage(), expected);
        }

        //assertion
        assertEquals(expected, actual);

        //subtest 3: IS simple type - nested.
        xPath = "/descendant::*[local-name()='element'][4]";
        simpleTypeNode = (Node) xpath.evaluate(xPath, xsdIS, XPathConstants.NODE);
        //points to                           <xsd:element name="salary">

        expected = true;
        try {
            actual = binderInstance.isSimpleType(simpleTypeNode);
        } catch (XPathExpressionException e) {
            actual = false;
            //node is valid though an exception raised
            assertFalse(e.getMessage(), expected);
        }

        //assertion
        assertEquals(expected, actual);

    }

    /**
     * Javadoc for expected verified method: Controls if the node is complexType
     *
     * @param node : '''element'' node from XSD document
     * @return True if the node is restricted as complex by the XSD Schema,
     * otherwise false
     * @throws javax.xml.xpath.XPathExpressionException if evaluation fails
     */
    @Test
    public void isComplexTypeTest() throws XPathExpressionException {
        Node complexTypeNode;
        String xPath;
        ///subtest1: IS complex type.
        xPath = "/descendant::*[local-name()='element'][2]";
        complexTypeNode = (Node) xpath.evaluate(xPath, xsdIS, XPathConstants.NODE);
        //points to  <xsd:element name="person">

        boolean expected = true;
        boolean actual;
        try {
            actual = binderInstance.isComplexType(complexTypeNode);
        } catch (XPathExpressionException e) {
            actual = false;
            //node is valid though an exception raised
            assertFalse(e.getMessage(), actual);
        }
        //assertion
        assertEquals(actual, expected);

        //subtest2: IS NOT complex type
        xPath = "/descendant::*[local-name()='element'][3]";
        complexTypeNode = (Node) xpath.evaluate(xPath, xsdIS, XPathConstants.NODE);
        //points to <xsd:element name="position" type="xsd:string"/>

        expected = false;
        try {
            actual = binderInstance.isComplexType(complexTypeNode);
        } catch (XPathExpressionException e) {
            actual = true;
            //node is valid though an exception raised
            assertFalse(e.getMessage(), actual);
        }

        //assertion
        assertEquals(expected, actual);
    }

    /**
     * Javadoc for expected verified method:
     *
     * @param node : the ''element'' of XSD document and
     * @return a list of attributes required for this node by XSDSchema
     * @throws javax.xml.xpath.XPathExpressionException if evaluation fails
     */
    @Test
    public void getAttributesTest() throws XPathExpressionException {
        String xPath = "/descendant::*[local-name()='element'][2]";
        Node relatedNode = (Node) xpath.evaluate(xPath, xsdIS, XPathConstants.NODE);
        //points to:     <xsd:element name="person">

        List<String> expected = new ArrayList<>();

        xPath = "/descendant::*[local-name()='attribute'][1]/@name";
        String value = ((String) xpath.evaluate(xPath, relatedNode, XPathConstants.STRING));
        //points to                         <xsd:attribute name="name" type="xsd:string"/>
        expected.add(value);

        xPath = "/descendant::*[local-name()='attribute'][2]/@name";
        value = ((String) xpath.evaluate(xPath, relatedNode, XPathConstants.STRING));
        //points to                         <xsd:attribute name="ID" type="xsd:integer"/>
        expected.add(value);

        List<String> actual = new ArrayList<>(binderInstance.getAttributes(relatedNode).keySet());
        //size assertion
        assertEquals(expected.size(), actual.size());
        //values assertion
        assertDeepEqualsNotSorted2(expected, actual);
    }

    /**
     * Gets under elements of node
     *
     * @param node
     * @return List of nodes,that are under elements of parameter node
     * @throws javax.xml.xpath.XPathExpressionException if evaluation fails
     */
    @Test
    public void getSubElementsTest() throws XPathExpressionException {

        String xPath = "/descendant::*[local-name()='element'][2]";
        Node relatedNode = (Node) xpath.evaluate(xPath, xsdIS, XPathConstants.NODE);
        //points to:     <xsd:element name="person">

        List<Node> expected = new ArrayList<>();

        xPath = "./descendant::*[local-name()='element'][1]";
        expected.add((Node) xpath.evaluate(xPath, relatedNode, XPathConstants.NODE));
        //points to:                             <xsd:element name="position" type="xsd:string"/>

        xPath = "./descendant::*[local-name()='element'][2]";
        expected.add((Node) xpath.evaluate(xPath, relatedNode, XPathConstants.NODE));
         //points to:                            <xsd:element name="salary">

        List<Node> actual = new ArrayList<>();

        actual = binderInstance.getSubElements(relatedNode);

        //length comparision 
        assertEquals(expected.size(), actual.size());

        assertDeepEqualsNotSorted(expected, actual);
    }

    /**
     * @param node
     * @return type of the content (?)
     * @throws XPathExpressionException if evaluation fails
     */
    @Test
    public void getTypeTest() throws XPathExpressionException {
        Node relatedNode;
        String xPath, expected, actual = "init";
        //subtest 1: type "xsd:string"
        xPath = "/descendant::*[local-name()='element'][3]";
        relatedNode = (Node) xpath.evaluate(xPath, xsdIS, XPathConstants.NODE);
        //points to:                             <xsd:element name="position" type="xsd:string"/>
        expected = "String";
        try {
            actual = binderInstance.getType(relatedNode);
        } catch (XPathExpressionException e) {
            assertFalse("Valid node has thrown exception", true);
        }
        //assertion
        assertEquals(expected, actual);

        //subtest 2: type "xsd:decimal" - nested restriction
        xPath = "/descendant::*[local-name()='element'][4]";
        relatedNode = (Node) xpath.evaluate(xPath, xsdIS, XPathConstants.NODE);
        //points to:                             <xsd:element name="salary">
        expected = "double";
        try {
            actual = binderInstance.getType(relatedNode);
        } catch (XPathExpressionException e) {
            System.out.println("here we are");
        }
        //assertion
        assertEquals(expected, actual);

    }

    private void assertDeepEqualsNotSorted(List<Node> expected, List<Node> actual) {
        //length comparision 
        assertEquals(expected.size(), actual.size());

        for (Node n : expected) {
            boolean found = false;

            for (Node n2 : actual) {
                if (n.equals(n2)) {
                    found = true;
                }
            }
            assertTrue(found);
        }

    }

    private void assertDeepEqualsNotSorted2(List<String> expected, List<String> actual) {
        //length comparision 
        assertEquals(expected.size(), actual.size());

        // attributes value comparision 
        for (String s : expected) {
            boolean valueFound = false;

            //considered expected:
            for (String s2 : actual) {
                if (s2.equals(s)) {
                    valueFound = true;
                }
            }
            assertTrue(valueFound);
        }

    }
}
