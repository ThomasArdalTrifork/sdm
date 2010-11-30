package com.trifork.sdm.replication.security;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.Signature;
import java.util.Arrays;

import javax.inject.Named;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.bouncycastle.util.encoders.Hex;


public class RSASecurityModule implements SecurityModule {

	private Signature signer;
	private Signature verifier;


	public RSASecurityModule(@Named("CertificateFile") File certificateFile) throws IOException,
			NoSuchAlgorithmException, InvalidKeyException {

		Security.addProvider(new BouncyCastleProvider());

		File privateKey = new File("private.pem");
		KeyPair keyPair = readKeyPair(privateKey, "password".toCharArray());

		signer = Signature.getInstance("SHA256WithRSAEncryption");
		signer.initSign(keyPair.getPrivate());

		verifier = Signature.getInstance("SHA256WithRSAEncryption");
		verifier.initVerify(keyPair.getPublic());
	}


	public void signMessage(String message) throws Exception {

		signer.update(message.getBytes());

		byte[] signatureBytes = signer.sign();
		String messageSignature = new String(Hex.encode(signatureBytes));
		
		message += messageSignature;
	}


	public String verifyMessage(String message) {
		
		byte[] signedBytes = getSignedBytes(message);
		
		verifier.update(signedMessage.getBytes());

		boolean isValid = verifier.verify(signatureBytes);
		
		
	}
	
	private byte[] fetchSignatureBytes() {
		
		
		
		return null;
	}


	private static KeyPair readKeyPair(File privateKey, char[] keyPassword) throws IOException {

		FileReader fileReader = new FileReader(privateKey);
		PEMReader r = new PEMReader(fileReader, new DefaultPasswordFinder(keyPassword));
		try {
			return (KeyPair) r.readObject();
		}
		catch (IOException ex) {
			throw new IOException("The private key could not be decrypted", ex);
		}
		finally {
			r.close();
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
