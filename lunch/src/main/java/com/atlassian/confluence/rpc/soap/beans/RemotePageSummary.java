/**
 * RemotePageSummary.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.atlassian.confluence.rpc.soap.beans;

@SuppressWarnings({ "unused", "serial", "rawtypes" })
public class RemotePageSummary extends com.atlassian.confluence.rpc.soap.beans.AbstractRemotePageSummary implements java.io.Serializable {

	private long parentId;

	private int version;

	public RemotePageSummary() {
	}

	public RemotePageSummary(final long id, final int permissions, final java.lang.String space, final java.lang.String title, final java.lang.String url, final long parentId, final int version) {
		super(id, permissions, space, title, url);
		this.parentId = parentId;
		this.version = version;
	}

	/**
	 * Gets the parentId value for this RemotePageSummary.
	 * 
	 * @return parentId
	 */
	public long getParentId() {
		return parentId;
	}

	/**
	 * Sets the parentId value for this RemotePageSummary.
	 * 
	 * @param parentId
	 */
	public void setParentId(final long parentId) {
		this.parentId = parentId;
	}

	/**
	 * Gets the version value for this RemotePageSummary.
	 * 
	 * @return version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Sets the version value for this RemotePageSummary.
	 * 
	 * @param version
	 */
	public void setVersion(final int version) {
		this.version = version;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(final java.lang.Object obj) {
		if (!(obj instanceof RemotePageSummary))
			return false;
		final RemotePageSummary other = (RemotePageSummary) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = super.equals(obj) && this.parentId == other.getParentId() && this.version == other.getVersion();
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
		int _hashCode = super.hashCode();
		_hashCode += new Long(getParentId()).hashCode();
		_hashCode += getVersion();
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(RemotePageSummary.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePageSummary"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("parentId");
		elemField.setXmlName(new javax.xml.namespace.QName("", "parentId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setNillable(false);
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
