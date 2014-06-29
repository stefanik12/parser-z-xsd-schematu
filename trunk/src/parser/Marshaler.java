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
            System.out.println(e.getKey() + " - " + n.getAttributes().getNamedItem("name").getNodeValue());

        }
        System.out.println(collection.getSubElements(0));

        //end
        //first initialize simpleTypes in Parser
        for (int i = 0; i < collection.length(); i++) {
            Node n = collection.getNodesById(i);
            String name = collection.getNameById(i);

            if (NewBinder.isSimpleType(n)) {

                expres = "/descendant::*[local-name()='" + collection.getNameById(i) + "']";
                NodeList XMLElements = (NodeList) xpath.evaluate(expres, xml, XPathConstants.NODESET);

                for (int j = 0; j < XMLElements.getLength(); j++) {
                    String localvar = name + (j + 1);
                    String content = XMLElements.item(j).getTextContent();
                    System.out.println("Content on "+localvar+" - "+content);
                    //checks expected data type of content
                    NamedNodeMap elementAtts = collection.getNodesById(i).getAttributes();
                    String expectedType;
                    if (elementAtts.getNamedItem("type") != null) {
                        expectedType = NewBinder.convertType(elementAtts.getNamedItem("type").getNodeValue());
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
                        if(expectedType.equals("String")){
                            content = "\""+content+"\"";
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
                                .append(i)
                                .append(")" + end + "                    .setContent(")
                                .append(localvar)
                                .append("));"+end);
                }
            }
            if (NewBinder.isComplexType(n)) {

                expres = "/descendant::*[local-name()='" + collection.getNameById(i) + "']";
                NodeList XMLElements = (NodeList) xpath.evaluate(expres, xml, XPathConstants.NODESET);

                for (int j = 0; j < XMLElements.getLength(); j++) {
                    Node XMLnode = XMLElements.item(j);

                    String localvar = name + (j + 1);
                    String content = XMLElements.item(i).getNodeValue();

                    parser.append("        Gen")
                                .append(name)
                                .append(" ")
                                .append(localvar)
                                .append(" = new Gen")
                                .append(name)
                                .append("();" + end);
                    //attributes declaration
                    //TO DO check the type of the attribute
                    NamedNodeMap attributes = XMLnode.getAttributes();
                    for (int k = 0; k < attributes.getLength(); k++) {
                        parser.append("        " + localvar + ".setAttribute(\"")
                                    .append(attributes.item(k).getNodeName())
                                    .append("\", \"")
                                    .append(attributes.item(k).getNodeValue())
                                    .append("\");" + end);
                    }
                    parser.append("        tree.addNode(new Node()");
                    //subElements declaration
                    List<Integer> subs = collection.getSubElements(i);
                    if (subs == null) {
                        System.out.println("Null subelements element: " + name);
                    }
                    for (Integer subID : subs) {
                        parser.append(".setSubElement(tree.getNode(" + subID + "))");
                    }

                    parser.append("" + end + "                    .setID(")
                                .append(i)
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
            System.out.println("Parser.java generated ");
        } catch (IOException ex) {
            System.out.println("Saving Parser.java failed");
        }
        return true;
    }
    //TO DO:  REFERENCE TREE (IDs) IS STILL SCREWED - RESOLVE! 
}
