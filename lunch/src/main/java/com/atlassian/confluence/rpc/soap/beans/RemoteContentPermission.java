/**
 * RemoteContentPermission.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.atlassian.confluence.rpc.soap.beans;

import javax.xml.namespace.QName;

@SuppressWarnings({"unused", "serial", "rawtypes"})
public class RemoteContentPermission implements java.io.Serializable {

	private java.lang.String groupName;

	private java.lang.String type;

	private java.lang.String userName;

	public RemoteContentPermission() {
	}

	public RemoteContentPermission(final java.lang.String groupName, final java.lang.String type, final java.lang.String userName) {
		this.groupName = groupName;
		this.type = type;
		this.userName = userName;
	}

	/**
	 * Gets the groupName value for this RemoteContentPermission.
	 *
	 * @return groupName
	 */
	public java.lang.String getGroupName() {
		return groupName;
	}

	/**
	 * Sets the groupName value for this RemoteContentPermission.
	 *
	 * @param groupName
	 */
	public void setGroupName(final java.lang.String groupName) {
		this.groupName = groupName;
	}

	/**
	 * Gets the type value for this RemoteContentPermission.
	 *
	 * @return type
	 */
	public java.lang.String getType() {
		return type;
	}

	/**
	 * Sets the type value for this RemoteContentPermission.
	 *
	 * @param type
	 */
	public void setType(final java.lang.String type) {
		this.type = type;
	}

	/**
	 * Gets the userName value for this RemoteContentPermission.
	 *
	 * @return userName
	 */
	public java.lang.String getUserName() {
		return userName;
	}

	/**
	 * Sets the userName value for this RemoteContentPermission.
	 *
	 * @param userName
	 */
	public void setUserName(final java.lang.String userName) {
		this.userName = userName;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(final java.lang.Object obj) {
		if (!(obj instanceof RemoteContentPermission))
			return false;
		final RemoteContentPermission other = (RemoteContentPermission) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		final boolean _equals;
		_equals = true && ((this.groupName == null && other.getGroupName() == null) || (this.groupName != null && this.groupName.equals(other.getGroupName())))
			&& ((this.type == null && other.getType() == null) || (this.type != null && this.type.equals(other.getType())))
			&& ((this.userName == null && other.getUserName() == null) || (this.userName != null && this.userName.equals(other.getUserName())));
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
		if (getGroupName() != null) {
			_hashCode += getGroupName().hashCode();
		}
		if (getType() != null) {
			_hashCode += getType().hashCode();
		}
		if (getUserName() != null) {
			_hashCode += getUserName().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(RemoteContentPermission.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteContentPermission"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("groupName");
		elemField.setXmlName(new javax.xml.namespace.QName("", "groupName"));
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
		elemField.setFieldName("userName");
		elemField.setXmlName(new javax.xml.namespace.QName("", "userName"));
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
	public static org.apache.axis.encoding.Serializer getSerializer(final Class _javaType, final QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	public static org.apache.axis.encoding.Deserializer getDeserializer(final Class _javaType, final QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

}
