/*
Project for PV138 as tought on Faculty of Informatics on Masaryk University in 2014
 */
package generated;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 * @param <T> type of the content in simpleType class
 */
public interface SimpleType<T> {
    boolean complex = false;
    /**
     *  
     * @return content of this element as assigned on Marshaling, or modified in the implementation afterwards
     */
    T getContent();
    /**
     * 
     * @param content content to be set for this object. Default set on Marshaling
     * @return this object
     */
    SimpleType<T> setContent(T content) ;
}
