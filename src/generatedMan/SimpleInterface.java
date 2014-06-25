/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package generatedMan;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public interface SimpleInterface {
    //gets a value of this simpleType node assigned at parsing
    Object getContent();
    
    //gets the parent of a complexType node
    //Object getParent();
     int getParent();
}
