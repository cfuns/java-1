/**
 * RemoteBlogEntrySummary.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.atlassian.confluence.rpc.soap.beans;

@SuppressWarnings({ "unused", "serial", "rawtypes" })
public class RemoteBlogEntrySummary extends com.atlassian.confluence.rpc.soap.beans.AbstractRemotePageSummary implements java.io.Serializable {

	private java.lang.String author;

	private java.util.Calendar publishDate;

	public RemoteBlogEntrySummary() {
	}

	public RemoteBlogEntrySummary(
			final long id,
			final int permissions,
			final java.lang.String space,
			final java.lang.String title,
			final java.lang.String url,
			final java.lang.String author,
			final java.util.Calendar publishDate) {
		super(id, permissions, space, title, url);
		this.author = author;
		this.publishDate = publishDate;
	}

	/**
	 * Gets the author value for this RemoteBlogEntrySummary.
	 * 
	 * @return author
	 */
	public java.lang.String getAuthor() {
		return author;
	}

	/**
	 * Sets the author value for this RemoteBlogEntrySummary.
	 * 
	 * @param author
	 */
	public void setAuthor(final java.lang.String author) {
		this.author = author;
	}

	/**
	 * Gets the publishDate value for this RemoteBlogEntrySummary.
	 * 
	 * @return publishDate
	 */
	public java.util.Calendar getPublishDate() {
		return publishDate;
	}

	/**
	 * Sets the publishDate value for this RemoteBlogEntrySummary.
	 * 
	 * @param publishDate
	 */
	public void setPublishDate(final java.util.Calendar publishDate) {
		this.publishDate = publishDate;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(final java.lang.Object obj) {
		if (!(obj instanceof RemoteBlogEntrySummary))
			return false;
		final RemoteBlogEntrySummary other = (RemoteBlogEntrySummary) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = super.equals(obj) && ((this.author == null && other.getAuthor() == null) || (this.author != null && this.author.equals(other.getAuthor())))
				&& ((this.publishDate == null && other.getPublishDate() == null) || (this.publishDate != null && this.publishDate.equals(other.getPublishDate())));
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
		if (getAuthor() != null) {
			_hashCode += getAuthor().hashCode();
		}
		if (getPublishDate() != null) {
			_hashCode += getPublishDate().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(RemoteBlogEntrySummary.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.soap.rpc.confluence.atlassian.com", "RemoteBlogEntrySummary"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("author");
		elemField.setXmlName(new javax.xml.namespace.QName("", "author"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("publishDate");
		elemField.setXmlName(new javax.xml.namespace.QName("", "publishDate"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
