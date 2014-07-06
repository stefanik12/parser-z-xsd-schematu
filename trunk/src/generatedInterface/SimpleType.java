/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package generatedInterface;

import java.io.Serializable;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 * @param <T> type of the content in simpleType class
 */
public interface SimpleType<T> {
    boolean complex = false;
    //gets a value of this simpleType node assigned at parsing
    T getContent();
    
    SimpleType<T> setContent(T content) ;
}
