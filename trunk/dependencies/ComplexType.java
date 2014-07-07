/*
Project for PV138 as tought on Faculty of Informatics on Masaryk University in 2014
 */
package generated;

import java.util.Map;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 * @param <T> content type in this element
 */
public interface ComplexType<T> {

    /**
     *
     * @return content of this element as assigned on Marshaling, or modified in
     * the implementation afterwards
     */
    T getContent();

    /**
     *
     * @param content content to be set for this object. Default set on
     * Marshaling
     * @return this object
     */
    ComplexType<T> setContent(T content);

    /**
     *
     * @return attributes of this object formatted as <Attribute name, Attribute
     * value>
     */
    Map<String, Object> getAttributes();

    /**
     *
     * @param attribute name of the attribute whose value is to be set
     * @param value new value of the specified attribute of this object
     * @return this object
     */
    ComplexType<T> setAttribute(String attribute, Object value);

}
