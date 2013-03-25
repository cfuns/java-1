package de.benjaminborbe.authentication.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.AuthenticationConstants;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class AuthenticationConfigImpl extends ConfigurationBase implements AuthenticationConfig {

	// ldap://prolog.rp.seibert-media.net:389/
	// ldap://prolog.rp.seibert-media.net:636/
	// ldaps://prolog.rp.seibert-media.net:636/

	private final ConfigurationDescriptionString providerUrl = new ConfigurationDescriptionString("ldap://prolog.rp.seibert-media.net:636/", "AuthenticationLdapProviderUrl",
			"Authentication Ldap ProviderUrl");

	private final ConfigurationDescriptionString domain = new ConfigurationDescriptionString("rp", "AuthenticationLdapDomain", "Authentication Ldap Domain");

	private final ConfigurationDescriptionString credentials = new ConfigurationDescriptionString(null, "AuthenticationLdapCredentials", "Authentication Ldap Credentials");

	private final ConfigurationDescriptionBoolean ssl = new ConfigurationDescriptionBoolean(true, "AuthenticationLdapSSL", "Authentication Ldap SSL");

	private final ConfigurationDescriptionBoolean ldapEnabled = new ConfigurationDescriptionBoolean(false, "AuthenticationLdapEnabled", "Authentication Ldap Enabled");

	private final ConfigurationDescriptionString emailFrom = new ConfigurationDescriptionString("noreply@example.com", AuthenticationConstants.CONFIG_EMAIL_FROM,
			"Authentication Email From");

	@Inject
	public AuthenticationConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(providerUrl);
		result.add(domain);
		result.add(credentials);
		result.add(ssl);
		result.add(ldapEnabled);
		result.add(emailFrom);
		return result;
	}

	@Override
	public String getProviderUrl() {
		return getValueString(providerUrl);
	}

	@Override
	public String getDomain() {
		return getValueString(domain);
	}

	@Override
	public String getCredentials() {
		return getValueString(credentials);
	}

	@Override
	public boolean isSSL() {
		return getValueBoolean(ssl);
	}

	@Override
	public boolean isLdapEnabled() {
		return getValueBoolean(ldapEnabled);
	}

	@Override
	public String getEmailFrom() {
		return getValueString(emailFrom);
	}
}
