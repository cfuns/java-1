package de.benjaminborbe.authentication.ldap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.config.AuthenticationConfig;

@Singleton
public class LdapConnector {

	private final AuthenticationConfig authenticationConfig;

	private final Logger logger;

	@Inject
	public LdapConnector(final Logger logger, final AuthenticationConfig authenticationConfig) {
		this.logger = logger;
		this.authenticationConfig = authenticationConfig;
	}

	public boolean verify(final String username, final String password) {
		final Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, authenticationConfig.getProviderUrl());
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, authenticationConfig.getDomain() + "\\" + username);
		env.put(Context.SECURITY_CREDENTIALS, password);
		if (authenticationConfig.isSSL()) {
			// Specify SSL
			env.put(Context.SECURITY_PROTOCOL, "ssl");
		}
		try {
			new InitialDirContext(env);
			logger.debug("login success for " + username);
			return true;
		}
		catch (final NamingException e) {
			logger.info("login fail for " + username);
			logger.debug(e.getClass().getName(), e);
			return false;
		}
	}

	public String getFullname(final String username) throws NamingException {
		final Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, authenticationConfig.getProviderUrl());
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "cn=ldaplookup,ou=Extern,ou=Mitarbeiter,dc=rp,dc=seibert-media,dc=net");
		env.put(Context.SECURITY_CREDENTIALS, authenticationConfig.getCredentials());
		final DirContext ctx = new InitialDirContext(env);

		final SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		final NamingEnumeration<SearchResult> enumeration = ctx.search("ou=Mitarbeiter,dc=rp,dc=seibert-media,dc=net", "(objectClass=person)", searchControls);
		while (enumeration.hasMore()) {
			final SearchResult searchResult = enumeration.next();
			final Attributes attrs = searchResult.getAttributes();
			final Attribute cn = attrs.get("cn");
			if (String.valueOf(cn).equalsIgnoreCase(username)) {
				return String.valueOf(attrs.get("displayName"));
			}
		}
		return null;
	}

	public Collection<String> getUsernames() throws NamingException {
		final Set<String> result = new HashSet<String>();
		final Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, authenticationConfig.getProviderUrl());
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "cn=ldaplookup,ou=Extern,ou=Mitarbeiter,dc=rp,dc=seibert-media,dc=net");
		env.put(Context.SECURITY_CREDENTIALS, authenticationConfig.getCredentials());
		final DirContext ctx = new InitialDirContext(env);

		final SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		final NamingEnumeration<SearchResult> enumeration = ctx.search("ou=Mitarbeiter,dc=rp,dc=seibert-media,dc=net", "(objectClass=person)", searchControls);
		while (enumeration.hasMore()) {
			final SearchResult searchResult = enumeration.next();
			final Attributes attrs = searchResult.getAttributes();
			final Attribute cn = attrs.get("cn");
			result.add(String.valueOf(cn));
		}
		return result;
	}
}
