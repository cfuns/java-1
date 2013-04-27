package de.benjaminborbe.authentication.core.verifycredential;

import de.benjaminborbe.tools.registry.RegistryBase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthenticationVerifyCredentialRegistry extends RegistryBase<AuthenticationVerifyCredential> {

	@Inject
	public AuthenticationVerifyCredentialRegistry(
		final AuthenticationVerifyCredentialLdap verifyCredentialLdap,
		final AuthenticationVerifyCredentialStorage verifyCredentialStorage
	) {
		add(verifyCredentialStorage);
		add(verifyCredentialLdap);
	}

}
