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
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 *
 * @author Sven Relovsky
 */
public class Binder implements BinderInterface{
    private Document doc;
    private XPath xpath;
    String prefix;
    String classString;
    private ArrayList<String> classNameList = new ArrayList<String>();
    
    
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
        
    

    @Override
    public void run() throws IOException{
        
        prefix = doc.getDocumentElement().getPrefix();
        
        new File("/generatedFiles").mkdir();
        File generatedClasses = new File("generatedFiles/generatedClasses.java");
        BufferedWriter classWriter = new BufferedWriter(new FileWriter(generatedClasses));
        
        classString = ("package generatedFiles;"
                + "/**\n"
                + "*contains java class creating"
                + "*for each element of schema"
                + "*/"
                + "public class GeneratedClasses{\n\t");
        
        //run
        analyse("/"+prefix+":schema","",classWriter);
        
        classString+=("\n}");
         classWriter.write(classString);
         classWriter.close();      
               
        
    }
    @Override
    public void analyse(String path, String parentalNodes, BufferedWriter writer)
            throws IOException{
        boolean a = true;
        String newParents = parentalNodes;
        NodeList nodes = null;
        String newPath;
        
        
        //This will require further processing of siblings
        newPath = path + "/" + prefix + ":complexType[@name]";
        
        try {
        XPathExpression expr = xpath.compile(path);
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        nodes = (NodeList) result;
        } catch (XPathExpressionException ex) {
        Logger.getLogger(Binder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(nodes==null){                
        //This will create complextype just for one element
        newPath = path + "/" + prefix + ":element[@name and " + prefix +
                "complexType]";
                
        try {
        XPathExpression expr = xpath.compile(path);
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        nodes= (NodeList) result;
        a = false;
        } catch (XPathExpressionException ex) {
        Logger.getLogger(Binder.class.getName()).log(Level.SEVERE, null, ex);
        }    
        }
        if(nodes!=null){
            for(int i=0;i<nodes.getLength();i++){
                if(nodes.item(i) instanceof Element){
                    Element el = (Element) nodes.item(i);
                    String nameAttr = el.getAttribute(path);
                    
                    //possible name conflict
                    
                    
                    
                    classNameList.add(nameAttr);
                    if(a){
                        
                    }
                    
                    writer.write("}\n\n");
                    if(parentalNodes.isEmpty()){
                    writer.close();
                    }
                    
                    
                }                    
            }
        }
    }
    
    
    @Override
    public void complexToClass(String className,String parentNames) {
        
    }
    
    public void createAttributes(String path, BufferedWriter writer, boolean att, String parentNames){
    
    }

    @Override
    public void  createFactoryMethod(String className,String parentNames){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
