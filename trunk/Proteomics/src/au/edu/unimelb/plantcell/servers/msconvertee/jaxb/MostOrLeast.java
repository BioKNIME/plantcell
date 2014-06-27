//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.27 at 10:11:06 AM EST 
//


package au.edu.unimelb.plantcell.servers.msconvertee.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MostOrLeast.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MostOrLeast">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MostIntense"/>
 *     &lt;enumeration value="LeastIntense"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MostOrLeast")
@XmlEnum
public enum MostOrLeast {

    @XmlEnumValue("MostIntense")
    MOST_INTENSE("MostIntense"),
    @XmlEnumValue("LeastIntense")
    LEAST_INTENSE("LeastIntense");
    private final String value;

    MostOrLeast(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MostOrLeast fromValue(String v) {
        for (MostOrLeast c: MostOrLeast.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
