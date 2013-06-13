package de.benjaminborbe.authentication.core.util;

import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.Base64UtilImpl;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AuthenticationPasswordEncryptionServiceUnitTest {

	@Test
	public void testEncryptPassword() throws InvalidKeySpecException, NoSuchAlgorithmException {
		Base64Util base64Util = new Base64UtilImpl();
		String password = "Test123!";
		String passwordEncrypted = "u4yh3L84yYWLa/Jz/A+vUdGkSQ0=";
		String passwordSalt = "VSUg4kHtGag=";
		AuthenticationPasswordEncryptionService authenticationPasswordEncryptionService = new AuthenticationPasswordEncryptionService();
		assertThat(base64Util.encode(authenticationPasswordEncryptionService.encryptPassword(password, base64Util.decode(passwordSalt))), is(passwordEncrypted));
	}
}
