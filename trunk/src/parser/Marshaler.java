/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class Marshaler {

    private Document xml;
    private Collection collection;

    private final XPath xpath;
    private String expres;
    private final String prefix = "xsd";

    //1. load XML
    //input XML must be valid for the schema
    public Marshaler(File xmlFile, Collection collection) throws IOException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();

            xml = docBuilder.parse(xmlFile);
            /*
             collection = (Collection) FileManager.selectFromFile(collectionPath);
             */
            this.collection = collection;

        } catch (SAXException | ParserConfigurationException ex) {
            System.out.println("Invalid XML file");
            System.out.println("or collection loading failed");
        }
        // other initialisation
        xpath = XPathFactory.newInstance().newXPath();
    }

    //2. load colection
    public boolean marshal() throws XPathExpressionException {

        //4. initialize data into generated classes
        // means to generate Parser.java
        StringBuilder parser = new StringBuilder();
        String end = System.lineSeparator();
        parser.append("package generated;" + end
                    + "import parser.*;" + end
                    + "public class Parser {" + end
                    + "    private final ParsedTree tree = new ParsedTree();" + end
                    + "" + end
                    + "    public Parser() {" + end
                    + "");
        //collection debug
        Map<Integer, Node> col = collection.getIDmap();
        for (Entry e : col.entrySet()) {
            Node n = (Node) e.getValue();
            //System.out.println(e.getKey() + " - " + n.getAttributes().getNamedItem("name").getNodeValue());

        }

        //end
        for (int elementID = 0; elementID < collection.length(); elementID++) {
            Node n = collection.getNodesById(elementID);
            String name = collection.getNameById(elementID);
            
            if (Binder.isSimpleType(n)) {
                
                expres = "/descendant::*[local-name()='" + collection.getNameById(elementID) + "']";
                NodeList XMLElements = (NodeList) xpath.evaluate(expres, xml, XPathConstants.NODESET);
                
                for (int j = 0; j < XMLElements.getLength(); j++) {
                    String IDout = "\"" + elementID + "-" + j + "\"";
                    String localvar = name + (j + 1);
                    String content = XMLElements.item(j).getTextContent().trim();
                    //checks expected data type of content
                    NamedNodeMap elementAtts = collection.getNodesById(elementID).getAttributes();
                    String expectedType;
                    if (elementAtts.getNamedItem("type") != null) {
                        expectedType = Binder.convertType(elementAtts.getNamedItem("type").getNodeValue());
                    } else {
                        expectedType = "";
                    }
                    if (content == null) {
                        if (expectedType.equals("double")) {
                            content = "null";
                        } else {
                            content = "\"" + content + "\"";
                        }
                    } else {
                        if (expectedType.equals("String")) {
                            content = "\"" + content + "\"";
                        }
                    }

                    parser.append("        Gen")
                                .append(name)
                                .append(" ")
                                .append(localvar)
                                .append(" = new Gen")
                                .append(name)
                                .append("();" + end + "        ")
                                .append(localvar)
                                .append(".setContent(")
                                .append(content)
                                .append(");" + end + "        tree.addNode(new Node().setID(")
                                .append(IDout)
                                .append(")" + end + "                    .setContent(")
                                .append(localvar)
                                .append("));" + end);
                    
                }
            }
            if (Binder.isComplexType(n)) {

                expres = "/descendant::*[local-name()='" + collection.getNameById(elementID) + "']";
                NodeList XMLElements = (NodeList) xpath.evaluate(expres, xml, XPathConstants.NODESET);

                for (int j = 0; j < XMLElements.getLength(); j++) {
                    Node XMLnode = XMLElements.item(j);
                    String ID = "\"" + elementID + "-" + j + "\"";
                    String localvar = name + (j + 1);

                    parser.append("        Gen")
                                .append(name)
                                .append(" ")
                                .append(localvar)
                                .append(" = new Gen")
                                .append(name)
                                .append("();" + end);
                    //attributes declaration
                    NamedNodeMap attributes = XMLnode.getAttributes();
                    for (int k = 0; k < attributes.getLength(); k++) {
                        //gather only attributes which are in accordance with Schema (condition)
                        List<String> schemaAtts = collection.getAllAttributes(elementID);
                        if (schemaAtts.contains(attributes.item(k).getNodeName())) {

                            parser.append("        " + localvar + ".setAttribute(\"")
                                        .append(attributes.item(k).getNodeName())
                                        .append("\", \"")
                                        .append(attributes.item(k).getNodeValue())
                                        .append("\");" + end);
                        }
                    }
                    parser.append("        tree.addNode(new Node()");
                    //subElements declaration
                    List<Integer> subs = collection.getSubElements(elementID);

                    for (Integer subID : subs) {
                        String subName = collection.getNameById(subID);
                        expres = "/descendant::*[local-name()='" + name + "'][" + (j + 1) + "]//" + subName + "";
                        NodeList subNo = (NodeList) xpath.evaluate(expres, xml, XPathConstants.NODESET);
                        //gets number of subElements
                        for (int k = 0; k < subNo.getLength(); k++) {
                            parser.append(".setSubElement(tree.getNode(\"" + subID + "-" + (j + k) + "\"))");
                        }
                    }

                    parser.append("" + end + "                    .setID(")
                                .append(ID)
                                .append(")" + end + "                    .setContent(")
                                .append(localvar)
                                .append("));" + end);

                }
            }
        }

        parser.append("    }" + end
                    + "}");
        try {
            //save generated Parser.java
            FileManager.save("Parser.java", parser.toString());
        } catch (IOException ex) {
            System.out.println("Saving Parser.java failed");
        }
        return true;
    }
}
