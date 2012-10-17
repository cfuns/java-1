/**
 * RemoteSpaceSummary.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.atlassian.confluence.rpc.soap.beans;

@SuppressWarnings({ "unused", "serial", "rawtypes" })
public class RemoteSpaceSummary implements java.io.Serializable {

	private java.lang.String key;

	private java.lang.String name;

	private java.lang.String type;

	private java.lang.String url;

	public RemoteSpaceSummary() {
	}

	public RemoteSpaceSummary(final java.lang.String key, final java.lang.String name, final java.lang.String type, final java.lang.String url) {
		this.key = key;
		this.name = name;
		this.type = type;
		this.url = url;
	}

	/**
	 * Gets the key value for this RemoteSpaceSummary.
	 * 
	 * @return key
	 */
	public java.lang.String getKey() {
		return key;
	}

	/**
	 * Sets the key value for this RemoteSpaceSummary.
	 * 
	 * @param key
	 */
	public void setKey(final java.lang.String key) {
		this.key = key;
	}

	/**
	 * Gets the name value for this RemoteSpaceSummary.
	 * 
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this RemoteSpaceSummary.
	 * 
	 * @param name
	 */
	public void setName(final java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the type value for this RemoteSpaceSummary.
	 * 
	 * @return type
	 */
	public java.lang.String getType() {
		return type;
	}

	/**
	 * Sets the type value for this RemoteSpaceSummary.
	 * 
	 * @param type
	 */
	public void setType(final java.lang.String type) {
		this.type = type;
	}

	/**
	 * Gets the url value for this RemoteSpaceSummary.
	 * 
	 * @return url
	 */
	public java.lang.String getUrl() {
		return url;
	}

	/**
	 * Sets the url value for this RemoteSpaceSummary.
	 * 
	 * @param url
	 */
	public void setUrl(final java.lang.String url) {
		this.url = url;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(final java.lang.Object obj) {
		if (!(obj instanceof RemoteSpaceSummary))
			return false;
		final RemoteSpaceSummary other = (RemoteSpaceSummary) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this.key == null && other.getKey() == null) || (this.key != null && this.key.equals(other.getKey())))
				&& ((this.name == null && other.getName() == null) || (this.name != null && this.name.equals(other.getName())))
				&& ((this.type == null && other.getType() == null) || (this.type != null && this.type.equals(other.getType())))
				&& ((this.url == null && other.getUrl() == null) || (this.url != null && this.url.equals(other.getUrl())));
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	@Override
	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getKey() != null) {
			_hashCode += getKey().hashCode();
		}
		if (getName() != null) {
			_hashCode += getName().hashCode();
		}
		if (getType() != null) {
			_hashCode += getType().hashCode();
		}
		if (getUrl() != null) {
			_hashCode += getUrl().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(RemoteSpaceSummary.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpaceSummary"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("key");
		elemField.setXmlName(new javax.xml.namespace.QName("", "key"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("name");
		elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("type");
		elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("url");
		elemField.setXmlName(new javax.xml.namespace.QName("", "url"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
	}

	/**
	 * Return type metadata object
	 */
	public static org.apache.axis.description.TypeDesc getTypeDesc() {
		return typeDesc;
	}

	/**
	 * Get Custom Serializer
	 */
	public static org.apache.axis.encoding.Serializer getSerializer(final java.lang.String mechType, final java.lang.Class _javaType, final javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	public static org.apache.axis.encoding.Deserializer getDeserializer(final java.lang.String mechType, final java.lang.Class _javaType, final javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

}
