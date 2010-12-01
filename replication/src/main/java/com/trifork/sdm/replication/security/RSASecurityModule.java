package com.trifork.sdm.replication.security;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyPair;
import java.security.Security;
import java.security.Signature;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.bouncycastle.util.encoders.Hex;

import com.trifork.sdm.replication.FailureNotifier;


public class RSASecurityModule implements SecurityModule {


	private KeyPair keyPair;
	private final FailureNotifier notifier;

	private final static String ENCRYPTION_ALGORITHM = "SHA256WithRSAEncryption";


	@Inject
	public RSASecurityModule(@Named("PrivateKeyFile") File privateKeyFile,
			@Named("PrivateKeyPassword") String password, FailureNotifier notifier) {

		this.notifier = notifier;

		try {
			Security.addProvider(new BouncyCastleProvider());
			keyPair = readKeyPair(privateKeyFile, "password");
		}
		catch (Throwable t) {
			// The signature file could not be read, the application
			// should fail.
			notifier.fatal("Could not initialize the security module.", t);
		}
	}


	/**
	 * Signs a message <i>M</i> by appending the signature to the string.
	 * 
	 * For the signing algorithm <i>S</i> and the messages signature <i>M' =
	 * S(M)</i>, the resulting string will be M:M'. The message and signature
	 * are separated by the <code>MESSAGE_SEPARATOR</code>.
	 * 
	 * @param message
	 *            The string object that will be appended with its own
	 *            signature.
	 */
	public void signMessage(String message) {

		try {
			Signature signer = Signature.getInstance(ENCRYPTION_ALGORITHM);
			signer.initSign(keyPair.getPrivate());
			signer.update(message.getBytes());

			byte[] signatureBytes = signer.sign();
			String messageSignature = new String(Hex.encode(signatureBytes));

			message = String.format("%s%s%s", message, MESSAGE_SEPARATOR, messageSignature);
		}
		catch (Throwable t) {
			notifier.error("Security Module could not sign a message.", t);
		}
	}


	/**
	 * Verifies that a message has been signed by the private key.
	 * 
	 * @param signedMessage
	 *            The signed message to be verified.
	 * 
	 * @return The message stripped of the signature, or <code>null</code> if
	 *         the message could not be verified.
	 */
	public String verifyMessage(String signedMessage) {

		String result = null;

		try {
			final Signature verifier = Signature.getInstance(ENCRYPTION_ALGORITHM);
			verifier.initVerify(keyPair.getPublic());

			final String[] parts = signedMessage.split(MESSAGE_SEPARATOR);
			final String message = parts[0];
			final String signature = parts[1];

			verifier.update(message.getBytes());

			final byte[] signatureBytes = Hex.decode(signature);

			if (verifier.verify(signatureBytes)) {
				result = message;
			}
			else {
				// TODO: Move this to calling code.
				String errorMessage = String.format(
						"The security module received a corrupted message:\n\n%s\n\n",
						signedMessage);
				notifier.warn(errorMessage);
			}
		}
		catch (Throwable t) {
			// There is no way we can recover from this..
			// The application should fail.
			notifier.error("", t);
		}

		return result;
	}


	private static KeyPair readKeyPair(File privateKey, String password) throws IOException {

		char[] keyPassword = password.toCharArray();

		FileReader fileReader = new FileReader(privateKey);
		PEMReader pemReader = new PEMReader(fileReader, new DefaultPasswordFinder(keyPassword));

		try {
			return (KeyPair) pemReader.readObject();
		}
		catch (IOException e) {
			throw new IOException("The private key could not be decrypted.", e);
		}
		finally {
			pemReader.close();
			fileReader.close();
		}
	}


	private static class DefaultPasswordFinder implements PasswordFinder {

		private final char[] password;


		private DefaultPasswordFinder(char[] password) {

			this.password = password;
		}


		@Override
		public char[] getPassword() {

			return Arrays.copyOf(password, password.length);
		}
	}
}
