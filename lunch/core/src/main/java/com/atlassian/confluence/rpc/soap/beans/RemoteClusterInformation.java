/**
 * RemoteClusterInformation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.atlassian.confluence.rpc.soap.beans;

import javax.xml.namespace.QName;

@SuppressWarnings({"unused", "serial", "rawtypes"})
public class RemoteClusterInformation implements java.io.Serializable {

	private java.lang.String description;

	private int memberCount;

	private java.lang.Object[] members;

	private java.lang.String multicastAddress;

	private java.lang.String multicastPort;

	private java.lang.String name;

	private boolean running;

	public RemoteClusterInformation() {
	}

	public RemoteClusterInformation(
		final java.lang.String description,
		final int memberCount,
		final java.lang.Object[] members,
		final java.lang.String multicastAddress,
		final java.lang.String multicastPort,
		final java.lang.String name,
		final boolean running
	) {
		this.description = description;
		this.memberCount = memberCount;
		this.members = members;
		this.multicastAddress = multicastAddress;
		this.multicastPort = multicastPort;
		this.name = name;
		this.running = running;
	}

	/**
	 * Gets the description value for this RemoteClusterInformation.
	 *
	 * @return description
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Sets the description value for this RemoteClusterInformation.
	 *
	 * @param description
	 */
	public void setDescription(final java.lang.String description) {
		this.description = description;
	}

	/**
	 * Gets the memberCount value for this RemoteClusterInformation.
	 *
	 * @return memberCount
	 */
	public int getMemberCount() {
		return memberCount;
	}

	/**
	 * Sets the memberCount value for this RemoteClusterInformation.
	 *
	 * @param memberCount
	 */
	public void setMemberCount(final int memberCount) {
		this.memberCount = memberCount;
	}

	/**
	 * Gets the members value for this RemoteClusterInformation.
	 *
	 * @return members
	 */
	public java.lang.Object[] getMembers() {
		return members;
	}

	/**
	 * Sets the members value for this RemoteClusterInformation.
	 *
	 * @param members
	 */
	public void setMembers(final java.lang.Object[] members) {
		this.members = members;
	}

	/**
	 * Gets the multicastAddress value for this RemoteClusterInformation.
	 *
	 * @return multicastAddress
	 */
	public java.lang.String getMulticastAddress() {
		return multicastAddress;
	}

	/**
	 * Sets the multicastAddress value for this RemoteClusterInformation.
	 *
	 * @param multicastAddress
	 */
	public void setMulticastAddress(final java.lang.String multicastAddress) {
		this.multicastAddress = multicastAddress;
	}

	/**
	 * Gets the multicastPort value for this RemoteClusterInformation.
	 *
	 * @return multicastPort
	 */
	public java.lang.String getMulticastPort() {
		return multicastPort;
	}

	/**
	 * Sets the multicastPort value for this RemoteClusterInformation.
	 *
	 * @param multicastPort
	 */
	public void setMulticastPort(final java.lang.String multicastPort) {
		this.multicastPort = multicastPort;
	}

	/**
	 * Gets the name value for this RemoteClusterInformation.
	 *
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this RemoteClusterInformation.
	 *
	 * @param name
	 */
	public void setName(final java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the running value for this RemoteClusterInformation.
	 *
	 * @return running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Sets the running value for this RemoteClusterInformation.
	 *
	 * @param running
	 */
	public void setRunning(final boolean running) {
		this.running = running;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(final java.lang.Object obj) {
		if (!(obj instanceof RemoteClusterInformation))
			return false;
		final RemoteClusterInformation other = (RemoteClusterInformation) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		final boolean _equals;
		_equals = true && ((this.description == null && other.getDescription() == null) || (this.description != null && this.description.equals(other.getDescription())))
			&& this.memberCount == other.getMemberCount()
			&& ((this.members == null && other.getMembers() == null) || (this.members != null && java.util.Arrays.equals(this.members, other.getMembers())))
			&& ((this.multicastAddress == null && other.getMulticastAddress() == null) || (this.multicastAddress != null && this.multicastAddress.equals(other.getMulticastAddress())))
			&& ((this.multicastPort == null && other.getMulticastPort() == null) || (this.multicastPort != null && this.multicastPort.equals(other.getMulticastPort())))
			&& ((this.name == null && other.getName() == null) || (this.name != null && this.name.equals(other.getName()))) && this.running == other.isRunning();
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
		if (getDescription() != null) {
			_hashCode += getDescription().hashCode();
		}
		_hashCode += getMemberCount();
		if (getMembers() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getMembers()); i++) {
				final java.lang.Object obj = java.lang.reflect.Array.get(getMembers(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getMulticastAddress() != null) {
			_hashCode += getMulticastAddress().hashCode();
		}
		if (getMulticastPort() != null) {
			_hashCode += getMulticastPort().hashCode();
		}
		if (getName() != null) {
			_hashCode += getName().hashCode();
		}
		_hashCode += (isRunning() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(RemoteClusterInformation.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteClusterInformation"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("description");
		elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("memberCount");
		elemField.setXmlName(new javax.xml.namespace.QName("", "memberCount"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("members");
		elemField.setXmlName(new javax.xml.namespace.QName("", "members"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("multicastAddress");
		elemField.setXmlName(new javax.xml.namespace.QName("", "multicastAddress"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("multicastPort");
		elemField.setXmlName(new javax.xml.namespace.QName("", "multicastPort"));
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
		elemField.setFieldName("running");
		elemField.setXmlName(new javax.xml.namespace.QName("", "running"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
