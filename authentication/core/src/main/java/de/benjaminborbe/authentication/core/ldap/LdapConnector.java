package de.benjaminborbe.authentication.core.ldap;

import de.benjaminborbe.authentication.core.config.AuthenticationConfig;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

@Singleton
public class LdapConnector {

	private static final int TIMEOUT = 5000;

	private final AuthenticationConfig authenticationConfig;

	private final Logger logger;

	@Inject
	public LdapConnector(final Logger logger, final AuthenticationConfig authenticationConfig) {
		this.logger = logger;
		this.authenticationConfig = authenticationConfig;
	}

	public boolean verify(final String username, final String password) throws LdapException {
		try {
			final Hashtable<String, String> env = getUserEnv(username, password);
			final DirContext ctx = new InitialDirContext(env);
			logger.debug("login success for " + username);
			final SearchControls searchControls = new SearchControls();
			searchControls.setTimeLimit(TIMEOUT);
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			ctx.search("ou=Mitarbeiter,dc=rp,dc=seibert-media,dc=net", "(objectClass=person)", searchControls);
			return true;
		} catch (final javax.naming.AuthenticationException e) {
			logger.info("login fail for " + username);
			return false;
		} catch (final NamingException e) {
			logger.trace(e.getClass().getName(), e);
			return false;
		}
	}

	public String getFullname(final String username) throws LdapException {
		try {
			final Hashtable<String, String> env = getReadEnv();
			final DirContext ctx = new InitialDirContext(env);
			final SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			final NamingEnumeration<SearchResult> enumeration = ctx.search("ou=Mitarbeiter,dc=rp,dc=seibert-media,dc=net", "(objectClass=person)", searchControls);
			while (enumeration.hasMore()) {
				final SearchResult searchResult = enumeration.next();
				final Attributes attrs = searchResult.getAttributes();
				final String cn = String.valueOf(attrs.get("cn").get());
				logger.trace("compare " + username + " <=> " + cn);
				if (cn.equalsIgnoreCase(username)) {
					final String displayName = String.valueOf(attrs.get("displayName").get());
					logger.debug("found user " + username + " => " + displayName);
					return displayName;
				}
			}
			logger.debug("no user found " + username);
			return null;
		} catch (final NamingException e) {
			throw new LdapException(e);
		}
	}

	public Collection<String> getUsernames() throws LdapException {
		try {
			final Set<String> result = new HashSet<String>();
			final Hashtable<String, String> env = getReadEnv();
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
		} catch (final NamingException e) {
			throw new LdapException(e);
		}
	}

	private Hashtable<String, String> getBaseEnv() {
		final Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, authenticationConfig.getProviderUrl());
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		if (authenticationConfig.isSSL()) {
			env.put(Context.SECURITY_PROTOCOL, "ssl");
		}
		return env;
	}

	private Hashtable<String, String> getReadEnv() {
		final Hashtable<String, String> env = getBaseEnv();
		env.put(Context.SECURITY_PRINCIPAL, "cn=ldaplookup,ou=Extern,ou=Mitarbeiter,dc=rp,dc=seibert-media,dc=net");
		env.put(Context.SECURITY_CREDENTIALS, authenticationConfig.getCredentials());
		return env;
	}

	private Hashtable<String, String> getUserEnv(final String username, final String password) {
		final Hashtable<String, String> env = getBaseEnv();
		env.put(Context.SECURITY_PRINCIPAL, authenticationConfig.getDomain() + "\\" + username);
		env.put(Context.SECURITY_CREDENTIALS, password);
		return env;
	}
}
