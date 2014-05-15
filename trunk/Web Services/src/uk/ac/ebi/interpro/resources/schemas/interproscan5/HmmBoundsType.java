//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.03.06 at 01:00:16 PM EST 
//


package uk.ac.ebi.interpro.resources.schemas.interproscan5;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HmmBoundsType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HmmBoundsType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="COMPLETE"/>
 *     &lt;enumeration value="N_TERMINAL_COMPLETE"/>
 *     &lt;enumeration value="C_TERMINAL_COMPLETE"/>
 *     &lt;enumeration value="INCOMPLETE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "HmmBoundsType")
@XmlEnum
public enum HmmBoundsType {

    COMPLETE,
    N_TERMINAL_COMPLETE,
    C_TERMINAL_COMPLETE,
    INCOMPLETE;

    public String value() {
        return name();
    }

    public static HmmBoundsType fromValue(String v) {
        return valueOf(v);
    }

}