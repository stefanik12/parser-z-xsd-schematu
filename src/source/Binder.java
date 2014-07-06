/*
Project for PV138 as tought on Faculty of Informatics on Masaryk University in 2014
 */
package source;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class Binder {

    private Document schema;
    private static XPath xpath;
    private static String expres;
    private static final String prefix = "xsd";
    private Collection collection;
    
    /**
     * @param schemaFile initiate Binder with this file
     * @throws ParserConfigurationException on invalid configuration
     * @throws SAXException indicates invalid Schema on input
     * @throws IOException if Schema fails to load
     */
    public Binder(File schemaFile) throws ParserConfigurationException, SAXException, IOException {
        // validate schema as valid XML document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();

        schema = docBuilder.parse(schemaFile);

        //other initialisations
        xpath = XPathFactory.newInstance().newXPath();
        collection = new Collection();
    }
    
    /**
     * @param outputDir directory where the generated files are attempted to be saved
     * @return Collection with given information about Content tree and generated objects
     * @throws XPathExpressionException if XML Schema of this binder is invalid 
     */
    public Collection bind(File outputDir) throws XPathExpressionException {

        // get all xsd:element elements 
        expres = "/descendant::*[local-name()='element']";
        NodeList elements = (NodeList) xpath.evaluate(expres, schema, XPathConstants.NODESET);

        // generate content tree
        //for each element declare its childs  as subNodes
        for (int i = 0; i < elements.getLength(); i++) {
            Node element = elements.item(i);
            String name = element.getAttributes().getNamedItem("name").getNodeValue();
            int ID = collection.add(name, element);
        }

        StringBuilder classContent;

        for (int i = 0; i < elements.getLength(); i++) {

            Node element = elements.item(i);
            int ID = i;

            String name = element.getAttributes().getNamedItem("name").getNodeValue();
            String type = getType(element);
            String cname = "Gen" + name;

            // for each element generate Class content
            classContent = new StringBuilder();
            String end = System.lineSeparator();
            if (isSimpleType(element)) {
                collection.set(ID, name, element, null, null);
                //adds all elements into Collection and assign their IDs

                //SimpleType template
                classContent.append("package generated;")
                            .append(end)
                            .append("import generatedInterface.*;")
                            .append(end)
                            .append("public class ")
                            .append(cname)
                            .append(" implements SimpleType<" + type + "> {")
                            .append(end)
                            .append("    private ")
                            .append(type)
                            .append(" content")
                            .append(";")
                            .append(end)
                            .append("    @Override")
                            .append(end)
                            .append("    public ")
                            .append(type)
                            .append(" getContent() {")
                            .append(end)
                            .append("        return content;")
                            .append(end)
                            .append("    }")
                            .append(end)
                            .append("    @Override")
                            .append(end)
                            .append("    public " + cname + " setContent(")
                            .append(type)
                            .append(" content) {")
                            .append(end)
                            .append("        this.content = content;")
                            .append(end)
                            .append("        return this;")
                            .append(end)
                            .append("    }")
                            .append(end)
                            .append("}");
                String filename = cname + ".java";

                try {
                    FileManager.save(filename, classContent.toString(), outputDir);
                } catch (IOException ex) {
                    System.out.println("ERROR generating " + filename);
                    return null;
                }

            }
            if (isComplexType(element)) {

                List<Integer> subElements = new ArrayList<>();
                for (Node n : getSubElements(element)) {
                    String subName = n.getAttributes().getNamedItem("name").getNodeValue();
                    int id = collection.getIdByName(subName);
                    subElements.add(id);
                }
                // subElement IDs are assigned for every element from elements

                List<String> attributes = new ArrayList<>(getAttributes(element).keySet());
                collection.set(ID, name, element, subElements, attributes);

                // ComplexType template
                classContent.append("package generated;")
                            .append(end)
                            .append("import generatedInterface.*;")
                            .append(end)
                            .append("import java.util.HashMap; ")
                            .append(end)
                            .append("import java.util.Map;")
                            .append(end)
                            .append(end)
                            .append("public class " + cname + " implements ComplexType<" + type + "> {")
                            .append(end);
                if (type.equals("")) {
                    classContent.append("private Object content;");
                } else {
                    classContent.append("private " + type + " content;");
                }
                classContent.append(end)
                            .append("private final Map<String, Object> attributes = new HashMap<>();")
                            .append(end)
                            .append("public " + cname + "(){")
                            .append(end);
                classContent.append(end);
                for (Map.Entry<String, String> a : getAttributes(element).entrySet()) {
                    //attribute set in format <Attribute name, Attribute type>

                    classContent.append("        setAttribute(\"" + a.getKey() + "\", \"" + a.getValue() + "\");" + end);
                }
                classContent.append("    }")
                            .append(end)
                            .append("    @Override")
                            .append(end)
                            .append("    public " + cname + " setAttribute(String attribute, Object value) {")
                            .append(end)
                            .append("        attributes.put(attribute, value);")
                            .append(end)
                            .append("        return this;")
                            .append(end)
                            .append("    }")
                            .append(end)
                            .append("    @Override")
                            .append(end)
                            .append("    public " + cname + " setContent(Object content) {")
                            .append(end)
                            .append("        this.content = content;")
                            .append(end)
                            .append("        return this;")
                            .append(end)
                            .append("    }")
                            .append(end)
                            .append("    @Override")
                            .append(end);
                if (type.equals("")) {
                    classContent.append("    public Object getContent() {");
                } else {
                    classContent.append("    public " + type + " getContent() {");
                }

                classContent.append(end)
                            .append("        return content;")
                            .append(end)
                            .append("    }")
                            .append(end)
                            .append("    @Override")
                            .append(end)
                            .append("    public Map<String, Object> getAttributes() {")
                            .append(end)
                            .append("        return attributes;")
                            .append(end)
                            .append("    }")
                            .append(end)
                            .append("}");

                String filename = "Gen" + name + ".java";
                try {
                    FileManager.save(filename, classContent.toString(), outputDir);
                } catch (IOException ex) {
                    System.out.println("Class " + filename + " save operation failed");
                    return null;
                }
            }
        }

        return collection;
    }

    /**
     * @param node : ''element'' node from XSD document
     * @return True if the node is recognized as simple, otherwise false
     * @throws javax.xml.xpath.XPathExpressionException if evaluation fails
     */
    public static boolean isSimpleType(Node node) throws XPathExpressionException {
        expres = "./*";
        NodeList result = (NodeList) xpath.evaluate(expres, node, XPathConstants.NODESET);

        if ((result.getLength() == 0) || (result.item(0).getLocalName().equalsIgnoreCase("simpleType"))) {
            return true;
        }
        return false;
    }

    /**
     * @param node : ''element'' node from Schema
     * @return True if the node is recognized as complex, otherwise false
     * @throws javax.xml.xpath.XPathExpressionException if evaluation fails
     */
    public static boolean isComplexType(Node node) throws XPathExpressionException {
        expres = "./*";
        NodeList result = (NodeList) xpath.evaluate(expres, node, XPathConstants.NODESET);

        if (result.item(0) != null) {
            if (result.item(0).getLocalName().equalsIgnoreCase("complexType")) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param node element node from Schema
     * @return Map <attribute name as key, attribute type as value>
     * @throws XPathExpressionException if evaluation on specified node fails
     */
    public static Map<String, String> getAttributes(Node node) throws XPathExpressionException {
        expres = "./*/*[local-name()='attribute']";
        NodeList attributes = (NodeList) xpath.evaluate(expres, node, XPathConstants.NODESET);
        Map<String, String> out = new HashMap<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node atbute = attributes.item(i);
            String type;
            if (atbute.getAttributes().getNamedItem("type") != null) {
                type = atbute.getAttributes().getNamedItem("type").getNodeValue();
            } else {
                type = "null";
            }
            type = convertType(type);
            out.put(atbute.getAttributes().getNamedItem("name").getNodeValue(), type);
        }

        return out;
    }

    /**
     * @param node element node from Schema
     * @return "element" nodes in Schema ONE level deeper
     * @throws XPathExpressionException if evaluation on specified node fails
     */
    public List<Node> getSubElements(Node node) throws XPathExpressionException {
        expres = "./*";
        NodeList subElements = null;
        while (subElements == null) {
            expres = expres + "/*";
            String expres2 = expres + "/*[local-name()='element']";
            subElements = (NodeList) xpath.evaluate(expres2, node, XPathConstants.NODESET);

        }
        List<Node> out = new ArrayList<>();
        for (int i = 0; i < subElements.getLength(); i++) {
            out.add(subElements.item(i));
        }
        return out;
    }

    /**
     * @param node "element" node from Schema
     * @return type of node content - In java-recognizable format
     * @throws XPathExpressionException if evaluation on specified node fails
     */
    public static String getType(Node node) throws XPathExpressionException {
        String foundType;

        expres = "./*/*[local-name()='restriction']";
        Node restrictionNode = (Node) xpath.evaluate(expres, node, XPathConstants.NODE);
        expres = ".";

        if (node.getAttributes().getNamedItem("type") != null) {
            foundType = node.getAttributes().getNamedItem("type").getNodeValue();

        } else if (isSimpleType(node) && restrictionNode != null) {
            foundType = restrictionNode.getAttributes().getNamedItem("base").getNodeValue();

        } else {
            foundType = "null";
        }

        //output assignment
        return convertType(foundType);
    }

    /**
     * @param xsdType String representation of type: In XML Schema format
     * @return String representation of type in Java recognizable format
     */
    public static String convertType(String xsdType) {
        switch (xsdType) {
            case prefix + ":byte":
            case prefix + ":decimal":
            case prefix + ":int":
            case prefix + ":nonNegativeInteger":
            case prefix + ":nonPositiveInteger":
            case prefix + ":long":
            case prefix + ":negativeInteger":
            case prefix + ":short":
            case prefix + ":unsignedLong":
            case prefix + ":unsignedInt":
            case prefix + ":unsignedShort":
            case prefix + ":unsignedByte":
            case prefix + ":double":
                return "Double";
            case prefix + ":string":
                return "String";
            case "null":
                return "Object";
            default:
                return "";
        }

    }
}
