/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parser;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author Sven
 */
public interface BinderInterface {
    
    
    /**
     * 
     * for all number types should return double and string type to String
     * other types are recognized as non-simpletype objects(?)
     * 
     * 
     * @param convert is type in XML format(xsd?) 
     * @return datatype in String
     */
    String  convertType(String convert);
    
    /**
     * opens new Buffered reader  and creates BinderFile class
     * 
     * @throws java.io.IOException
     */
    void run() throws IOException;
    
    
    /**
     * analysing the complex content
     * @param path Xpath
     * @param parentalNodes parental nodes as string separated with dot
     * @param writer opened BufferedWriter which will be used
     */
    void analyse(String path, String parentalNodes, BufferedWriter writer) throws IOException;
    
    
    /** 
     * creating java classes from  complex types
     */
    void complexToClass(String className, String parentNames);
    
    /**
     * creating java variable from simple methods(este to mozno bude inak)
     */
    void  createFactoryMethod(String className, String ancestorClasses);
}
