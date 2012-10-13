package de.benjaminborbe.authentication.verifycredential;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.registry.RegistryBase;

@Singleton
public class VerifyCredentialRegistry extends RegistryBase<VerifyCredential> {

	@Inject
	public VerifyCredentialRegistry(final VerifyCredentialLdap verifyCredentialLdap, final VerifyCredentialStorage verifyCredentialStorage) {
		add(verifyCredentialLdap);
		add(verifyCredentialStorage);
	}

}
