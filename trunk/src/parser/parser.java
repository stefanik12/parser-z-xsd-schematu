
package parser;

import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author 
 */
public class parser implements parserInterface{
    
    public parser(String filename) {
        //filename is expected to be XSD document - XML Schema
    }
    
    @Override
    public void makeParser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isSimpleType(Node node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        /*method suitable with the test:
        
        NodeList simpleTypeList = node.getChildNodes();
        for (int i = 0; i < simpleTypeList.getLength(); i++) {
            Node n = simpleTypeList.item(i);
            if (n.getNodeName().equals("simpleType")) {
                return true;
            }
        }
        return false;
        */
    }

    @Override
    public boolean isComplexType(Node node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getAttributes(Node node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Node> getUnderElements(Node node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

    public static void main(String[] args) {
        
    }}
