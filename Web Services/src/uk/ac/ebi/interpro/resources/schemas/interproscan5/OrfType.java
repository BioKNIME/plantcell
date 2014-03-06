//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.03.06 at 01:00:16 PM EST 
//


package uk.ac.ebi.interpro.resources.schemas.interproscan5;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrfType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrfType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ebi.ac.uk/interpro/resources/schemas/interproscan5}protein" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="end" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="start" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="strand" use="required" type="{http://www.ebi.ac.uk/interpro/resources/schemas/interproscan5}NucleotideSequenceStrandType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrfType", propOrder = {
    "protein"
})
public class OrfType {

    protected ProteinType protein;
    @XmlAttribute(name = "end", required = true)
    protected int end;
    @XmlAttribute(name = "start", required = true)
    protected int start;
    @XmlAttribute(name = "strand", required = true)
    protected NucleotideSequenceStrandType strand;

    /**
     * Gets the value of the protein property.
     * 
     * @return
     *     possible object is
     *     {@link ProteinType }
     *     
     */
    public ProteinType getProtein() {
        return protein;
    }

    /**
     * Sets the value of the protein property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProteinType }
     *     
     */
    public void setProtein(ProteinType value) {
        this.protein = value;
    }

    /**
     * Gets the value of the end property.
     * 
     */
    public int getEnd() {
        return end;
    }

    /**
     * Sets the value of the end property.
     * 
     */
    public void setEnd(int value) {
        this.end = value;
    }

    /**
     * Gets the value of the start property.
     * 
     */
    public int getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     */
    public void setStart(int value) {
        this.start = value;
    }

    /**
     * Gets the value of the strand property.
     * 
     * @return
     *     possible object is
     *     {@link NucleotideSequenceStrandType }
     *     
     */
    public NucleotideSequenceStrandType getStrand() {
        return strand;
    }

    /**
     * Sets the value of the strand property.
     * 
     * @param value
     *     allowed object is
     *     {@link NucleotideSequenceStrandType }
     *     
     */
    public void setStrand(NucleotideSequenceStrandType value) {
        this.strand = value;
    }

}
