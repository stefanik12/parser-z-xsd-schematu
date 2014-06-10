/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parser;

import java.io.IOException;
import java.net.URI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 *
 * @author Sven Relovsky
 */
public class Binder implements BinderInterface{
    private Document doc;
    private XPath xpath;
    String prefix;
    /**
    * Constructor creating an instance of this class of given URI
     * @param uri
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
    */
    public Binder(URI uri) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory domFac = DocumentBuilderFactory.newInstance();
        domFac.setNamespaceAware(true);
        DocumentBuilder builder = domFac.newDocumentBuilder();
        doc = builder.parse(uri.toString());
        
        XPathFactory factory = XPathFactory.newInstance();
        xpath = factory.newXPath();
        
        prefix="xsd";//for now only xsd is implemented 
               
        
    }

    @Override
    public String convertType(String convert) {
               
        /* 
            if numric returns double
           source http://www.w3schools.com/schema/schema_dtypes_numeric.asp
        */
        if(convert.equals(prefix+":byte") || convert.equals(prefix+":decimal")
                || convert.equals(prefix+":int") 
                || convert.equals(prefix+":nonNegativeInteger") 
                || convert.equals(prefix+":nonPositiveInteger") 
                || convert.equals(prefix+":long") 
                || convert.equals(prefix+":negativeInteger") 
                || convert.equals(prefix+":short") 
                || convert.equals(prefix+":unsignedLong") 
                || convert.equals(prefix+":unsignedInt") 
                || convert.equals(prefix+":unsignedShort") 
                || convert.equals(prefix+":unsignedByte")
           ){
            return "double";
        }
        else{
            if(convert.equals(prefix+":string")){
                return "String";
            }
            else{
            return convert;
            }
            }            
        }
        
    }

    @Override
    public void run() {
        
    }

    @Override
    public void complexToClass() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void simpleToVar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
