/**
 * ConfluenceSoapServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package net.seibertmedia.kunden;

@SuppressWarnings("serial")
public class ConfluenceSoapServiceServiceLocator extends org.apache.axis.client.Service implements net.seibertmedia.kunden.ConfluenceSoapServiceService {

	public ConfluenceSoapServiceServiceLocator() {
	}

	public ConfluenceSoapServiceServiceLocator(final org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public ConfluenceSoapServiceServiceLocator(final java.lang.String wsdlLoc, final javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for ConfluenceserviceV1
	private java.lang.String ConfluenceserviceV1_address = "https://kunden.seibert-media.net/rpc/soap-axis/confluenceservice-v1";

	@Override
	public java.lang.String getConfluenceserviceV1Address() {
		return ConfluenceserviceV1_address;
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String ConfluenceserviceV1WSDDServiceName = "confluenceservice-v1";

	public java.lang.String getConfluenceserviceV1WSDDServiceName() {
		return ConfluenceserviceV1WSDDServiceName;
	}

	public void setConfluenceserviceV1WSDDServiceName(final java.lang.String name) {
		ConfluenceserviceV1WSDDServiceName = name;
	}

	@Override
	public net.seibertmedia.kunden.ConfluenceSoapService getConfluenceserviceV1() throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(ConfluenceserviceV1_address);
		}
		catch (final java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getConfluenceserviceV1(endpoint);
	}

	@Override
	public net.seibertmedia.kunden.ConfluenceSoapService getConfluenceserviceV1(final java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			final net.seibertmedia.kunden.ConfluenceserviceV1SoapBindingStub _stub = new net.seibertmedia.kunden.ConfluenceserviceV1SoapBindingStub(portAddress, this);
			_stub.setPortName(getConfluenceserviceV1WSDDServiceName());
			return _stub;
		}
		catch (final org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setConfluenceserviceV1EndpointAddress(final java.lang.String address) {
		ConfluenceserviceV1_address = address;
	}

	/**
	 * For the given interface, get the stub implementation.
	 * If this service has no port for the given interface,
	 * then ServiceException is thrown.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public java.rmi.Remote getPort(final Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (net.seibertmedia.kunden.ConfluenceSoapService.class.isAssignableFrom(serviceEndpointInterface)) {
				final net.seibertmedia.kunden.ConfluenceserviceV1SoapBindingStub _stub = new net.seibertmedia.kunden.ConfluenceserviceV1SoapBindingStub(new java.net.URL(
						ConfluenceserviceV1_address), this);
				_stub.setPortName(getConfluenceserviceV1WSDDServiceName());
				return _stub;
			}
		}
		catch (final java.lang.Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  "
				+ (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}

	/**
	 * For the given interface, get the stub implementation.
	 * If this service has no port for the given interface,
	 * then ServiceException is thrown.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public java.rmi.Remote getPort(final javax.xml.namespace.QName portName, final Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		final java.lang.String inputPortName = portName.getLocalPart();
		if ("confluenceservice-v1".equals(inputPortName)) {
			return getConfluenceserviceV1();
		}
		else {
			final java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	@Override
	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("https://kunden.seibert-media.net/rpc/soap-axis/confluenceservice-v1", "ConfluenceSoapServiceService");
	}

	@SuppressWarnings("rawtypes")
	private java.util.HashSet ports = null;

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName("https://kunden.seibert-media.net/rpc/soap-axis/confluenceservice-v1", "confluenceservice-v1"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(final java.lang.String portName, final java.lang.String address) throws javax.xml.rpc.ServiceException {

		if ("ConfluenceserviceV1".equals(portName)) {
			setConfluenceserviceV1EndpointAddress(address);
		}
		else { // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(final javax.xml.namespace.QName portName, final java.lang.String address) throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}

}
