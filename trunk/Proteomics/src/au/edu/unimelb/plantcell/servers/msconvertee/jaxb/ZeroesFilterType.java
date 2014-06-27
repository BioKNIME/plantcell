//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.27 at 10:11:06 AM EST 
//


package au.edu.unimelb.plantcell.servers.msconvertee.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for zeroesFilterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="zeroesFilterType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flankingZeroCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="applyToMsLevel" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "zeroesFilterType", propOrder = {
    "mode",
    "flankingZeroCount",
    "applyToMsLevel"
})
public class ZeroesFilterType {

    @XmlElement(required = true)
    protected String mode;
    protected int flankingZeroCount;
    @XmlElement(type = Integer.class)
    protected List<Integer> applyToMsLevel;

    /**
     * Gets the value of the mode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the value of the mode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMode(String value) {
        this.mode = value;
    }

    /**
     * Gets the value of the flankingZeroCount property.
     * 
     */
    public int getFlankingZeroCount() {
        return flankingZeroCount;
    }

    /**
     * Sets the value of the flankingZeroCount property.
     * 
     */
    public void setFlankingZeroCount(int value) {
        this.flankingZeroCount = value;
    }

    /**
     * Gets the value of the applyToMsLevel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the applyToMsLevel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApplyToMsLevel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getApplyToMsLevel() {
        if (applyToMsLevel == null) {
            applyToMsLevel = new ArrayList<Integer>();
        }
        return this.applyToMsLevel;
    }

}
