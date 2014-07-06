/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package generatedInterface;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 * @param <T> content type in this element
 */
public interface ComplexType<T> {
    
    T getContent();
    
    ComplexType<T> setContent(T content);
    
    Map<String, Object> getAttributes();
    
    ComplexType<T> setAttribute(String attribute, Object value);
     
}
