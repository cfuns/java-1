/**
 * RemotePageHistory.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.atlassian.confluence.rpc.soap.beans;

@SuppressWarnings({ "unused", "serial", "rawtypes" })
public class RemotePageHistory implements java.io.Serializable {

	private long id;

	private java.util.Calendar modified;

	private java.lang.String modifier;

	private int version;

	public RemotePageHistory() {
	}

	public RemotePageHistory(final long id, final java.util.Calendar modified, final java.lang.String modifier, final int version) {
		this.id = id;
		this.modified = modified;
		this.modifier = modifier;
		this.version = version;
	}

	/**
	 * Gets the id value for this RemotePageHistory.
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id value for this RemotePageHistory.
	 * 
	 * @param id
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Gets the modified value for this RemotePageHistory.
	 * 
	 * @return modified
	 */
	public java.util.Calendar getModified() {
		return modified;
	}

	/**
	 * Sets the modified value for this RemotePageHistory.
	 * 
	 * @param modified
	 */
	public void setModified(final java.util.Calendar modified) {
		this.modified = modified;
	}

	/**
	 * Gets the modifier value for this RemotePageHistory.
	 * 
	 * @return modifier
	 */
	public java.lang.String getModifier() {
		return modifier;
	}

	/**
	 * Sets the modifier value for this RemotePageHistory.
	 * 
	 * @param modifier
	 */
	public void setModifier(final java.lang.String modifier) {
		this.modifier = modifier;
	}

	/**
	 * Gets the version value for this RemotePageHistory.
	 * 
	 * @return version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Sets the version value for this RemotePageHistory.
	 * 
	 * @param version
	 */
	public void setVersion(final int version) {
		this.version = version;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(final java.lang.Object obj) {
		if (!(obj instanceof RemotePageHistory))
			return false;
		final RemotePageHistory other = (RemotePageHistory) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		final boolean _equals;
		_equals = true && this.id == other.getId() && ((this.modified == null && other.getModified() == null) || (this.modified != null && this.modified.equals(other.getModified())))
				&& ((this.modifier == null && other.getModifier() == null) || (this.modifier != null && this.modifier.equals(other.getModifier()))) && this.version == other.getVersion();
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
		_hashCode += new Long(getId()).hashCode();
		if (getModified() != null) {
			_hashCode += getModified().hashCode();
		}
		if (getModifier() != null) {
			_hashCode += getModifier().hashCode();
		}
		_hashCode += getVersion();
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(RemotePageHistory.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePageHistory"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("id");
		elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("modified");
		elemField.setXmlName(new javax.xml.namespace.QName("", "modified"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("modifier");
		elemField.setXmlName(new javax.xml.namespace.QName("", "modifier"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("version");
		elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
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
