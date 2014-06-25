package parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Marek Burda
 */
public class parser implements parserInterface {

    Document doc;
    File xsd;
    String prefix;
    String path;

    public parser(File xsd) {
        this.xsd = xsd;
    }

    @Override
    public void makeParser() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        documentFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        doc = documentBuilder.parse(xsd);

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression expr = xpath.compile(".");

        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        if (!(nodes.getLength() == 1)) {
            throw new IOException("Incorrect input");
        }

        prefix = nodes.item(0).getPrefix();

        String name = nodes.item(0).getAttributes().getNamedItem("name").getNodeValue() + "parser";
        if (!(new File("generatedFiles").exists())) {
            if (!(new File("generatedFiles").mkdir())) {
                throw new IOException("File \"generatedFiles\" could not be created");
            }
        }

        File xmlParser = new File("generatedFiles/" + name + ".java");
        if (!(xmlParser.createNewFile())) {
            throw new IOException("Parser \"" + name + "\" could not be created");
        }

        BufferedWriter out = new BufferedWriter(new FileWriter(xmlParser));
        path = "generatedFiles";

        out.write("package generatedFiles;"
                + "\n"
                + "import java.io.*;\n"
                + "import javax.xml.parsers.*;\n"
                + "import javax.xml.xpath.*;\n"
                + "import org.w3c.dom.*;\n"
                + "import org.xml.sax.*;"
                + "\n"
                + "public class " + name + "Parser throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{\n"
                + "\n"
                + "public " + name + "Parser(File xml){\n"
                + "DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();\n"
                + "DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();\n"
                + "Document doc = dBuilder.parse(xml);\n"
                + "\n"
                + "XPathFactory xPathFactory = XPathFactory.newInstance();\n"
                + "XPath xpath = xPathFactory.newXPath();\n"
                + "XPathExpression expr = xpath.compile(\".\");\n"
                + "\n"
                + "NodeList nodes = (NodeList) expr.evaluate(node, XPathConstants.NODESET);"
                + "\n"
                + "if(!(nodes.get(0).getNodeName().equals(\"" + name + "\"))){\n"
                + "throw new IOException(\"Parser used on wrong xml file.\");\n"
                + "\n}\n"
                + "if(nodes.getLength() != 1){"
                + "\nthrow new IOException(\"XML file has zero or more than one root elements.\");\n}\n"
                + "Node node = nodes.get(i);\n"
                + "parseFile(node);\n"
                + "}\n"
                + "\n"
               /* + "private void createMethod(Node node){\n"
                + "\n"
                + "}\n\n"*/
                + "public void parseFile(Node node){\n"
                + getType(nodes.item(0)) +" "+getType(nodes.item(0))+"instance = new "+getType(nodes.item(0)) + "();\n");

        out.append(createMethod(nodes.item(0),"", path));

        out.append("\n}\n}");

    }
    
    char sign = 'a'-1;
    
    @Override
    public String createMethod(Node node, String str, String path) throws XPathExpressionException {
        List<String> attributes = getAttributes(node);
        List<Node> underElements = getSubElements(node);

        //String orPath = path;
        
        String nodeName = node.getAttributes().getNamedItem("name").getNodeValue();
        if(getType(node).equals("String")){
        str = str + "String "+nodeName+"+inst;\n"
                + nodeName+"inst.set"+nodeName+"(node.getValue());\n";
        }
        if(getType(node).equals("double")){
            str = str + "double "+nodeName+"inst;\n"
                    + nodeName+"inst.set"+nodeName+"(Double.parseDouble(node.getValue()));\n";
        }
        if(!(getType(node).equals("String")) && !(getType(node).equals("double"))){
            str = str + "generatedFiles." +getType(node)+" "+getType(node)+"inst = new generatedFiles."+getType(node)+"();\n";
            sign++;
            str = str + "for(int "+sign+"=0;"+sign+"<node.getChildeNodes().getLength();"+sign+"++){\n";
                    
            for(int i=0;i<underElements.size();i++){
                str = str+ createMethod(underElements.get(i), str, path);
            }
            str += "\n}\n";
        }
        for (int i = 0; i < attributes.size(); i++) {
            str = str + "\n" + nodeName + "instance.set" + attributes.get(i) + "Attr(" + node.getAttributes().getNamedItem(attributes.get(i)).getNodeValue() + "); \n";
            //}   
        }
        /*for (int i = 0; i < underElements.size(); i++) {
            path = orPath;
            if (isSimpleType(underElements.get(i))) {
                str = str + "\n " + nodeName + "instance.set" + underElements.get(i).getAttributes().getNamedItem("name").getNodeValue() + "(" + underElements.get(i).getNodeValue() + ");\n";
            }
            if (isComplexType(underElements.get(i))) {
                path = path + "." + nodeName;
                str = str + " " + getType(underElements.get(i)) +" "+getType(underElements.get(i))+"instance = new "+getType(underElements.get(i))+"();\n";
                createMethod(underElements.get(i), str, path);
                
            }

        }*/
        str = str + "\n";
        return str;
    }

    @Override
    public boolean isSimpleType(Node node) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression expr = xpath.compile("*");

        NodeList result = (NodeList) expr.evaluate(node, XPathConstants.NODESET);

        if ((result.getLength() == 0) || (result.item(0).getNodeName().equalsIgnoreCase(prefix + ":simpleType"))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isComplexType(Node node) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression expr = xpath.compile("*");

        NodeList result = (NodeList) expr.evaluate(node, XPathConstants.NODESET);

        for (int i = 0; i < result.getLength(); i++) {
            if (result.item(i).getNodeName().equalsIgnoreCase(prefix + ":complexType")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getAttributes(Node node) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression expr = xpath.compile("*");

        NodeList nodes = (NodeList) expr.evaluate(node, XPathConstants.NODESET);

        List<String> result = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeName().equalsIgnoreCase(prefix + ":attribute")) {
                for (int j = 0; j < nodes.item(i).getAttributes().getLength(); j++) {
                    if (nodes.item(i).getAttributes().item(j).getNodeName().equalsIgnoreCase("name")) {
                        result.add(nodes.item(i).getAttributes().item(j).getNodeValue());
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<Node> getSubElements(Node node) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression expr = xpath.compile("*");
        Object result = expr.evaluate(node, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;

        Node node2 = nodes.item(0);

        if (node.getNodeName().equals(prefix + ":element")) {
            expr = xpath.compile("*");
            result = expr.evaluate(node2, XPathConstants.NODESET);
            nodes = (NodeList) result;

            node2 = nodes.item(0);
        }

        expr = xpath.compile("child::*");
        result = expr.evaluate(node2, XPathConstants.NODESET);
        nodes = (NodeList) result;

        List<Node> underElementsNodes = new ArrayList<Node>();

        for(int i=0;i<nodes.getLength();i++){
                for(int j=0;j<nodes.item(i).getChildNodes().getLength();j++){
                    if((nodes.item(i).getChildNodes().item(j).getNodeName().equals(prefix + ":all")) ||
                            (nodes.item(i).getChildNodes().item(j).getNodeName().equals(prefix + ":sequence")) ||
                            (nodes.item(i).getChildNodes().item(j).getNodeName().equals(prefix + ":choice"))){
                        for(int k=0;k<nodes.item(i).getChildNodes().item(j).getChildNodes().getLength();k++){
                            if(nodes.item(i).getChildNodes().item(j).getChildNodes().item(k).getNodeName().equals(prefix + ":element")){
                                underElementsNodes.add(nodes.item(i).getChildNodes().item(j).getChildNodes().item(k));
                            }
                        }
                    }
                }
        }
        
        if (nodes.getLength() == 0) {
            return underElementsNodes;
        } else {
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getNodeName().equals(prefix + ":element")) {
                    underElementsNodes.add(nodes.item(i));
                }
            }
            return underElementsNodes;

        }
    }

    @Override
    public String getType(Node node) throws XPathExpressionException {
        if (node.getAttributes().getNamedItem("type") != null) {
            return node.getAttributes().getNamedItem("type").getNodeValue();
        } else {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();
            XPathExpression expr = xpath.compile("*");

            NodeList nodes = (NodeList) expr.evaluate(node, XPathConstants.NODESET);

            if (isSimpleType(nodes.item(0))) {
                if(nodes.item(0).getAttributes().getNamedItem("base").getNodeValue().equals(prefix+":string")){
                    return "String";
                }
                String type = nodes.item(0).getAttributes().getNamedItem("base").getNodeValue();
                 if (type.equals(prefix + ":byte") || type.equals(prefix + ":decimal")
                || type.equals(prefix + ":int")
                || type.equals(prefix + ":nonNegativeInteger")
                || type.equals(prefix + ":nonPositiveInteger")
                || type.equals(prefix + ":long")
                || type.equals(prefix + ":negativeInteger")
                || type.equals(prefix + ":short")
                || type.equals(prefix + ":unsignedLong")
                || type.equals(prefix + ":unsignedInt")
                || type.equals(prefix + ":unsignedShort")
                || type.equals(prefix + ":unsignedByte")) {
            return "double";
        } 
            }
            return null;
        }
    }
}
