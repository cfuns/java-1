/**
 * RemoteNodeStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.atlassian.confluence.rpc.soap.beans;

@SuppressWarnings({"unused", "serial", "rawtypes"})
public class RemoteNodeStatus implements java.io.Serializable {

	private java.util.HashMap JVMstats;

	private java.util.HashMap buildStats;

	private int nodeId;

	private java.util.HashMap props;

	public RemoteNodeStatus() {
	}

	public RemoteNodeStatus(final java.util.HashMap JVMstats, final java.util.HashMap buildStats, final int nodeId, final java.util.HashMap props) {
		this.JVMstats = JVMstats;
		this.buildStats = buildStats;
		this.nodeId = nodeId;
		this.props = props;
	}

	/**
	 * Gets the JVMstats value for this RemoteNodeStatus.
	 *
	 * @return JVMstats
	 */
	public java.util.HashMap getJVMstats() {
		return JVMstats;
	}

	/**
	 * Sets the JVMstats value for this RemoteNodeStatus.
	 *
	 * @param JVMstats
	 */
	public void setJVMstats(final java.util.HashMap JVMstats) {
		this.JVMstats = JVMstats;
	}

	/**
	 * Gets the buildStats value for this RemoteNodeStatus.
	 *
	 * @return buildStats
	 */
	public java.util.HashMap getBuildStats() {
		return buildStats;
	}

	/**
	 * Sets the buildStats value for this RemoteNodeStatus.
	 *
	 * @param buildStats
	 */
	public void setBuildStats(final java.util.HashMap buildStats) {
		this.buildStats = buildStats;
	}

	/**
	 * Gets the nodeId value for this RemoteNodeStatus.
	 *
	 * @return nodeId
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * Sets the nodeId value for this RemoteNodeStatus.
	 *
	 * @param nodeId
	 */
	public void setNodeId(final int nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Gets the props value for this RemoteNodeStatus.
	 *
	 * @return props
	 */
	public java.util.HashMap getProps() {
		return props;
	}

	/**
	 * Sets the props value for this RemoteNodeStatus.
	 *
	 * @param props
	 */
	public void setProps(final java.util.HashMap props) {
		this.props = props;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(final java.lang.Object obj) {
		if (!(obj instanceof RemoteNodeStatus))
			return false;
		final RemoteNodeStatus other = (RemoteNodeStatus) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		final boolean _equals;
		_equals = true && ((this.JVMstats == null && other.getJVMstats() == null) || (this.JVMstats != null && this.JVMstats.equals(other.getJVMstats())))
			&& ((this.buildStats == null && other.getBuildStats() == null) || (this.buildStats != null && this.buildStats.equals(other.getBuildStats())))
			&& this.nodeId == other.getNodeId() && ((this.props == null && other.getProps() == null) || (this.props != null && this.props.equals(other.getProps())));
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
		if (getJVMstats() != null) {
			_hashCode += getJVMstats().hashCode();
		}
		if (getBuildStats() != null) {
			_hashCode += getBuildStats().hashCode();
		}
		_hashCode += getNodeId();
		if (getProps() != null) {
			_hashCode += getProps().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(RemoteNodeStatus.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteNodeStatus"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("JVMstats");
		elemField.setXmlName(new javax.xml.namespace.QName("", "JVMstats"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("buildStats");
		elemField.setXmlName(new javax.xml.namespace.QName("", "buildStats"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("nodeId");
		elemField.setXmlName(new javax.xml.namespace.QName("", "nodeId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("props");
		elemField.setXmlName(new javax.xml.namespace.QName("", "props"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
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
