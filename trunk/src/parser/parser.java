package parser;

import java.io.File;
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
        if(!(new File("generatedFiles").exists())) {
            if(!(new File("generatedFiles").mkdir())) {
                throw new IOException("File \"generatedFiles\" could not be created");
            }
        }
        
        File xmlParser = new File("generatedFiles/"+name+".java");
        if(!(xmlParser.createNewFile())){
            throw new IOException("Parser \""+ name +"\" could not be created");
        }
        
        
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
        if (node.getNodeName().equalsIgnoreCase(prefix + ":complexType")) {
            return true;
        } else {
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
            //if(isComplexType())
        }

    }
}
