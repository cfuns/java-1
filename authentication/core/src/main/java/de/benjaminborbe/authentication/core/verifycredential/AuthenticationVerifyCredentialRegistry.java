package de.benjaminborbe.authentication.core.verifycredential;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.tools.registry.RegistryBase;

@Singleton
public class AuthenticationVerifyCredentialRegistry extends RegistryBase<AuthenticationVerifyCredential> {

	@Inject
	public AuthenticationVerifyCredentialRegistry(final AuthenticationVerifyCredentialLdap verifyCredentialLdap, final AuthenticationVerifyCredentialStorage verifyCredentialStorage) {
		add(verifyCredentialStorage);
		add(verifyCredentialLdap);
	}

}
