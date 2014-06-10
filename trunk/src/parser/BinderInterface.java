/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parser;

/**
 *
 * @author Sven
 */
public interface BinderInterface {
    
    
    /**
     * 
     * for all number types should return double and for all other types String
     * 
     * @param converted is type in XML format(xsd?) 
     * @return datatype in String
     */
    String  convertType(String convert);
    
    /**
     * opens new Buffered reader  and creates BinderFile class
     * 
     */
    void run();
    
    /** 
     * creating java classes from  complex types
     */
    void complexToClass();
    
    /**
     * creating java variable from simple methods(este to mozno bude inak)
     */
    void simpleToVar();
}
