/*
Project for PV138 as tought on Faculty of Informatics on Masaryk University in 2014
 */
package source;

import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Node;

/**
 *
 *  @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public interface BinderInterface {

    /**Checks if the node is simple type
     * 
     * @param node
     * @return True if node is simple type
     */
    public boolean isSimpleType(Node node) throws XPathExpressionException;
    
    
    /**Checks if the node is complex
     * 
     * @param node
     * @return True if node is complex type
     */
    boolean isComplexType(Node node)  throws XPathExpressionException;
    
    /**Gets list of attributes of the node
     * 
     * @param node
     * @return Map of attributes of parameter node
     * @throws javax.xml.xpath.XPathExpressionException
     */
    Map<String, String> getAttributes(Node node) throws XPathExpressionException;
    
    /**Gets under elements of node
     * 
     * @param node
     * @return List of nodes,that are under elements of parameter node
     */
    List<Node> getSubElements(Node node) throws XPathExpressionException;
    
    /**Method getType returns type of node as string
     *
     * @param node
     * @return type of input node
     * @throws XPathExpressionException
     */
    String getType(Node node) throws XPathExpressionException;
    /**Method getType returns type of node as string
     *
     * @param type input type in xsd:<T> format in string representation
     * @return input type converted to Java-recognized type format, or Object, if type is not recognized
     * @throws XPathExpressionException
     */
    String getType(String type) throws XPathExpressionException;
    
}
