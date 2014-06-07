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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import parser.parser;

/**
 *  TO DO: Testy zatial neukazuju na ziadane uzly 
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class ParserTest {

    private Document xsdDoc;
    private final String xsdAddress = "src/parser/testXSD.xsd";
    private Node schemaNode;
    
    private Document xmlDoc;
    private final String xmlAddress = "src/parser/testXML.xml";

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
            xsdDoc = builder.parse(xsdAddress);
            schemaNode = xsdDoc.getLastChild();
            parserInstance = new parser(new File(xsdAddress));

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Instantiate Parser, probably ???
     */
    @Test
    public void makeParserTest() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Javadoc for expected verified method: Controls if the node is simpleType
     *
     * @param node : ''element'' node from XSD document
     * @return True if the node is restricted as simple by the XML Schema,
     * otherwise false
     */
    @Test
    public void isSimpleTypeTest() throws XPathExpressionException {
        
        //test of the test:
        //System.out.println(xsdDoc.getLastChild().getChildNodes().item(1));
        
        
        Node simpleTypeNode;
        //subtest1 gets simpleType node and Assert Eqals       
        //explicit path to the simpleType element in "testXSD.xsd" - not working yet
        simpleTypeNode = schemaNode.getChildNodes().item(0);

        //expected declaration
        NodeList simpleTypeList = simpleTypeNode.getChildNodes();
        boolean expected = false;
        for (int i = 0; i < simpleTypeList.getLength(); i++) {
            Node n = simpleTypeList.item(i);
            if (n.getNodeName().equals("simpleType")) {
                expected = true;
                break;
            }
        }

        boolean actual = parserInstance.isSimpleType(simpleTypeNode);

        //assertion
        if (expected) {
            assertTrue(actual);
        } else {
            assertFalse(actual);
        }
        
        //subtest2 gets some complexType Node and Assert Equals
        //explicit path to the complexType element in "testXSD.xsd" - not working yet
        simpleTypeNode = schemaNode.getChildNodes().item(0).getChildNodes().item(0);
        
        //expected declaration
        simpleTypeList = simpleTypeNode.getChildNodes();
        expected = true;
        for (int i = 0; i < simpleTypeList.getLength(); i++) {
            Node n = simpleTypeList.item(i);
            if (n.getNodeName().equals("complexType")) {
                expected = false;
                break;
            }
        }

        //actual declaration
        actual = parserInstance.isSimpleType(simpleTypeNode);

        //assertion
        if (expected) {
            assertTrue(actual);
        } else {
            assertFalse(actual);
        }
    }

    /**
     * Javadoc for expected verified method: Controls if the node is complexType
     *
     * @param node : '''element'' node from XSD document
     * @return True if the node is restricted as complex by the XML Schema,
     * otherwise false
     */
    @Test
    public void isComplexTypeTest() throws XPathExpressionException {
        Node complexTypeNode;
        //subtest1 gets simpleType node and Assert Eqals       
        //explicit path to the simpleType element in "testXSD.xsd" - not working yet
        complexTypeNode = schemaNode.getChildNodes().item(0).getChildNodes().item(1);

        //expected declaration
        NodeList complexTypeList = complexTypeNode.getChildNodes();
        boolean expected = false;
        for (int i = 0; i < complexTypeList.getLength(); i++) {
            Node n = complexTypeList.item(i);
            if (n.getNodeName().equals("simpleType")) {
                expected = true;
                break;
            }
        }

        boolean actual = parserInstance.isComplexType(complexTypeNode);

        //assertion
        if (expected) {
            assertTrue(actual);
        } else {
            assertFalse(actual);
        }

        //subtest2 gets some complexType Node and Assert Equals
        //explicit path to the complexType element in "testXSD.xsd" - <xsd:element name="company">
        complexTypeNode = schemaNode.getChildNodes().item(0).getChildNodes().item(0);

        //expected declaration
        complexTypeList = complexTypeNode.getChildNodes();
        expected = true;
        for (int i = 0; i < complexTypeList.getLength(); i++) {
            Node n = complexTypeList.item(i);
            if (n.getNodeName().equals("complexType")) {
                expected = false;
                break;
            }
        }

        //actual declaration
        actual = parserInstance.isComplexType(complexTypeNode);

        //assertion
        if (expected) {
            assertTrue(actual);
        } else {
            assertFalse(actual);
        }
    }

    /**
     * Javadoc for expected verified method:
     *
     * @param node : the ''element'' of XSD document and
     * @return a list of attributes required for this node by XMLSchema
     */
    @Test
    public void getAttributesTest() throws XPathExpressionException {
        //explicit path to the element with two parameters
        //points to <xsd:all> line 13 
        NodeList allNodes = schemaNode.getChildNodes().item(0).getChildNodes().item(0).getChildNodes();

        List<String> expected = new ArrayList<>();
        for (int i = 0; i < allNodes.getLength(); i++) {
            Node n = allNodes.item(i);
            if (n.getNodeName().equals("attribute")) {
                expected.add(n.getNodeValue());
            }
        }
        List<String> actual = parserInstance.getAttributes(allNodes.item(0)
                    .getParentNode().getParentNode().getParentNode());

        assertDeepEquals(expected, actual);
    }

    /**
     * Gets under elements of node
     *
     * @param node
     * @return List of nodes,that are under elements of parameter node
     */
    @Test
    public void getUnderElementsTest() {
        NodeList subnodes = xsdDoc.getElementsByTagNameNS("xsd", "element");
        List<Node> expected = new ArrayList<>();
        for (int i = 0; i < subnodes.getLength(); i++) {
            expected.add(subnodes.item(i));
        }
        
        List<Node> actual = parserInstance.getUnderElements(schemaNode.getChildNodes().item(0));
        
        assertDeepEqualsNotSorted(expected, actual);
    }

    private void assertDeepEquals(List<String> expected, List<String> actual) {
        //length comparision 
        assertEquals(expected.size(), actual.size());

        // attributes value comparision 
        for (int i = 0; i < expected.size() && i < actual.size(); i++) {
            //assertEquals(expected.get(i).getNodeName(), actual.get(i).getNodeName());
            assertEquals(expected.get(i), actual.get(i));
        }
        
        
    }
    private void assertDeepEqualsNotSorted(List<Node> expected, List<Node> actual) {
            assertEquals(expected.size(), actual.size());
            
            for(Node n: expected){
                boolean nameFound = false;
                boolean valueFound = false;
                //considered values:
                String name = n.getNodeName();
                String value = n.getNodeValue();
                
                for(Node n2: actual) {
                    if(n2.getNodeName().equals(name)){
                        nameFound = true;
                    }
                    if(n2.getNodeValue().equals(value)){
                        valueFound = true;
                    }
                }
                assertTrue(nameFound);
                assertTrue(valueFound);
            }
    }
}
