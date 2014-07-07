/*
Project for PV138 as tought on Faculty of Informatics on Masaryk University in 2014
 */
package source;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class Marshaler {

    private Document xml;
    private Collection collection;

    private static final XPath xpath = XPathFactory.newInstance().newXPath();
    private String expres;
    private final String prefix = "xsd";

    /**
     *  Initiates Marshaler with given XML
     *  XML Must be valid for the Schema used for binding - Marshaler does not verify it and throws an Exception at undefined point 
     * @param xmlFile File representation of a path to related XML
     * @param collection binded data used for marshaling in Collection data structure
     * @throws IOException if input file does not exists or is not accessible
     */
    public Marshaler(File xmlFile, Collection collection) throws IOException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();

            xml = docBuilder.parse(xmlFile);
            
            this.collection = collection;

        } catch (SAXException | ParserConfigurationException ex) {
            System.out.println("Invalid XML file");
            System.out.println("or collection loading failed");
        }
    }
 /**
  * @param outputDir direction where generated Parser.java is attempted to be saved
  * @return true on success, otherwise false
  * @throws XPathExpressionException most likely if XML is not valid for binded Schema
  *  Throwing XPathExpresionException does not implicates that Schema does not correspond to the XML
  */
    public boolean marshal(File outputDir) throws XPathExpressionException {

        // StringBuilder parser: to generate Parser.java
        StringBuilder parser = new StringBuilder();
        String end = System.lineSeparator();
        parser.append("package generated;" + end
                    + "public class Parser implements ParserInterface {" + end
                    + "    private final ParsedTree tree = new ParsedTree();" + end
                    + end
                    + "    public Parser() {" + end
                    + "");
        
        // Ordering elements into the order so the generated IDs always refer to EXISTING elements 
        // (using DFS Post-order algorithm: see below)
        List<Integer> ordered = order();
        
        for (int elementID : ordered) {
            //browses XML for all elements processed in Schema - in valid-reference order 
            
            Node n = collection.getNodesById(elementID);
            String name = collection.getNameById(elementID);

            if (Binder.isSimpleType(n)) {
                expres = "./descendant::*[local-name()='" + name + "']";
                NodeList XMLElements = (NodeList) xpath.evaluate(expres, xml, XPathConstants.NODESET);

                for (int j = 0; j < XMLElements.getLength(); j++) {
                    //browses all occurences of <name> element in XML
                    
                    String IDout = "\"" + elementID + "-" + j + "\"";
                    String localvar = name + (j + 1);

                    String content = XMLElements.item(j).getTextContent().trim();
                    //checks expected data type of content
                    String expectedType = Binder.getType(n);
                    if (content == null) {
                        content = "null";
                    }
                    if (expectedType.equals("Double")) {
                        content = "Double.parseDouble(\"" + content + "\")";
                    } else {
                        content = "\"" + content + "\"";
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
                expres = "/descendant::*[local-name()='" + name + "']";
                NodeList XMLElements = (NodeList) xpath.evaluate(expres, xml, XPathConstants.NODESET);

                for (int j = 0; j < XMLElements.getLength(); j++) {
                    //browses all occurences of <name> element in XML
                    
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
                        //process only attributes which are in accordance with Schema (if-condition)
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
                    
                    // subElements ID assertion
                    List<Integer> subs = collection.getSubElements(elementID);
                    for (Integer subID : subs) {
                        String subName = collection.getNameById(subID);
                        expres = "/descendant::*[local-name()='" + name + "'][" + (j + 1) + "]//" + subName + "";
                        NodeList subNo = (NodeList) xpath.evaluate(expres, xml, XPathConstants.NODESET);
                        // gets number of occurences for every subElement of this node, as binded from Schema
                        
                        //outputs references to every occurence of subElement underneath this node
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
            FileManager.save("Parser.java", parser.toString(), outputDir);
            //attach ParserInterface, Node, ParsedTree to outputDir 
            
            
            new FileManager().copy("dependencies/ParserInterface.java", outputDir.getAbsolutePath());
            new FileManager().copy("dependencies/Node.java", outputDir.getAbsolutePath());
            new FileManager().copy("dependencies/ParsedTree.java", outputDir.getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Saving Parser.java failed");
        }
        return true;
    }

    // Shared resources for elements ordering
    private List<Integer> visited = new ArrayList<>();
    private List<Integer> visited2 = new ArrayList<>();
    
    /**
     *  Triggering function for postOrderDFS()
     * @return list of elements specifically ordered
     */
    private List<Integer> order() {
        Integer root = collection.getRoot();
        
        postOrderDFS(root);
        visited2.add(root);
        return visited2;
    }
    /**
     * Recursive Post-order DepthFirst Search over general tree function
     *  Uses shared data types to gather browsed nodes
     * @param node start node
     */
    private void postOrderDFS(Integer node) {
        visited.add(node);
        boolean subPath = false;
        for (Integer subID : collection.getSubElements(node)) {
            if (!visited.contains(subID)) {
                subPath = true;
                postOrderDFS(subID);
            }
        }
        if (!subPath && collection.getParent(node) != -1) {
            visited2.add(node);
            postOrderDFS(collection.getParent(node));
        }
    }
}
