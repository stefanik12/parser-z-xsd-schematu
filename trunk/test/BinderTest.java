/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class BinderTest {
    
    public BinderTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * 
     * for all number types should return double and string type to String
     * other types are recognized as non-simpletype objects(?)
     * 
     * @param converted is type in XML format(xsd?) 
     * @return datatype in String
     */
    @Test
    public void convertType(String convert){
        
    }
    
    /**
     * opens new Buffered reader  and creates BinderFile class
     * 
     */
    @Test
    public void run() {
        
    }
    
    /** 
     * creating java classes from  complex types
     */
    @Test
    public void complexToClass(){
        
    }
    
    /**
     * creating java variable from simple methods(este to mozno bude inak)
     */
    @Test
    public void simpleToVar(){
        
    }
}
