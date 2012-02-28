package andre.keytool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class Md5Fingerprint {

	public String generate() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		File certFile = new java.io.File(
				"C:\\Users\\limao\\.android\\debug.keystore");
		//Maps API key = 08v3Kp1N7z0ZM1cCr7yHH9Yu35od8l36rCcz8YA
		FileInputStream is = new java.io.FileInputStream(certFile);
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(is, "android".toCharArray());
		Certificate c = ks.getCertificate("androiddebugkey");
		MessageDigest messagedigest = MessageDigest.getInstance("MD5");
		byte fingerprint[] = messagedigest.digest(c.getEncoded());
		return toHexString(fingerprint);
	}

	private static String toHexString(byte abyte0[]) {
		StringBuffer stringbuffer = new StringBuffer();
		int i = abyte0.length;

		for (int j = 0; j < i; j++) {
			byte2hex(abyte0[j], stringbuffer);
			if (j < i - 1)
				stringbuffer.append(":");
		}
		return stringbuffer.toString();
	}

	private static void byte2hex(byte byte0, StringBuffer stringbuffer) {
		char ac[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };

		int i = (byte0 & 0xf0) >> 4;
		int j = byte0 & 0xf;
		stringbuffer.append(ac[i]);
		stringbuffer.append(ac[j]);
	}
}
