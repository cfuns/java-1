/**
 * RemotePage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.atlassian.confluence.rpc.soap.beans;

@SuppressWarnings({ "unused", "serial", "rawtypes" })
public class RemotePage extends com.atlassian.confluence.rpc.soap.beans.RemotePageSummary implements java.io.Serializable {

	private java.lang.String content;

	private java.lang.String contentStatus;

	private java.util.Calendar created;

	private java.lang.String creator;

	private boolean current;

	private boolean homePage;

	private java.util.Calendar modified;

	private java.lang.String modifier;

	public RemotePage() {
	}

	public RemotePage(
			final long id,
			final int permissions,
			final java.lang.String space,
			final java.lang.String title,
			final java.lang.String url,
			final long parentId,
			final int version,
			final java.lang.String content,
			final java.lang.String contentStatus,
			final java.util.Calendar created,
			final java.lang.String creator,
			final boolean current,
			final boolean homePage,
			final java.util.Calendar modified,
			final java.lang.String modifier) {
		super(id, permissions, space, title, url, parentId, version);
		this.content = content;
		this.contentStatus = contentStatus;
		this.created = created;
		this.creator = creator;
		this.current = current;
		this.homePage = homePage;
		this.modified = modified;
		this.modifier = modifier;
	}

	/**
	 * Gets the content value for this RemotePage.
	 * 
	 * @return content
	 */
	public java.lang.String getContent() {
		return content;
	}

	/**
	 * Sets the content value for this RemotePage.
	 * 
	 * @param content
	 */
	public void setContent(final java.lang.String content) {
		this.content = content;
	}

	/**
	 * Gets the contentStatus value for this RemotePage.
	 * 
	 * @return contentStatus
	 */
	public java.lang.String getContentStatus() {
		return contentStatus;
	}

	/**
	 * Sets the contentStatus value for this RemotePage.
	 * 
	 * @param contentStatus
	 */
	public void setContentStatus(final java.lang.String contentStatus) {
		this.contentStatus = contentStatus;
	}

	/**
	 * Gets the created value for this RemotePage.
	 * 
	 * @return created
	 */
	public java.util.Calendar getCreated() {
		return created;
	}

	/**
	 * Sets the created value for this RemotePage.
	 * 
	 * @param created
	 */
	public void setCreated(final java.util.Calendar created) {
		this.created = created;
	}

	/**
	 * Gets the creator value for this RemotePage.
	 * 
	 * @return creator
	 */
	public java.lang.String getCreator() {
		return creator;
	}

	/**
	 * Sets the creator value for this RemotePage.
	 * 
	 * @param creator
	 */
	public void setCreator(final java.lang.String creator) {
		this.creator = creator;
	}

	/**
	 * Gets the current value for this RemotePage.
	 * 
	 * @return current
	 */
	public boolean isCurrent() {
		return current;
	}

	/**
	 * Sets the current value for this RemotePage.
	 * 
	 * @param current
	 */
	public void setCurrent(final boolean current) {
		this.current = current;
	}

	/**
	 * Gets the homePage value for this RemotePage.
	 * 
	 * @return homePage
	 */
	public boolean isHomePage() {
		return homePage;
	}

	/**
	 * Sets the homePage value for this RemotePage.
	 * 
	 * @param homePage
	 */
	public void setHomePage(final boolean homePage) {
		this.homePage = homePage;
	}

	/**
	 * Gets the modified value for this RemotePage.
	 * 
	 * @return modified
	 */
	public java.util.Calendar getModified() {
		return modified;
	}

	/**
	 * Sets the modified value for this RemotePage.
	 * 
	 * @param modified
	 */
	public void setModified(final java.util.Calendar modified) {
		this.modified = modified;
	}

	/**
	 * Gets the modifier value for this RemotePage.
	 * 
	 * @return modifier
	 */
	public java.lang.String getModifier() {
		return modifier;
	}

	/**
	 * Sets the modifier value for this RemotePage.
	 * 
	 * @param modifier
	 */
	public void setModifier(final java.lang.String modifier) {
		this.modifier = modifier;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(final java.lang.Object obj) {
		if (!(obj instanceof RemotePage))
			return false;
		final RemotePage other = (RemotePage) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		final boolean _equals;
		_equals = super.equals(obj) && ((this.content == null && other.getContent() == null) || (this.content != null && this.content.equals(other.getContent())))
				&& ((this.contentStatus == null && other.getContentStatus() == null) || (this.contentStatus != null && this.contentStatus.equals(other.getContentStatus())))
				&& ((this.created == null && other.getCreated() == null) || (this.created != null && this.created.equals(other.getCreated())))
				&& ((this.creator == null && other.getCreator() == null) || (this.creator != null && this.creator.equals(other.getCreator()))) && this.current == other.isCurrent()
				&& this.homePage == other.isHomePage() && ((this.modified == null && other.getModified() == null) || (this.modified != null && this.modified.equals(other.getModified())))
				&& ((this.modifier == null && other.getModifier() == null) || (this.modifier != null && this.modifier.equals(other.getModifier())));
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
		if (getContent() != null) {
			_hashCode += getContent().hashCode();
		}
		if (getContentStatus() != null) {
			_hashCode += getContentStatus().hashCode();
		}
		if (getCreated() != null) {
			_hashCode += getCreated().hashCode();
		}
		if (getCreator() != null) {
			_hashCode += getCreator().hashCode();
		}
		_hashCode += (isCurrent() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isHomePage() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		if (getModified() != null) {
			_hashCode += getModified().hashCode();
		}
		if (getModifier() != null) {
			_hashCode += getModifier().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(RemotePage.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemotePage"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("content");
		elemField.setXmlName(new javax.xml.namespace.QName("", "content"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("contentStatus");
		elemField.setXmlName(new javax.xml.namespace.QName("", "contentStatus"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("created");
		elemField.setXmlName(new javax.xml.namespace.QName("", "created"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("creator");
		elemField.setXmlName(new javax.xml.namespace.QName("", "creator"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("current");
		elemField.setXmlName(new javax.xml.namespace.QName("", "current"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("homePage");
		elemField.setXmlName(new javax.xml.namespace.QName("", "homePage"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
