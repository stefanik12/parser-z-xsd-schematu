package parser;

import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Marek Burda
 */
public interface parserInterface {
    
    /**
     *makeParser will create parser for xsd schema
     */
    void makeParser() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException;
    
    /**Checks if the node is simple type
     * 
     * @param node
     * @return True if node is simple type
     */
    boolean isSimpleType(Node node) throws XPathExpressionException;
    
    
    /**Checks if the node is complex
     * 
     * @param node
     * @return True if node is complex type
     */
    boolean isComplexType(Node node)  throws XPathExpressionException;
    
    /**Gets list of attributes of the node
     * 
     * @param node
     * @return List of attributes of parameter node
     */
    List<String> getAttributes(Node node) throws XPathExpressionException;
    
    /**Gets under elements of node
     * 
     * @param node
     * @return List of nodes,that are under elements of parameter node
     */
    List<Node> getUnderElements(Node node);
    
    /**Method getType returns type of node as string
     *
     * @param node
     * @return type of input node
     * @throws XPathExpressionException
     */
    String getType(Node node) throws XPathExpressionException;
    
    /**
     Method will append new method for parsing input node to the string,that will
     * later 
     * @param node Node for which will method be created
     * @param str String to which will created method append
     * @return String with appended method
     */
    String createMethod(Node node,String str,String path) throws XPathExpressionException;
}
