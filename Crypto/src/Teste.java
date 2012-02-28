import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Teste {

	public static void main(String[] args) throws Exception {
		// geracaoChaves();
		// return readPvtKey();

		String crypt = encrypt();
		System.out.printf("Criptografado: '%s'\n", crypt);
		String decript = decrypt(crypt);
		System.out.printf("Descriptografado: '%s'\n", decript);
//		String crypt = encryptDecryptFile("Texto para ser criptografado", readPubKey(), Cipher.ENCRYPT_MODE);
//		System.out.printf("Criptografado: '%s'\n", crypt);
//		String decript = encryptDecryptFile(crypt, readPvtKey(), Cipher.DECRYPT_MODE);
//		System.out.printf("Descriptografado: '%s'\n", decript);
	}

	/**
	 * Encrypt and Decrypt files using 1024 RSA encryption
	 * 
	 * @param srcFileName
	 *            Source file name
	 * @param destFileName
	 *            Destination file name
	 * @param key
	 *            The key. For encryption this is the Private Key and for
	 *            decryption this is the public key
	 * @param cipherMode
	 *            Cipher Mode
	 * @throws Exception
	 */
	public static String encryptDecryptFile(String srcFileName, Key key, int cipherMode)
			throws Exception {
		StringBuilder ret = new StringBuilder();
		InputStream inputReader = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			byte[] buf = cipherMode == Cipher.ENCRYPT_MODE ? new byte[100] : new byte[128];
			int bufl;
			// init the Cipher object for Encryption…
			cipher.init(cipherMode, key);
			// start FileIO
			inputReader = new ByteArrayInputStream(srcFileName.getBytes());
			while ((bufl = inputReader.read(buf)) != -1) {
				byte[] encText = null;
				if (cipherMode == Cipher.ENCRYPT_MODE) {
					encText = encrypt(copyBytes(buf, bufl), (PublicKey) key);
				} else {
					encText = decrypt(copyBytes(buf, bufl), (PrivateKey) key);
				}
				ret.append(new String(encText));
			}
		} finally {
			try {
				if (inputReader != null) {
					inputReader.close();
				}
			} catch (Exception e) {
				// do nothing…
			}
		}
		return ret.toString();
	}

	/**
	 * Encrypt a text using public key.
	 * 
	 * @param text
	 *            The original unencrypted text
	 * @param key
	 *            The public key
	 * @return Encrypted text
	 * @throws java.lang.Exception
	 */
	public static byte[] encrypt(byte[] text, PublicKey key) throws Exception {
		byte[] cipherText = null;
		// get an RSA cipher object and print the provider
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		System.out.println("nProvider is: " + cipher.getProvider().getInfo());
		// encrypt the plaintext using the public key
		cipher.init(Cipher.ENCRYPT_MODE, key);
		cipherText = cipher.doFinal(text);
		return cipherText;
	}

	/**
	 * Decrypt text using private key
	 * 
	 * @param text
	 *            The encrypted text
	 * @param key
	 *            The private key
	 * @return The unencrypted text
	 * @throws java.lang.Exception
	 */
	public static byte[] decrypt(byte[] text, PrivateKey key) throws Exception {
		byte[] dectyptedText = null;
		// decrypt the text using the private key
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		dectyptedText = cipher.doFinal(text);
		return dectyptedText;
	}

	public static byte[] copyBytes(byte[] arr, int length) {
		byte[] newArr = null;
		if (arr.length == length) {
			newArr = arr;
		} else {
			newArr = new byte[length];
			for (int i = 0; i < length; i++) {
				newArr[i] = (byte) arr[i];
			}
		}
		return newArr;
	}

	private static String encrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException {
		String TYPE = "RSA/ECB/PKCS1Padding";
		Cipher cipher = Cipher.getInstance(TYPE);
		cipher.init(Cipher.ENCRYPT_MODE, readPubKey());
		return Base64.encode(cipher.doFinal("Texto para ser criptografado".getBytes()));
	}

	private static String decrypt(String textToDecrypt) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException {
		String TYPE = "RSA/ECB/PKCS1Padding";
		Cipher cipher = Cipher.getInstance(TYPE);
		cipher.init(Cipher.DECRYPT_MODE, readPvtKey());
		StringBuilder sb = new StringBuilder(textToDecrypt);
		while (sb.length() > 0) {
			int end = 100;
			if (sb.length() < 100) {
				end = sb.length();
			}
			String decrypt = sb.substring(0, end);
			cipher.update(decrypt.getBytes());
			sb.delete(0, end);
		}
		byte[] doFinal = cipher.doFinal();
		return Base64.encode(doFinal);
	}

	private static PrivateKey readPvtKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		File pvtFile = new File("chave-privada.key");
		FileInputStream fis = new FileInputStream(pvtFile);
		ByteBuffer byteBuffer = ByteBuffer.allocate(fis.available());
		fis.getChannel().read(byteBuffer);
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(byteBuffer.array());
		return keyFactory.generatePrivate(privateKeySpec);
	}

	private static PublicKey readPubKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		File pubFile = new File("chave-publica.key");
		FileInputStream fis = new FileInputStream(pubFile);
		ByteBuffer byteBuffer = ByteBuffer.allocate(fis.available());
		fis.getChannel().read(byteBuffer);
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(byteBuffer.array());
		return keyFactory.generatePublic(publicKeySpec);
	}

	private static void geracaoChaves() throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		keyGen.initialize(2048, random);
		KeyPair pair = keyGen.generateKeyPair();
		File pvtFile = new File("chave-privada.key");
		FileOutputStream pvt = new FileOutputStream(pvtFile);
		System.out.printf("Generate private key in '%s'\n", pvtFile.getAbsolutePath());
		pvt.getChannel().write(ByteBuffer.wrap(pair.getPrivate().getEncoded()));
		File pubFile = new File("chave-publica.key");
		FileOutputStream pub = new FileOutputStream(pubFile);
		System.out.printf("Generate public key in '%s'\n", pubFile.getAbsolutePath());
		pub.getChannel().write(ByteBuffer.wrap(pair.getPublic().getEncoded()));
	}
}
