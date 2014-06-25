/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class NewBinder implements parserInterface {

    private Document schema;
    private XPath xpath;
    private String expres;
    private final String prefix = "xsd";
    private Collection collection;
    private String schemaName;

    public NewBinder(File schemaFile) throws ParserConfigurationException, SAXException, IOException {
        //step0: validate schema as valid XML document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();

        schema = docBuilder.parse(schemaFile);

        //other initialisations
        xpath = XPathFactory.newInstance().newXPath();
        collection = new Collection();
        schemaName = schemaFile.getName().replace(".xsd", "");
    }

    public boolean bind() throws XPathExpressionException {
        //step 1 get all xsd:element elements 

        expres = "/descendant::*[local-name()='element']";
        NodeList elements = (NodeList) xpath.evaluate(expres, schema, XPathConstants.NODESET);

        //step 2: generate content tree
        //for each element declare its childs  as subNodes
        for (int i = 0; i < elements.getLength(); i++) {
            Node element = elements.item(i);
            String name = element.getAttributes().getNamedItem("name").getNodeValue();
            int ID = collection.add(name, element);

            //invariant: adds all elements into Collection and assign their IDs
        }

        StringBuilder classContent;

        List<Integer> subElements;
        Map<String, String> attributes;
        for (int i = 0; i < elements.getLength(); i++) {

            Node element = elements.item(i);

            //2. get subElement ID
            //3. get content 
            //4. get attributes
            String name = element.getAttributes().getNamedItem("name").getNodeValue();
            String type = getType(element);

            // step 2: for each element generate Class content
            classContent = new StringBuilder();
            String end = System.lineSeparator();
            if (isSimpleType(element)) {
                //SimpleType template
                classContent.append("package generated;")
                            .append(end)
                            .append("public class Gen")
                            .append(name)
                            .append(" {")
                            .append(end)
                            .append("private ")
                            .append(type)
                            .append(" content")
                            .append(";")
                            .append(end)
                            .append("public final boolean complex = false;")
                            .append(end)
                            .append("public ")
                            .append(type)
                            .append(" getContent() {")
                            .append(end)
                            .append("" + "return content;")
                            .append(end)
                            .append("}")
                            .append(end)
                            .append("" + "public void setContent(")
                            .append(type)
                            .append(" content) {")
                            .append(end)
                            .append("this.content = content;")
                            .append(end)
                            .append("}")
                            .append(end)
                            .append("}");
                String filename = "Gen" + name + ".java";

                try {
                    System.out.println(classContent.toString());
                    save(filename, classContent.toString());
                } catch (IOException ex) {
                    System.out.println("Class " + filename + " save operation failed");
                    return false;
                }

            }
            if (isComplexType(element)) {
                //subElements declaration
                subElements = new ArrayList<>();
                System.out.println("Element: " + element.getAttributes().getNamedItem("name").getNodeValue());
                for (Node n : getSubElements(element)) {
                    String subName = n.getAttributes().getNamedItem("name").getNodeValue();
                    System.out.println("Sub: " + subName);
                    int id = collection.getIdByName(subName);
                    subElements.add(id);
                }
                //Invariant: subElement IDs are assigned for every element from elements

                String subElementStr = subElements.toString().substring(1, subElements.toString().length() - 1);
                //posledna ciarka musi prec 
                System.out.println(subElementStr);

                //definovat parenta
                String parent = "1";

                // ComplexType template
                classContent.append("package generated;")
                            .append(end)
                            .append("import java.util.ArrayList;")
                            .append(end)
                            .append("import java.util.Arrays;")
                            .append(end)
                            .append("import java.util.HashMap; ")
                            .append(end)
                            .append("import java.util.List;")
                            .append(end)
                            .append("import java.util.Map;")
                            .append(end)
                            .append("public class Gen" + name + " {")
                            .append(end)
                            .append("private final List<Integer> subElements = new ArrayList<>(Arrays.asList(" + subElementStr + "));")
                            .append(end)
                            .append("private final int parent = " + parent + ";")
                            .append(end)
                            .append("public final boolean complex = true;")
                            .append(end);
                if (type.equals("")) {
                    classContent.append("private Object content = null;");
                } else {
                    classContent.append("private " + type + "content;");
                }
                classContent.append(end)
                            .append("private final Map<String, Object> attributes = new HashMap<>();")
                            .append(end).append("public Gen" + name + "(){" + end)
                            .append(end);

                for (Map.Entry<String, String> a : getAttributes(element).entrySet()) {
                    //attribute set in format <String Attribute name, String Attribute type>

                    classContent.append("        setAttribute(\"" + a.getKey() + "\", \"" + a.getValue() + "\");" + end);
                }
                classContent.append("    }")
                            //.append("@Override")
                            .append(end)
                            .append("    public List<Integer> getSubElements() {")
                            .append(end)
                            .append("        return subElements;")
                            .append(end)
                            .append("    }")
                            .append(end)
                            .append("    public void setAttribute(String attribute, Object value) {")
                            .append(end)
                            .append("        attributes.put(attribute, value);")
                            .append(end)
                            .append("    }")
                            .append(end)
                            //.append("    @Override")
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
                            //.append("    @Override")
                            .append(end)
                            .append("    public Map<String, Object> getAttributes() {")
                            .append(end)
                            .append("        return attributes;")
                            .append(end)
                            .append("    }")
                            .append(end)
                            //.append("    @Override")
                            .append(end)
                            .append("    public int getParent() {")
                            .append(end)
                            .append("        return parent;")
                            .append(end)
                            .append("    }" + "}");

                String filename = "Gen" + name + ".java";
                try {
                    System.out.println(classContent.toString());
                    save(filename, classContent.toString());
                } catch (IOException ex) {
                    System.out.println("Class " + filename + " save operation failed");
                    return false;
                }
            }
        }

        //ATTACH OTHER FILES INTO THE PACKAGE 
        //interfaces
        //Collection
        try {
            CollectionManager.insertIntoFile(collection, "src/generated/"+schemaName + "Col" + ".bin");
            System.out.println("Collection " + schemaName + " saved");
        } catch (IOException e) {
            System.out.println("Collection " + schemaName + " save failed");
            return false;
        }
        return true;
    }

    /*
     **USED METHODS** 
     */
    @Override
    public boolean isSimpleType(Node node) throws XPathExpressionException {
        expres = "./*";
        NodeList result = (NodeList) xpath.evaluate(expres, node, XPathConstants.NODESET);

        if ((result.getLength() == 0) || (result.item(0).getLocalName().equalsIgnoreCase("simpleType"))) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isComplexType(Node node) throws XPathExpressionException {
        String expres = "./*";
        NodeList result = (NodeList) xpath.evaluate(expres, node, XPathConstants.NODESET);

        if (result.item(0) != null) {
            if (result.item(0).getLocalName().equalsIgnoreCase("complexType")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, String> getAttributes(Node node) throws XPathExpressionException {
        expres = "./*/*[local-name()='attribute']";
        NodeList attributes = (NodeList) xpath.evaluate(expres, node, XPathConstants.NODESET);
        Map<String, String> out = new HashMap<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node atbute = attributes.item(i);
            String type;
            try {
                type = atbute.getAttributes().getNamedItem("type").getNodeValue();
            } catch (NullPointerException e) {
                type = "null";
            }
            type = convertType(type);
            out.put(atbute.getAttributes().getNamedItem("name").getNodeValue(), type);
            System.out.println(atbute.getAttributes().getNamedItem("name").getNodeValue());
        }

        return out;
    }

    @Override
    public List<Node> getSubElements(Node node) throws XPathExpressionException {
        expres = "./*/descendant::*[local-name()='element']";
        NodeList subElements = (NodeList) xpath.evaluate(expres, node, XPathConstants.NODESET);
        List<Node> out = new ArrayList<>();
        for (int i = 0; i < subElements.getLength(); i++) {
            out.add(subElements.item(i));
        }
        return out;
    }

    @Override
    public String getType(Node node) throws XPathExpressionException {
        String foundType = null;

        expres = "./*/*[local-name()='restriction']";
        Node restrictionNode = (Node) xpath.evaluate(expres, node, XPathConstants.NODE);
        expres = ".";
        Node type = (Node) xpath.evaluate(expres, node, XPathConstants.NODE);

        if (node.getAttributes().getNamedItem("type") != null) {
            foundType = node.getAttributes().getNamedItem("type").getNodeValue();

        } else if (isSimpleType(node) && restrictionNode != null) {
            foundType = restrictionNode.getAttributes().getNamedItem("base").getNodeValue();

        } else {
            return "";
        }

        //output assignment
        return convertType(foundType);
    }

    public String convertType(String xsdType) {
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
                return "double";
            case prefix + ":string":
                return "String";
            default:
                return "";
        }

    }

    private void save(String filename, String classContent) throws IOException {
        boolean success = false;
        String path = "src/generated";
        File folder = new File(path);
        folder.mkdir();
        File file = new File(path + "/" +filename);

        file.createNewFile();
        try (OutputStream os = new FileOutputStream(file)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(classContent);
            bw.flush();

            success = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewBinder.class.getName()).log(Level.SEVERE, null, ex);

        }
        if (success) {
            System.out.println(filename + " successfully generated");
        }

    }

}
