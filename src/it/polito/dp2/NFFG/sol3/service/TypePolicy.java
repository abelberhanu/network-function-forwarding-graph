//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.14 at 01:32:56 PM CET 
//


package it.polito.dp2.NFFG.sol3.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TypePolicy complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TypePolicy">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ReachPolicy" type="{http://www.example.org/nffgInfo}TypeReachPolicy" minOccurs="0"/>
 *         &lt;element name="NffgResult" type="{http://www.example.org/nffgInfo}TypeNffgResult" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nffgname" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="IsPosetive" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TypePolicy", propOrder = {
    "reachPolicy",
    "nffgResult"
})
public class TypePolicy {

    @XmlElement(name = "ReachPolicy")
    protected TypeReachPolicy reachPolicy;
    @XmlElement(name = "NffgResult")
    protected TypeNffgResult nffgResult;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "nffgname", required = true)
    protected String nffgname;
    @XmlAttribute(name = "IsPosetive")
    protected Boolean isPosetive;

    /**
     * Gets the value of the reachPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link TypeReachPolicy }
     *     
     */
    public TypeReachPolicy getReachPolicy() {
        return reachPolicy;
    }

    /**
     * Sets the value of the reachPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeReachPolicy }
     *     
     */
    public void setReachPolicy(TypeReachPolicy value) {
        this.reachPolicy = value;
    }

    /**
     * Gets the value of the nffgResult property.
     * 
     * @return
     *     possible object is
     *     {@link TypeNffgResult }
     *     
     */
    public TypeNffgResult getNffgResult() {
        return nffgResult;
    }

    /**
     * Sets the value of the nffgResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeNffgResult }
     *     
     */
    public void setNffgResult(TypeNffgResult value) {
        this.nffgResult = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the nffgname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNffgname() {
        return nffgname;
    }

    /**
     * Sets the value of the nffgname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNffgname(String value) {
        this.nffgname = value;
    }

    /**
     * Gets the value of the isPosetive property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPosetive() {
        return isPosetive;
    }

    /**
     * Sets the value of the isPosetive property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPosetive(Boolean value) {
        this.isPosetive = value;
    }

}
