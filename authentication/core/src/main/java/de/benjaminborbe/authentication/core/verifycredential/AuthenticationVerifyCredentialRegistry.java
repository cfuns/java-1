package de.benjaminborbe.authentication.core.verifycredential;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.tools.registry.RegistryBase;

@Singleton
public class AuthenticationVerifyCredentialRegistry extends RegistryBase<AuthenticationVerifyCredential> {

	@Inject
	public AuthenticationVerifyCredentialRegistry(final AuthenticationVerifyCredentialLdap verifyCredentialLdap, final AuthenticationVerifyCredentialStorage verifyCredentialStorage) {
		add(verifyCredentialStorage);
		add(verifyCredentialLdap);
	}

}
