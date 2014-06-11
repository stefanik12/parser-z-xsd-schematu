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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Marek Burda
 */
public class Parser implements parserInterface {

    Document doc;
    File xsd;
    String prefix;
    String path;

    public Parser(File xsd) {
        this.xsd = xsd;
    }

    @Override
    /****
     *  
     */
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
        if(!(new File("generatedFiles").exists())) {
            if(!(new File("generatedFiles").mkdir())) {
                throw new IOException("File \"generatedFiles\" could not be created");
            }
        }
        
        File xmlParser = new File("generatedFiles/"+name+".java");
        if(!(xmlParser.createNewFile())){
            throw new IOException("Parser \""+ name +"\" could not be created");
        }
        
        BufferedWriter out = new BufferedWriter(new FileWriter(xmlParser));
        path = "generatedFiles";
        
        out.write("package generatedFiles;"
                + "\n"
                + "import java.io.File;\n" 
                + "import java.io.FileWriter;\n"
                + ""
                + ""
                + ""
                + "public class " + name + "Parser throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{\n"
                + "\n"
                + "public parse(File xml){"
                + "DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();\n" 
                + "DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();\n" 
                + "Document doc = dBuilder.parse(fXmlFile);\n"
                + "\n"
                + "XPathFactory xPathFactory = XPathFactory.newInstance();\n" 
                + "XPath xpath = xPathFactory.newXPath();\n" 
                + "XPathExpression expr = xpath.compile(\".\");\n"
                + "\n"
                + "if(!(node.getNodeName().equals("+ name +"))){\n"
                + "throw new IOException(\"Parser used on wrong xml file.\");\n"
                + "\n}\n"
                + "\nNodeList nodes = (NodeList) expr.evaluate(node, XPathConstants.NODESET);\n"
                + "if(nodes.getLength() != 1){"
                + "\nthrow new IOException(\"XML file has zero or more than one root element.\");\n}\n"
                + ""
                + "}"
                + ""
                + "\n");
        
        String resultString = "";
        
        
        
        out.append("\n}");
        
    }

    @Override
    public String createMethod(Node node,String str,String path) throws XPathExpressionException{
        List<String> attributes = getAttributes(node);
        List<Node> underElements = getUnderElements(node);
        
        
            str = str + "\n public " +path+"."+node.getAttributes().getNamedItem("name").getNodeValue()+" instance" 
                    +node.getAttributes().getNamedItem("name").getNodeValue()+"{\n"
                + ""
                + ""
                + "";
        if(!(path.equals("generatedFiles"))){
            
        }
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
        /*if (node.getNodeName().equalsIgnoreCase(prefix + ":complexType")) {
            return true;
        } else {*/
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
    //}

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
    public List<Node> getUnderElements(Node node) {
        List<Node> result = new ArrayList<>();

        if (!node.hasChildNodes()) {
            return result;
        }
        for (int i = 0; node.getChildNodes().getLength() < i; i++) {
            result.add(node.getChildNodes().item(i));
        }
        return result;
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
                return nodes.item(0).getAttributes().getNamedItem("base").getNodeValue();
            }
          return null;
        }
    }
}
