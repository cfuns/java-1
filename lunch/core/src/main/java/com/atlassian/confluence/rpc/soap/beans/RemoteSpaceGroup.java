/**
 * RemoteSpaceGroup.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.atlassian.confluence.rpc.soap.beans;

@SuppressWarnings({"unused", "serial", "rawtypes"})
public class RemoteSpaceGroup implements java.io.Serializable {

	private java.lang.String creatorName;

	private java.lang.String key;

	private java.lang.String licenseKey;

	private java.lang.String name;

	public RemoteSpaceGroup() {
	}

	public RemoteSpaceGroup(final java.lang.String creatorName, final java.lang.String key, final java.lang.String licenseKey, final java.lang.String name) {
		this.creatorName = creatorName;
		this.key = key;
		this.licenseKey = licenseKey;
		this.name = name;
	}

	/**
	 * Gets the creatorName value for this RemoteSpaceGroup.
	 *
	 * @return creatorName
	 */
	public java.lang.String getCreatorName() {
		return creatorName;
	}

	/**
	 * Sets the creatorName value for this RemoteSpaceGroup.
	 *
	 * @param creatorName
	 */
	public void setCreatorName(final java.lang.String creatorName) {
		this.creatorName = creatorName;
	}

	/**
	 * Gets the key value for this RemoteSpaceGroup.
	 *
	 * @return key
	 */
	public java.lang.String getKey() {
		return key;
	}

	/**
	 * Sets the key value for this RemoteSpaceGroup.
	 *
	 * @param key
	 */
	public void setKey(final java.lang.String key) {
		this.key = key;
	}

	/**
	 * Gets the licenseKey value for this RemoteSpaceGroup.
	 *
	 * @return licenseKey
	 */
	public java.lang.String getLicenseKey() {
		return licenseKey;
	}

	/**
	 * Sets the licenseKey value for this RemoteSpaceGroup.
	 *
	 * @param licenseKey
	 */
	public void setLicenseKey(final java.lang.String licenseKey) {
		this.licenseKey = licenseKey;
	}

	/**
	 * Gets the name value for this RemoteSpaceGroup.
	 *
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this RemoteSpaceGroup.
	 *
	 * @param name
	 */
	public void setName(final java.lang.String name) {
		this.name = name;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(final java.lang.Object obj) {
		if (!(obj instanceof RemoteSpaceGroup))
			return false;
		final RemoteSpaceGroup other = (RemoteSpaceGroup) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		final boolean _equals;
		_equals = true && ((this.creatorName == null && other.getCreatorName() == null) || (this.creatorName != null && this.creatorName.equals(other.getCreatorName())))
			&& ((this.key == null && other.getKey() == null) || (this.key != null && this.key.equals(other.getKey())))
			&& ((this.licenseKey == null && other.getLicenseKey() == null) || (this.licenseKey != null && this.licenseKey.equals(other.getLicenseKey())))
			&& ((this.name == null && other.getName() == null) || (this.name != null && this.name.equals(other.getName())));
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
		if (getCreatorName() != null) {
			_hashCode += getCreatorName().hashCode();
		}
		if (getKey() != null) {
			_hashCode += getKey().hashCode();
		}
		if (getLicenseKey() != null) {
			_hashCode += getLicenseKey().hashCode();
		}
		if (getName() != null) {
			_hashCode += getName().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(RemoteSpaceGroup.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteSpaceGroup"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("creatorName");
		elemField.setXmlName(new javax.xml.namespace.QName("", "creatorName"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("key");
		elemField.setXmlName(new javax.xml.namespace.QName("", "key"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("licenseKey");
		elemField.setXmlName(new javax.xml.namespace.QName("", "licenseKey"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("name");
		elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
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
	public static org.apache.axis.encoding.Serializer getSerializer(
		final java.lang.String mechType,
		final java.lang.Class _javaType,
		final javax.xml.namespace.QName _xmlType
	) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	public static org.apache.axis.encoding.Deserializer getDeserializer(
		final java.lang.String mechType,
		final java.lang.Class _javaType,
		final javax.xml.namespace.QName _xmlType
	) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

}
