/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package generatedMan;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public interface ComplexInterface {
    //gets the list of sub elements of this complexType node referenced by ID
    List<Integer> getSubElements();
    
    //gets a value of this complexType node assigned at parsing
    Object getContent();
    
    //gets the attributes names
    //after parsing, together with their values
    Map<String, String> getAttributes();
    
    //gets the parent of a complexType node
    //Object getParent();
     int getParent();
     
     
}
